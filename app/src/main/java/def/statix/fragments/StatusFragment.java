package def.statix.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import def.statix.R;
import def.statix.utils.ui.StatusManager;

/**
 * Created by AdYa on 24.02.14.
 */
public class StatusFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.status_fragment, container, false);
        StatusManager.addStatusView((TextView) v.findViewById(R.id.tvStatus));
        return v;
    }
}
