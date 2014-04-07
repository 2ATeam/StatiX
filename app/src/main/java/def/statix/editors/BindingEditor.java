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
import def.statix.construction.Binding;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.utils.MathUtils;

/**
 * Created by AdYa on 17.03.14.
 */
public class BindingEditor extends UnitEditor {

    Spinner spinner;
    private EditText etAngle;
    private SeekBar sbAngle;


    public BindingEditor() {
        super(R.layout.binding_editor, BindingType.FIXED);
    }

    private static int SEEK_BAR_DECIMAL_ACCURACY = 10;
    @Override
    protected void initializeChildViews() {
        prepareSpinner();
        etAngle = (EditText) view.findViewById(R.id.editor_plank_etAngle);
        sbAngle = (SeekBar) view.findViewById(R.id.editor_plank_sbAngle);


        sbAngle.setMax(360 * SEEK_BAR_DECIMAL_ACCURACY);
        initEditText(etAngle);
        linkSeekBarWithEdit(sbAngle, etAngle, -180, 180);
    }

    @Override
    public void updateValues() {
        ConstructionUnitType type = (unit == null ? unitType : unit.getType());
        spinner.setEnabled(false);
        spinner.setSelection(((BindingType) type).ordinal());
        if (unit != null) {///TODO: Temporary solution when creating an object
            Binding binding = (Binding) unit;
            etAngle.setText(String.valueOf(binding.getAngle()));
        } else {
            etAngle.setText("0");
        }
    }

    @Override
    public void applyChanges() {
        Binding binding = (Binding) unit;
        binding.setAngle(Float.parseFloat(etAngle.getText().toString()));
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
                    UnitEditorManager.getInstance().hideEditor(BindingEditor.this);
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
