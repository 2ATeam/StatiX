package def.statix.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import def.statix.R;
import def.statix.rendering.Renderer;
import def.statix.rendering.RenderingSurface;

/**
 * Created by Lux on 16.02.14.
 */
public class RenderingSurfaceFragment extends Fragment implements View.OnTouchListener {
    //test
    private Bitmap balka;
    private Renderer renderer;
    private RenderingSurface surface;

    public RenderingSurfaceFragment() {
        renderer = new Renderer(getActivity());
        surface = renderer.getRenderingSurface();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //test demo
            balka = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            surface.setBalka(balka);
        //
        surface.setOnTouchListener(this);
        return surface;
    }

    @Override
    public void onPause() {
        super.onPause();
        surface.pause();
        Log.d("TEST", "PAUSED");
    }

    @Override
    public void onResume() {
        super.onResume();
        surface.resume();
        Log.d("TEST", "RESUMED");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            //test
            surface.moveBalka(motionEvent.getX(), motionEvent.getY());
        }
        return true;
    }

}
