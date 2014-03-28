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
import def.statix.construction.ConstructionUnit;
import def.statix.construction.Force;
import def.statix.construction.Plank;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ForceType;
import def.statix.rendering.SceneController;
import def.statix.utils.ui.StatusManager;

public class StatusFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.status_fragment, container, false);
        Button bSolve = (Button) v.findViewById(R.id.bSolve);
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

        Button bTest = (Button) v.findViewById(R.id.bTest);
        bTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

        StatusManager.addStatusView((TextView) v.findViewById(R.id.tvStatus));
        return v;
    }

    private void test() {
        Construction c = new Construction();
        SceneController sceneController = ((ActivityMain) getActivity()).getSurfaceFragment().getScene();
        Plank p1 = new Plank(2, 90);
        Plank p2 = new Plank(3, 0);
        p1.setPosition(200, 200);
        p2.setPosition(100, 200);
        c.addUnit(p1);
        c.addUnit(p2);

        sceneController.addForce(200, 200, ForceType.CONCENTRATED);
        ConstructionUnit unit = (sceneController.getSelected());
        unit.setAngle(-60f);
        ((Force) unit).applyToPlank(p1);
        ((Force) unit).setValue(6);
        c.addUnit(unit);
        sceneController.addForce(200, 150, ForceType.DISTRIBUTED);
        unit = (sceneController.getSelected());
        unit.setAngle(90);
        ((Force) unit).applyToPlank(p1);
        ((Force) unit).setValue(2);
        c.addUnit(unit);

        sceneController.addForce(150, 200, ForceType.MOMENT);
        unit = (sceneController.getSelected());
        unit.setAngle(180);
        ((Force) unit).applyToPlank(p2);
        ((Force) unit).setValue(7);
        c.addUnit(unit);

        sceneController.addBinding(200, 100, BindingType.FIXED);
        unit = (sceneController.getSelected());
        unit.setAngle(0f);
        c.addUnit(unit);

        sceneController.addBinding(100, 200, BindingType.MOVABLE);
        unit = (sceneController.getSelected());
        unit.setAngle(-30f);
        c.addUnit(unit);

        StaticProblemSolver solver = new StaticProblemSolver();
        float[] s = solver.solve(c);
        StatusManager.setSuccess(getString(R.string.msg_solution) + ": R = " + String.format("%.2f", s[0]) + "; X = " + String.format("%.2f", s[1]) + "; Y = " + String.format("%.2f", s[2]));
    }

}
