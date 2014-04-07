package def.statix.editors;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import def.statix.R;
import def.statix.construction.Force;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;
import def.statix.utils.MathUtils;

/**
 * Created by AdYa on 18.03.14.
 */
public class ForceEditor extends UnitEditor {

    private static int SEEK_BAR_DECIMAL_ACCURACY = 10;

    Spinner spinner;
    private EditText etAngle;
    private SeekBar sbAngle;
    private EditText etValue;
    private SeekBar sbValue;

    public ForceEditor() {
        super(R.layout.force_editor, ForceType.CONCENTRATED);
    }

    @Override
    protected void initializeChildViews() {
        prepareSpinner();
        etValue = (EditText) view.findViewById(R.id.editor_plank_etValue);
        etAngle = (EditText) view.findViewById(R.id.editor_plank_etAngle);
        sbAngle = (SeekBar) view.findViewById(R.id.editor_plank_sbAngle);
        sbValue = (SeekBar) view.findViewById(R.id.editor_plank_sbValue);

        sbAngle.setMax(360 * SEEK_BAR_DECIMAL_ACCURACY);
        sbValue.setMax(10000);


        initEditText(etValue);
        initEditText(etAngle);
        linkSeekBarWithEdit(sbAngle, etAngle, -180, 180);
        linkSeekBarWithEdit(sbValue, etValue, 0, 10000);
    }

    @Override
    public void updateValues() {
        ConstructionUnitType type = (unit == null ? unitType : unit.getType());
        spinner.setEnabled(false);
        spinner.setSelection(((ForceType) type).ordinal());
        if (unit != null) {///TODO: Temporary solution when creating an object
            Force force = (Force) unit;
            etAngle.setText(String.valueOf(force.getAngle()));
            etValue.setText(String.valueOf(force.getValue()));
        } else {
            etAngle.setText("0");
            etValue.setText("0");
        }
    }

    @Override
    public void applyChanges() {
        Force force = (Force) unit;
        force.setValue(Float.parseFloat(etValue.getText().toString()));
        force.setAngle(Float.parseFloat(etAngle.getText().toString()));
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

    private void initEditText(final EditText text) {

        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    applyChanges();
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    UnitEditorManager.getInstance().hideEditor(ForceEditor.this);
                    return true;
                }
                return false;
            }
        });
    }

    private void linkSeekBarWithEdit(SeekBar seekBar, EditText editText, final float min, final float max) {
        final SeekBar seek = seekBar;
        final EditText text = editText;

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) return;
                int value = (int) ((Float.parseFloat(editable.toString())));
                int clampedValue = MathUtils.clamp((int) min, value, (int) max, true);
                int scaled = value + (int) (Math.abs(min));
                seek.setProgress(scaled * SEEK_BAR_DECIMAL_ACCURACY);
                if (clampedValue != value) {
                    text.setText(String.valueOf(clampedValue));
                }

            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                text.setText(String.valueOf((progress / SEEK_BAR_DECIMAL_ACCURACY) - (Math.abs(min))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
