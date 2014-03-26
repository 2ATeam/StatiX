package def.statix.fragments;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import def.statix.ActivityMain;
import def.statix.R;
import def.statix.construction.ConstructionUnit;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;
import def.statix.construction.unittypes.PlankType;
import def.statix.rendering.SceneController;
import def.statix.utils.capricom.ArcMenu;
import def.statix.utils.drag.DragSource;
import def.statix.utils.drag.DragView;
import def.statix.utils.drag.DropTarget;
import def.statix.utils.drag.DropableSurface;
import def.statix.utils.ui.GestureAdapter;
import def.statix.utils.ui.GestureHandler;
import def.statix.utils.ui.StatusManager;
import def.statix.utils.ui.editors.UnitEditor;
import def.statix.utils.ui.editors.UnitEditorManager;

/**
 * Created by Lux on 16.02.14.
 */
public class RenderingSurfaceFragment extends Fragment implements View.OnTouchListener {


    private DropableSurface frame;
    private SceneController sceneController;
    private GestureDetectorCompat gestureDetector;
    private GestureHandler gestureHandler;

    private ArcMenu overlayMenu;
    private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera,
            R.drawable.composer_music,
            R.drawable.composer_place,
            R.drawable.composer_place,
            R.drawable.shaolin, //shaolins are placeholders to move 'close' button down
            R.drawable.shaolin,
            R.drawable.composer_sleep};


    private enum ModifyActions {NONE, CREATING, ROTATE_LEFT, ROTATE_RIGHT, SCALE_LEFT, SCALE_RIGHT;}

    ;

    public void startCreatingPlank() {
        modifyAction = ModifyActions.CREATING;
    }

    public void stopCreatingPlank() {
        if (modifyAction == ModifyActions.CREATING)
            modifyAction = ModifyActions.NONE;
    }

    private ModifyActions modifyAction = ModifyActions.NONE;

    /**
     * Handler of the dragging, which responses for creating objects
     */
    private class DragHandler implements DropTarget {

        @Override
        public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
            if (!(dragInfo instanceof ConstructionUnitType)) return; // ignore garbage if any...

            if (dragInfo instanceof PlankType) {
                sceneController.beginPlank(x, y);
            } else if (dragInfo instanceof BindingType) {
                sceneController.addBinding(x, y, (BindingType) dragInfo);
            } else if (dragInfo instanceof ForceType) {
                sceneController.addForce(x, y, (ForceType) dragInfo);
            }

        }

        @Override
        public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
            ///TODO: Check for accessibility to drop in requested location
            return true;
        }

        @Override
        public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
            ///TODO: here can be placed some "magnetic" features to clamp dropped object
            return null;
        }

        @Override
        public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        }

        @Override
        public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        }

        @Override
        public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        }

        @Override
        public void getHitRect(Rect outRect) {
            frame.getHitRect(outRect);
        }

        @Override
        public void getLocationOnScreen(int[] loc) {
            frame.getLocationOnScreen(loc);
        }

        @Override
        public int getLeft() {
            return frame.getLeft();
        }

        @Override
        public int getTop() {
            return frame.getTop();
        }
    }


    private void initDragger() {
        ActivityMain activity = (ActivityMain) getActivity();
        assert (activity != null);
        activity.getDragController().addDropTarget(frame); // connect surface to the dragController, so it can accept drops
        activity.getDragController().addDropTarget(new DragHandler()); // separate controller from view
    }

    private void initializeGestures() {
        GestureAdapter gestureAdapter = new GestureAdapter();
        gestureDetector = new GestureDetectorCompat(this.getActivity(), gestureAdapter);
        gestureHandler = new GestureHandler();
    }

    private void initOverlayMenu() {
        overlayMenu = new ArcMenu(this.getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        overlayMenu.setLayoutParams(params);
        overlayMenu.setDegreeses(210, 450);
        overlayMenu.setChildSize(30);
        final int itemCount = ITEM_DRAWABLES.length;

        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this.getActivity());
            item.setImageResource(ITEM_DRAWABLES[i]);
            item.setId(i);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case 0:
                            modifyAction = ModifyActions.SCALE_LEFT;
                            StatusManager.setWarning("Scaling left...");
                            break;
                        case 1:
                            modifyAction = ModifyActions.ROTATE_LEFT;
                            StatusManager.setWarning("Rotating left...");
                            break;
                        case 2:
                            modifyAction = ModifyActions.ROTATE_RIGHT;
                            StatusManager.setWarning("Rotating right...");
                            break;
                        case 3:
                            modifyAction = ModifyActions.SCALE_RIGHT;
                            StatusManager.setWarning("Scaling right...");

                            break;
                        default:
                            modifyAction = ModifyActions.NONE;
                            break;
                    }
                    hideOverlayMenu();
                }
            });

            overlayMenu.addItem(item);
            if (i == itemCount - 1) // close item
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sceneController.removeSelected();
                        UnitEditorManager.getInstance().hideActiveEditor();
                        hideOverlayMenu();
                    }
                });
        }
        // makes those shaolins invisible....
        overlayMenu.getItem(itemCount - 2).setVisibility(View.INVISIBLE);
        overlayMenu.getItem(itemCount - 3).setVisibility(View.INVISIBLE);

        overlayMenu.setMainButtonVisible(false);
        overlayMenu.expand(false);
        hideOverlayMenu();
    }

    private void moveOverlayMenu(int x, int y) {
        assert overlayMenu.getLayoutParams() != null;
        ((DropableSurface.LayoutParams) overlayMenu.getLayoutParams()).setMargins(x - overlayMenu.getWidth() / 2, y - overlayMenu.getHeight() / 2, 0, 0);
        overlayMenu.requestLayout();
    }

    private void hideOverlayMenu() {
        overlayMenu.setVisibility(View.INVISIBLE);
    }

    private void showOverlayMenu() {
        overlayMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sceneController = new SceneController(getActivity());
        initializeGestures();
        frame = new DropableSurface(this.getActivity());
        frame.setClipChildren(false);
        frame.setLayoutParams(new DropableSurface.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sceneController.getSurface().setOnTouchListener(this);
        frame.addView(sceneController.getSurface());

        initOverlayMenu();
        initDragger();
        frame.addView(overlayMenu);

        sceneController.addForce(100, 100, ForceType.DISTRIBUTED);
        sceneController.addForce(300, 300, ForceType.DISTRIBUTED);
        sceneController.addForce(200, 200, ForceType.DISTRIBUTED);
        StatusManager.setStatus(getString(R.string.hint_create_object));
        return frame;
    }

    @Override
    public void onPause() {
        super.onPause();
        sceneController.getSurface().pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        sceneController.getSurface().resume();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        if (modifyAction != ModifyActions.NONE)  // calculates only when something is gonna be modified
            gestureHandler.onTouchEvent(motionEvent);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                hideOverlayMenu();
                if (modifyAction == ModifyActions.CREATING) {
                    sceneController.beginPlank(motionEvent.getX(), motionEvent.getY()); // move plank creation from menu for clarity (menu must only switches actions)
                    StatusManager.setStatus(this.getActivity().getString(R.string.hint_creation_edit_object));
                } else {
                    sceneController.select((int) motionEvent.getX(), (int) motionEvent.getY());
                    if (sceneController.isObjectSelected()) {
                        UnitEditor editor = UnitEditorManager.getInstance().getEditorForUnit((ConstructionUnit) sceneController.getSelected());
                        UnitEditorManager.getInstance().hideActiveEditor();
                        UnitEditorManager.getInstance().showEditor(editor);
                        PointF rotCenter = new PointF(sceneController.getSelected().getBoundingRect().centerX(),
                        sceneController.getSelected().getBoundingRect().centerY()); // rotate around whatever you want
                        gestureHandler.setRotationCenter(rotCenter);
                    } else {
                        UnitEditorManager.getInstance().hideActiveEditor();
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                /// TODO: Transform here using the dx value (evaluated in density points)
                switch (modifyAction) {
                    case ROTATE_LEFT:
                        StatusManager.setWarning("Rotating left on " + gestureHandler.getAngle() + " degrees.");
                        break;
                    case ROTATE_RIGHT:
                        StatusManager.setWarning("Rotating right on " + gestureHandler.getAngle() + " degrees.");
                        break;
                    case SCALE_LEFT:
                        StatusManager.setWarning("Scaling left on " + gestureHandler.getDX() + " points.");
                        break;
                    case SCALE_RIGHT:
                        StatusManager.setWarning("Scaling left on " + gestureHandler.getDX() + " points.");
                        break;
                    default:
                        break;
                }
                if (sceneController.getUnconfirmedPlank() != null) {
                    sceneController.editPlank(gestureHandler.getDX(), gestureHandler.getDY());
                } else if (sceneController.isObjectSelected()) {
                    sceneController.translateSelected(motionEvent.getX(), motionEvent.getY());
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                modifyAction = ModifyActions.NONE;
                /// TODO: apply transform here if needed
                if (sceneController.getUnconfirmedPlank() != null) {
                    sceneController.confirmPlank();
                } else if (sceneController.isObjectSelected()) {
                    sceneController.applyTransformToSelected();
                    RectF r = sceneController.getSelected().getBoundingRect();
                    moveOverlayMenu((int) r.centerX(), (int) r.centerY());
                    showOverlayMenu();
                } else {
                    hideOverlayMenu();
                }
                break;
            }
        }
        return true;
    }
}
