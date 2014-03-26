package def.statix.editors;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import def.statix.R;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by AdYa on 17.03.14.
 */
public class BindingEditor extends UnitEditor {

    Spinner spinner;


    public BindingEditor() {
        super(R.layout.binding_editor, BindingType.FIXED);
    }

    @Override
    protected void initializeChildViews() {
        prepareSpinner();
    }

    @Override
    public void updateValues() {
        ConstructionUnitType type = (unit == null ? unitType : unit.getType());
        spinner.setEnabled(unit != null);
        spinner.setSelection(((BindingType) type).ordinal());
    }

    @Override
    public void applyChanges() {
        /// TODO: BindingEditor: possible changes of binding type
    }

    private void prepareSpinner() {
        spinner = (Spinner) view.findViewById(R.id.editor_binding_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.binding_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (unit == null) return; ///TODO: Temporary solution when creating an object
                switch (pos) {
                    default: // default fixed
                    case 0:
                        unit.setType(BindingType.FIXED);
                        break;
                    case 1:
                        unit.setType(BindingType.MOVABLE);
                        break;
                    case 2:
                        unit.setType(BindingType.STATIC);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
