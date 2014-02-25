package def.statix.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import def.statix.R;
import def.statix.rendering.SceneController;

/**
 * Created by AdYa on 24.02.14.
 */
public class ToolboxFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toolbox, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("DEBUG", "PAUSED");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DEBUG", "RESUMED");
    }
}
