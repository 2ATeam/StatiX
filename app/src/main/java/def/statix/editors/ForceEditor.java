package def.statix.editors;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import def.statix.R;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;

/**
 * Created by AdYa on 18.03.14.
 */
public class ForceEditor extends UnitEditor {

    Spinner spinner;

    public ForceEditor() {
        super(R.layout.force_editor, ForceType.CONCENTRATED);
    }

    @Override
    protected void initializeChildViews() {
        prepareSpinner();
    }

    @Override
    public void updateValues() {
        ConstructionUnitType type = (unit == null ? unitType : unit.getType());
        spinner.setEnabled(unit != null);
        spinner.setSelection(((ForceType) type).ordinal());
    }

    @Override
    public void applyChanges() {
        /// TODO: ForceEditor: possible changes of force type
    }

    private void prepareSpinner() {
        spinner = (Spinner) view.findViewById(R.id.editor_force_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.forces_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (unit == null) return; ///TODO: Temporary solution when creating an object
                switch (pos) {
                    default: // default concentrated
                    case 0:
                        unit.setType(ForceType.CONCENTRATED);
                        break;
                    case 1:
                        unit.setType(ForceType.DISTRIBUTED);
                        break;
                    case 2:
                        unit.setType(ForceType.MOMENT);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
