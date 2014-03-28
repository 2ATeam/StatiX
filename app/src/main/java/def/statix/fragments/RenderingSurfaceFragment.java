package def.statix.fragments;

import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import def.statix.ActivityMain;
import def.statix.R;
import def.statix.construction.ConstructionUnit;
import def.statix.construction.Force;
import def.statix.construction.Plank;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;
import def.statix.construction.unittypes.PlankType;
import def.statix.editors.UnitEditor;
import def.statix.editors.UnitEditorManager;
import def.statix.grid.GridRenderer;
import def.statix.rendering.SceneController;
import def.statix.utils.drag.DragSource;
import def.statix.utils.drag.DragView;
import def.statix.utils.drag.DropTarget;
import def.statix.utils.drag.DropableSurface;
import def.statix.utils.ui.GestureHandler;
import def.statix.utils.ui.StatusManager;

public class RenderingSurfaceFragment extends Fragment implements View.OnTouchListener {

    private static final float DRAG_SENSIVITY = 5;
    private DropableSurface frame;
    private DragHandler dragHandler;
    private SceneController sceneController;
    private GestureHandler gestureHandler;

    public SceneController getScene() {
        return sceneController;
    }

    private enum ModifyActions {NONE, CREATING, ROTATE_LEFT, ROTATE_RIGHT, SCALE_LEFT, SCALE_RIGHT}

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
                Plank p = (Plank) sceneController.getSelected();
                sceneController.addForce(x, y, (ForceType) dragInfo);
                ((Force) sceneController.getSelected()).applyToPlank(p);
            }

        }

        @Override
        public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
            sceneController.select(x, y);
            return (sceneController.isObjectSelected() && ((sceneController.getSelected()).getType() == PlankType.PLANK));
            ///TODO: Check for accessibility to drop in requested location
        }

        @Override
        public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
            ///TODO: here can be placed some "magnetic" features to clamp dropped object
            return null;
        }

        @Override
        public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
            sceneController.getSurface().getGridRenderer().getGrid().setShowGuides(true);
        }

        @Override
        public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
            sceneController.getSurface().getGridRenderer().setGridGuideLine(x, y);
        }

        @Override
        public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
            sceneController.getSurface().getGridRenderer().getGrid().setShowGuides(false);
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
        dragHandler = new DragHandler();
        activity.getDragController().addDropTarget(dragHandler); // separate controller from view
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        gestureHandler = new GestureHandler();
        initDragger();
        frame = new DropableSurface(getActivity().getApplicationContext());
        frame.setClipChildren(false);
        frame.setLayoutParams(new DropableSurface.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sceneController = new SceneController(getActivity().getApplicationContext());
        frame.addView(sceneController.getSurface());
        sceneController.getSurface().setOnTouchListener(this);

        UnitEditorManager.getInstance().setScene(sceneController);
        StatusManager.setStatus(getString(R.string.hint_create_object));
        return frame;
    }

    @Override
    public void onDestroy() {
        frame.removeAllViews();
        ActivityMain activity = (ActivityMain) getActivity();
        assert (activity != null);
        activity.getDragController().removeDropTarget(frame);
        activity.getDragController().removeDropTarget(dragHandler);
        super.onDestroy();
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

    public GridRenderer getGridRender() {
        return sceneController.getSurface().getGridRenderer();
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureHandler.onTouchEvent(motionEvent);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                StatusManager.setStatus("");
                //    test();
                sceneController.getSurface().getGridRenderer().getGrid().setShowGuides(true);
                if (modifyAction == ModifyActions.CREATING) {
                    sceneController.beginPlank(motionEvent.getX(), motionEvent.getY());
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
                if (gestureHandler.getDX() > DRAG_SENSIVITY || gestureHandler.getDY() > DRAG_SENSIVITY) {
                    UnitEditorManager.getInstance().hideActiveEditor();
                }
                sceneController.getSurface().getGridRenderer().setGridGuideLine(motionEvent.getX(), motionEvent.getY());
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
                if (motionEvent.getX() < 0) {
                    sceneController.removeSelected();
                    UnitEditorManager.getInstance().hideActiveEditor();
                }
                sceneController.getSurface().getGridRenderer().getGrid().setShowGuides(false);
                modifyAction = ModifyActions.NONE;
                /// TODO: apply transform here if needed
                if (sceneController.getUnconfirmedPlank() != null) {
                    sceneController.confirmPlank();
                } else if (sceneController.isObjectSelected()) {
                    sceneController.applyTransformToSelected();
                }
                break;
            }
        }
        return true;
    }
}
