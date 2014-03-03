package def.statix.fragments;

import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import def.statix.R;
import def.statix.rendering.SceneController;
import utils.capricom.ArcMenu;
import utils.ui.StatusManager;

/**
 * Created by Lux on 16.02.14.
 */
public class RenderingSurfaceFragment extends Fragment implements View.OnTouchListener {

    private SceneController sceneController;

    protected ArcMenu menu;
    private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with};

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

            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                }
            });
        }
        hideMenu();
    }

    private void moveMenu(int x, int y) {
        assert menu.getLayoutParams() != null;
        ((FrameLayout.LayoutParams) menu.getLayoutParams()).setMargins(x - menu.getWidth() / 2, y - menu.getHeight() / 2, 0, 0);
        menu.requestLayout();
    }

    private void hideMenu() {
        menu.collapse();
        menu.setVisibility(View.INVISIBLE);

    }

    private void showMenu() {
        menu.setVisibility(View.VISIBLE);
        menu.expand();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sceneController = new SceneController(getActivity());
        //NOTE: test:
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
            sceneController.addBeam(500.0f, 500.0f);
        //====================================//
        FrameLayout frame = new FrameLayout(this.getActivity());
        frame.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sceneController.getSurface().setOnTouchListener(this);
        frame.addView(sceneController.getSurface());
        initArcMenu();
        frame.addView(menu);
        return frame;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                hideMenu();
                sceneController.select((int) motionEvent.getX(), (int) motionEvent.getY());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (sceneController.isObjectSelected())
                    sceneController.translateSelected(motionEvent.getX(), motionEvent.getY());
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (sceneController.isObjectSelected()){
                    sceneController.applyTransformToSelected();
                    StatusManager.setSuccess("Object has been selected");
                    showMenu();
                    RectF r = sceneController.getSelected().getBoundingRect();
                    moveMenu((int)r.centerX(), (int)r.centerY());
                } else {
                    StatusManager.setError("Object has been deselected");
                    hideMenu();
                }
                break;
            }
        }
        return true;
    }
}
