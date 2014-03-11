package def.statix.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
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
import android.widget.RelativeLayout;

import java.util.ArrayList;

import def.statix.R;
import def.statix.construction.Force;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ForceType;
import def.statix.rendering.SceneController;
import utils.capricom.ArcMenu;
import utils.touchmenotapps.RadialMenuItem;
import utils.touchmenotapps.RadialMenuWidget;
import utils.ui.GestureAdapter;
import utils.ui.GestureHandler;
import utils.ui.StatusManager;

/**
 * Created by Lux on 16.02.14.
 */
public class RenderingSurfaceFragment extends Fragment implements View.OnTouchListener {


    private GestureDetectorCompat gestureDetector;
    private RelativeLayout frame;
    private SceneController sceneController;
    private Point touch; /// TODO: get rid of it as soon as possible...

    private ArcMenu overlayMenu;
    private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_place, R.drawable.shaolin, R.drawable.shaolin, R.drawable.composer_sleep}; // 2 shaolins are placeholders to move 'close' button down
    // create enum depending on this id's:
    // 0 - scale left, 1 - rotate left, 2 - rotate right, 3 - scale right, 4 - close


    private enum ModifyActions {NONE, CREATING, ROTATE_LEFT, ROTATE_RIGHT, SCALE_LEFT, SCALE_RIGHT}

    ;
    private ModifyActions modifyAction = ModifyActions.NONE;

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
            if (i == itemCount - 1)
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sceneController.removeSelected();
                        hideOverlayMenu();
                    }
                });
        }
        // makes those 2 shaolins invisible....
        overlayMenu.getItem(itemCount - 2).setVisibility(View.INVISIBLE);
        overlayMenu.getItem(itemCount - 3).setVisibility(View.INVISIBLE);

        overlayMenu.setMainButtonVisible(false);
        overlayMenu.expand(false);
        hideOverlayMenu();
    }

    private void moveOverlayMenu(int x, int y) {
        assert overlayMenu.getLayoutParams() != null;
        ((RelativeLayout.LayoutParams) overlayMenu.getLayoutParams()).setMargins(x - overlayMenu.getWidth() / 2, y - overlayMenu.getHeight() / 2, 0, 0);
        overlayMenu.requestLayout();
    }

    private void hideOverlayMenu() {
        overlayMenu.setVisibility(View.INVISIBLE);
    }

    private void showOverlayMenu() {
        overlayMenu.setVisibility(View.VISIBLE);
    }

    private GestureHandler gestureHandler;

    private RadialMenuWidget creationMenu;
    private CreationMenuBuilder menuBuilder;

    private enum CreationMenuType {ALL, PLANK, FORCES, BINDINGS_AND_PLANKS}

    ;

    // Tool for building specific menus
    private class CreationMenuBuilder {


        private RadialMenuWidget menuPlank;
        private RadialMenuWidget menuBindingsAndForces;
        private RadialMenuWidget menuForces;
        private RadialMenuWidget menuAll;

        private CreationMenuBuilder() {
            menuPlank = buildCreationMenu(CreationMenuType.PLANK);
            menuForces = buildCreationMenu(CreationMenuType.FORCES);
            menuBindingsAndForces = buildCreationMenu(CreationMenuType.BINDINGS_AND_PLANKS);
            menuAll = buildCreationMenu(CreationMenuType.ALL);
        }

        public RadialMenuWidget getCreationMenu(CreationMenuType type) {
            switch (type) {
                case PLANK:
                    return menuPlank;
                case FORCES:
                    return menuForces;
                case BINDINGS_AND_PLANKS:
                    return menuBindingsAndForces;
                default:
                    return menuAll;
            }
        }

        public RadialMenuWidget buildCreationMenu(CreationMenuType type) {
            RadialMenuWidget r = initMenu();
            switch (type) {

                case PLANK:
                    r.setCenterCircle(createPlankItem());
                    break;
                case FORCES:
                    r = buildForcesMenu();
                    break;
                case BINDINGS_AND_PLANKS:
                    r = buildBindingsMenu();
                    r.setCenterCircle(createPlankItem());
                    break;
                default:
                    r.addMenuEntry(createPlankItem());
                    r.addMenuEntry(createBindingsItem());
                    r.addMenuEntry(createForcesItem());
                    break;
            }

            return r;
        }

        private RadialMenuWidget initMenu() {
            RadialMenuWidget menu = new RadialMenuWidget(RenderingSurfaceFragment.this.getActivity());
            menu.setAnimationSpeed(0L);
            menu.setIconSize(15, 30);
            menu.setTextSize(13);
            menu.setOutlineColor(Color.BLACK, 225);
            menu.setInnerRingColor(0x0099CC, 180);
            menu.setOuterRingColor(0x0099CC, 180);
            return menu;
        }

        private RadialMenuItem createCloseItem() {
            RadialMenuItem close = new RadialMenuItem("close", null);
            close.setDisplayIcon(R.drawable.composer_icn_plus_normal);
            close.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {
                    creationMenu.dismiss();
                }
            });
            return close;
        }

        private RadialMenuItem createPlankItem() {
            RadialMenuItem plank = new RadialMenuItem("plank", getString(R.string.units_beam));
            plank.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {

                    creationMenu.dismiss();
                    modifyAction = ModifyActions.CREATING;
                    StatusManager.setStatus(RenderingSurfaceFragment.this.getActivity().getString(R.string.hint_creating_object));
                }
            });
            plank.setDisplayIcon(R.drawable.plank_icon);
            return plank;
        }

        private RadialMenuItem createBindingStaticItem() {
            RadialMenuItem bStatic = new RadialMenuItem("static", getString(R.string.units_bindings_static));
            bStatic.setDisplayIcon(R.drawable.binding_stationary_icon);
            bStatic.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {
                    sceneController.addBinding(touch.x, touch.y, BindingType.STATIC);
                    creationMenu.dismiss();
                }
            });

            return bStatic;
        }

        private RadialMenuItem createBindingFixedItem() {
            RadialMenuItem bFixed = new RadialMenuItem("fixed", getString(R.string.units_bindings_fixed));
            bFixed.setDisplayIcon(R.drawable.binding_const_icon);
            bFixed.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {
                    sceneController.addBinding(touch.x, touch.y, BindingType.FIXED);
                    creationMenu.dismiss();
                }
            });
            return bFixed;
        }

        private RadialMenuItem createBindingMovableItem() {
            RadialMenuItem bMovable = new RadialMenuItem("movable", getString(R.string.units_bindings_movable));
            bMovable.setDisplayIcon(R.drawable.binding_movalbe_icon);
            bMovable.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {
                    sceneController.addBinding(touch.x, touch.y, BindingType.MOVABLE);
                    creationMenu.dismiss();
                }
            });
            return bMovable;
        }

        private RadialMenuItem createBindingsItem() {
            RadialMenuItem bindings = new RadialMenuItem("binding", getString(R.string.units_bindings));
            bindings.setDisplayIcon(R.drawable.binding_const_icon);
            ArrayList<RadialMenuItem> bindingsChildren = new ArrayList<>();
            bindingsChildren.add(createBindingFixedItem());
            bindingsChildren.add(createBindingMovableItem());
            bindingsChildren.add(createBindingStaticItem());
            bindings.setMenuChildren(bindingsChildren);
            return bindings;
        }

        private RadialMenuWidget buildBindingsMenu() {
            RadialMenuWidget menu = initMenu();
            menu.addMenuEntry(createBindingFixedItem());
            menu.addMenuEntry(createBindingMovableItem());
            menu.addMenuEntry(createBindingStaticItem());
            return menu;
        }

        private RadialMenuItem createForceConcentratedItem() {
            RadialMenuItem fConc = new RadialMenuItem("concentrated", getString(R.string.units_forces_concentrated));
            fConc.setDisplayIcon(R.drawable.force_conc_icon);
            fConc.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {
                    sceneController.addForce(new Force(getActivity(), ForceType.CONCENTRATED, touch.x, touch.y));
                    creationMenu.dismiss();
                    creationMenu.setActivated(false);
                }
            });
            return fConc;
        }

        private RadialMenuItem createForceDistributedItem() {
            RadialMenuItem fDist = new RadialMenuItem("distributed", getString(R.string.units_forces_distributed));
            fDist.setDisplayIcon(R.drawable.force_distributed_icon);
            fDist.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {
                    sceneController.addForce(new Force(getActivity(), ForceType.DISTRIBUTED, touch.x, touch.y));
                    creationMenu.dismiss();
                }
            });
            return fDist;
        }

        private RadialMenuItem createForceMomentItem() {
            RadialMenuItem fMom = new RadialMenuItem("moment", getString(R.string.units_forces_moment));
            fMom.setDisplayIcon(R.drawable.force_moment_icon);
            fMom.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
                @Override
                public void execute() {
                    sceneController.addForce(new Force(getActivity(), ForceType.MOMENT, touch.x, touch.y));
                    creationMenu.dismiss();
                }
            });
            return fMom;
        }

        private RadialMenuItem createForcesItem() {
            RadialMenuItem forces = new RadialMenuItem("forces", getString(R.string.units_forces));
            forces.setDisplayIcon(R.drawable.force_conc_icon);
            ArrayList<RadialMenuItem> forcesChildren = new ArrayList<>();
            forcesChildren.add(createForceConcentratedItem());
            forcesChildren.add(createForceDistributedItem());
            forcesChildren.add(createForceMomentItem());
            forces.setMenuChildren(forcesChildren);
            return forces;
        }

        private RadialMenuWidget buildForcesMenu() {
            RadialMenuWidget menu = initMenu();
            menu.addMenuEntry(createForceConcentratedItem());
            menu.addMenuEntry(createForceDistributedItem());
            menu.addMenuEntry(createForceMomentItem());
            return menu;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sceneController = new SceneController(getActivity());
        initializeGestures();
        frame = new RelativeLayout(this.getActivity());
        frame.setClipChildren(false);
        frame.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sceneController.getSurface().setOnTouchListener(this);
        frame.addView(sceneController.getSurface());

        initOverlayMenu();
        menuBuilder = new CreationMenuBuilder();
        frame.addView(overlayMenu);

        StatusManager.setStatus(getString(R.string.hint_open_menu));
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

    private void initializeGestures() {
        GestureAdapter gestureAdapter = new GestureAdapter();
        gestureDetector = new GestureDetectorCompat(this.getActivity(), gestureAdapter);
        gestureDetector.setIsLongpressEnabled(true);
        gestureAdapter.setOnTapListener(new GestureAdapter.TapListener() {
            @Override
            public void onDoubleTap(float touchX, float touchY) {
                if (RenderingSurfaceFragment.this.modifyAction != ModifyActions.NONE)
                    return; // ignore accidently gestures while editing objects
                int x = (int) touchX;
                int y = (int) touchY;
                if (x <= 70) x = 70;    // bound screen edges
                else if (x <= sceneController.getSurface().getWidth() - 70)
                    x -= 0.35 * x; // magic offset...
                else x -= 0.45 * x;

                y += 50;

                creationMenu.show(frame, x, y);
                creationMenu.setCenterLocation(x, y);
            }

            @Override
            public void onLongTap(float touchX, float touchY) {
                onDoubleTap(touchX, touchY); // support old way to add obejcts
            }

            @Override
            public void onTapStarted(float touchX, float touchY) {

            }
        });
        gestureHandler = new GestureHandler();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        if (modifyAction != ModifyActions.NONE)  // calculates only when something is gonna be modified
            gestureHandler.onTouchEvent(motionEvent);

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN: {
                hideOverlayMenu();
                this.touch = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                sceneController.select(touch.x, touch.y);

                if (modifyAction == ModifyActions.CREATING) {
                    sceneController.beginPlank(motionEvent.getX(), motionEvent.getY()); // move plank creation from menu for clarity (menu must only switches actions)
                    StatusManager.setStatus(this.getActivity().getString(R.string.hint_creation_edit_object));
                } else if (sceneController.isObjectSelected()) {
                    creationMenu = menuBuilder.getCreationMenu(CreationMenuType.FORCES);
                    PointF rotCenter = new PointF(sceneController.getSelected().getBoundingRect().centerX(),
                            sceneController.getSelected().getBoundingRect().centerY()); // rotate around whatever you want
                    gestureHandler.setRotationCenter(rotCenter);
                } else
                    creationMenu = menuBuilder.getCreationMenu(CreationMenuType.PLANK);
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
                    sceneController.editPlank(motionEvent.getX(), motionEvent.getY());
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
                } else if (sceneController.isObjectSelected()){
                    sceneController.applyTransformToSelected();
                    RectF r = sceneController.getSelected().getBoundingRect();
                    moveOverlayMenu((int) r.centerX(), (int) r.centerY());
                    showOverlayMenu();
                } else {
                    hideOverlayMenu();
                }
                StatusManager.setStatus(creationMenu.isActivated() ? getString(R.string.hint_choose_object) : getString(R.string.hint_open_menu));
                break;
            }
        }
        return true;
    }
}
