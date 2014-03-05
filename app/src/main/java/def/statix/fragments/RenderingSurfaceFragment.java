package def.statix.fragments;

import android.graphics.Color;
import android.graphics.Point;
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
import utils.ui.StatusManager;

/**
 * Created by Lux on 16.02.14.
 */
public class RenderingSurfaceFragment extends Fragment implements View.OnTouchListener {


    private GestureDetectorCompat gestureDetector;
    private RelativeLayout frame;
    private SceneController sceneController;
    private Point touch;

    private ArcMenu menu;
    private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep};

    private void initArcMenu() {
        menu = new ArcMenu(this.getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menu.setLayoutParams(params);
        menu.setDegreeses(180, 360);
        menu.setChildSize(30);
        final int itemCount = ITEM_DRAWABLES.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this.getActivity());
            item.setImageResource(ITEM_DRAWABLES[i]);
            item.setId(i + 1);

            menu.addItem(item);
        }
        menu.setMainButtonTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sceneController.removeSelected();
                    hideMenu();
                }
                return false;
            }
        });
        menu.expand(false);
        hideMenu();
    }

    private void moveMenu(int x, int y) {
        assert menu.getLayoutParams() != null;
        ((RelativeLayout.LayoutParams) menu.getLayoutParams()).setMargins(x - menu.getWidth() / 2, y - menu.getHeight() / 2, 0, 0);
        menu.requestLayout();
    }

    private void hideMenu() {
        //       menu.collapse(true);
        menu.setVisibility(View.INVISIBLE);

    }

    private void showMenu() {
        menu.setVisibility(View.VISIBLE);
        //    menu.expand(true);
    }


    private RadialMenuWidget menuAdd;

    private void initRadMenu() {
        menuAdd = new RadialMenuWidget(this.getActivity());

        menuAdd.setAnimationSpeed(0L);
        menuAdd.setIconSize(15, 30);
        menuAdd.setTextSize(13);
        menuAdd.setOutlineColor(Color.BLACK, 225);
        menuAdd.setInnerRingColor(0x0099CC, 180);
        menuAdd.setOuterRingColor(0x0099CC, 180);

        RadialMenuItem close = new RadialMenuItem("close", null);
        close.setDisplayIcon(R.drawable.composer_icn_plus_normal);
        close.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                menuAdd.dismiss();
            }
        });
        menuAdd.setCenterCircle(close);


        RadialMenuItem beam = new RadialMenuItem("beam", getString(R.string.units_beam));
        beam.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                sceneController.addBeam(touch.x, touch.y);
                menuAdd.dismiss();
            }
        });
        beam.setDisplayIcon(R.drawable.plank_icon);

        RadialMenuItem bindings = new RadialMenuItem("binding", getString(R.string.units_bindings));
        bindings.setDisplayIcon(R.drawable.binding_const_icon);

        ArrayList<RadialMenuItem> bindingsChildren = new ArrayList<>();
        RadialMenuItem bFixed = new RadialMenuItem("fixed", getString(R.string.units_bindings_fixed));
        RadialMenuItem bStatic = new RadialMenuItem("static", getString(R.string.units_bindings_static));
        RadialMenuItem bMovable = new RadialMenuItem("movable", getString(R.string.units_bindings_movable));

        bFixed.setDisplayIcon(R.drawable.binding_const_icon);
        bStatic.setDisplayIcon(R.drawable.binding_stationary_icon);
        bMovable.setDisplayIcon(R.drawable.binding_movalbe_icon);

        bFixed.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                sceneController.addBinding(touch.x, touch.y, BindingType.FIXED);
                menuAdd.dismiss();
            }
        });
        bStatic.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                sceneController.addBinding(touch.x, touch.y, BindingType.STATIC);
                menuAdd.dismiss();
            }
        });
        bMovable.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                sceneController.addBinding(touch.x, touch.y, BindingType.MOVABLE);
                menuAdd.dismiss();
            }
        });
        bindingsChildren.add(bFixed);
        bindingsChildren.add(bMovable);
        bindingsChildren.add(bStatic);
        bindings.setMenuChildren(bindingsChildren);
        RadialMenuItem forces = new RadialMenuItem("forces", getString(R.string.units_forces));
        forces.setDisplayIcon(R.drawable.force_conc_icon);

        ArrayList<RadialMenuItem> forcesChildren = new ArrayList<>();
        RadialMenuItem fConc = new RadialMenuItem("concentrated", getString(R.string.units_forces_concentrated));
        RadialMenuItem fDist = new RadialMenuItem("distributed", getString(R.string.units_forces_distributed));
        RadialMenuItem fMom = new RadialMenuItem("moment", getString(R.string.units_forces_moment));

        fConc.setDisplayIcon(R.drawable.force_conc_icon);
        fDist.setDisplayIcon(R.drawable.force_distributed_icon);
        fMom.setDisplayIcon(R.drawable.force_moment_icon);

        fConc.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                sceneController.addForce(new Force(getActivity(), ForceType.CONCENTRATED, touch.x, touch.y));
                menuAdd.dismiss();
                menuAdd.setActivated(false);
            }
        });
        fDist.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                sceneController.addForce(new Force(getActivity(), ForceType.DISTRIBUTED, touch.x, touch.y));
                menuAdd.dismiss();
            }
        });
        fMom.setOnMenuItemPressed(new RadialMenuItem.RadialMenuItemClickListener() {
            @Override
            public void execute() {
                sceneController.addForce(new Force(getActivity(), ForceType.MOMENT, touch.x, touch.y));
                menuAdd.dismiss();
            }
        });


        forcesChildren.add(fConc);
        forcesChildren.add(fDist);
        forcesChildren.add(fMom);
        forces.setMenuChildren(forcesChildren);

        menuAdd.addMenuEntry(beam);
        menuAdd.addMenuEntry(bindings);
        menuAdd.addMenuEntry(forces);


    }

    //TODO: remove on release.
    private void testStage(){
        sceneController.addBeam(500.0f, 500.0f);
        sceneController.addBinding(100.0f, 100.0f, BindingType.FIXED);
        sceneController.addBinding(100.0f, 200.0f, BindingType.STATIC);
        sceneController.addBinding(100.0f, 300.0f, BindingType.MOVABLE);
        sceneController.addForce(new Force(getActivity(), ForceType.MOMENT, 100.0f, 400.0f));
        sceneController.addForce(new Force(getActivity(), ForceType.DISTRIBUTED, 100.0f, 500.0f));
        sceneController.addForce(new Force(getActivity(), ForceType.CONCENTRATED, 100.0f, 600.0f));

        sceneController.addBeam(500.0f, 500.0f);
        sceneController.rotateSelected(45.0f);
        sceneController.applyTransformToSelected();
        sceneController.addBinding(100.0f, 100.0f, BindingType.FIXED);
        sceneController.rotateSelected(90.0f);
        sceneController.applyTransformToSelected();
        sceneController.addBinding(100.0f, 200.0f, BindingType.STATIC);
        sceneController.rotateSelected(90.0f);
        sceneController.applyTransformToSelected();
        sceneController.addBinding(100.0f, 300.0f, BindingType.MOVABLE);
        sceneController.rotateSelected(90.0f);
        sceneController.applyTransformToSelected();
        sceneController.addForce(new Force(getActivity(), ForceType.MOMENT, 100.0f, 400.0f));
        sceneController.rotateSelected(90.0f);
        sceneController.applyTransformToSelected();
        sceneController.addForce(new Force(getActivity(), ForceType.DISTRIBUTED, 100.0f, 500.0f));
        sceneController.rotateSelected(90.0f);
        sceneController.applyTransformToSelected();
        sceneController.addForce(new Force(getActivity(), ForceType.CONCENTRATED, 100.0f, 600.0f));
        sceneController.rotateSelected(90.0f);
        sceneController.applyTransformToSelected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sceneController = new SceneController(getActivity());
        initializeGestures();

        testStage();
        frame = new RelativeLayout(this.getActivity());
        frame.setClipChildren(false);
        frame.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sceneController.getSurface().setOnTouchListener(this);
        frame.addView(sceneController.getSurface());
        initArcMenu();
        initRadMenu();

        frame.addView(menu);

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
        gestureAdapter.setOnTapListener(new GestureAdapter.TapListener() {
            @Override
            public void onDoubleTap() {

            }

            @Override
            public void onLongTap() {
                int x = touch.x;
                int y = touch.y;
                if (x <= 110) x = 110;    // bound screen edges
                else
                    x -= 0.45 * x; // magic offset...

                y += 50;
                menuAdd.show(frame, x, y);
                menuAdd.setCenterLocation(x, y);
            }

            @Override
            public void onTapStarted() {

            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.setIsLongpressEnabled(true);
        gestureDetector.onTouchEvent(motionEvent);

        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                hideMenu();
                this.touch = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
                sceneController.select(touch.x, touch.y);
                if (sceneController.isObjectSelected())
                    StatusManager.setSuccess(getString(R.string.hint_selected_object));

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (sceneController.isObjectSelected()) {
                    sceneController.translateSelected(motionEvent.getX(), motionEvent.getY());
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (sceneController.isObjectSelected()){
                    sceneController.applyTransformToSelected();

                    RectF r = sceneController.getSelected().getBoundingRect();
                    moveMenu((int) r.centerX(), (int) r.centerY());
                    showMenu();
                } else {
                    StatusManager.setStatus(menuAdd.isActivated() ? getString(R.string.hint_choose_object) : getString(R.string.hint_open_menu));
                    hideMenu();
                }
                break;
            }
        }
        return true;
    }
}
