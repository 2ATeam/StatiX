package def.statix.fragments;

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

    public RenderingSurfaceFragment() {
        sceneController = new SceneController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sceneController.getSurface().setOnTouchListener(this);
        return sceneController.getSurface();
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
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
           motionEvent.getAction() == MotionEvent.ACTION_MOVE){
           // perform scene controller calls.
        }
        return true;
    }
}
