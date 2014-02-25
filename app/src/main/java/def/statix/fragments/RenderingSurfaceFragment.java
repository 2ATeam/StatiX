package def.statix.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import def.statix.rendering.SceneController;

/**
 * Created by Lux on 16.02.14.
 */
public class RenderingSurfaceFragment extends Fragment implements View.OnTouchListener {

    private SceneController sceneController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sceneController = new SceneController(getActivity());
        //NOTE: test:
            sceneController.addBeam(100.0f, 190.0f);
            sceneController.addBeam(100.0f, 340.0f);
            sceneController.addBeam(100.0f, 490.0f);
            sceneController.addBeam(100.0f, 650.0f);
            sceneController.addBeam(100.0f, 800.0f);
        //====================================//
        sceneController.getSurface().setOnTouchListener(this);
        return sceneController.getSurface();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        sceneController.getSurface().pause();
        Log.d("DEBUG", "PAUSED");
    }

    @Override
    public void onResume() {
        super.onResume();
        sceneController.getSurface().resume();
        Log.d("DEBUG", "RESUMED");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                sceneController.select((int) motionEvent.getX(), (int) motionEvent.getY());
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (sceneController.isObjectSelected())
                    sceneController.translateSelected(motionEvent.getX(), motionEvent.getY());
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (sceneController.isObjectSelected())
                    sceneController.applyTransformToSelected();
                break;
            }
        }
        return true;
    }
}
