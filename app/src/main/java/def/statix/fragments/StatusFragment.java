package def.statix.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import def.statix.ActivityMain;
import def.statix.R;
import def.statix.calculations.constructions.Construction;
import def.statix.calculations.constructions.StaticProblemSolver;
import def.statix.utils.ui.StatusManager;

public class StatusFragment extends Fragment {

    private Button bSolve;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.status_fragment, container, false);
        bSolve = (Button) v.findViewById(R.id.bSolve);
        bSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Construction c = ((ActivityMain) getActivity()).getSurfaceFragment().getScene().getConstruction();
                StaticProblemSolver solver = new StaticProblemSolver();

                if (c != null && c.canSolve()) {
                    float[] s = solver.solve(c);
                    StatusManager.setSuccess(getString(R.string.msg_solution) + ": R = " + String.format("%.2f", s[0]) + "; X = " + String.format("%.2f", s[1]) + "; Y = " + String.format("%.2f", s[2]));
                } else
                    StatusManager.setError(getActivity().getString(R.string.msg_construction_not_ready));
            }
        });
        StatusManager.addStatusView((TextView) v.findViewById(R.id.tvStatus));
        return v;
    }
}
