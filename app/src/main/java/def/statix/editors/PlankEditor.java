package def.statix.editors;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;

import def.statix.R;
import def.statix.construction.ConstructionUnit;
import def.statix.construction.Plank;
import def.statix.construction.unittypes.PlankType;
import def.statix.utils.MathUtils;

public class PlankEditor extends UnitEditor {

    private static int SEEK_BAR_DECIMAL_ACCURACY = 10;

    private EditText etWidth;
    private EditText etAngle;
    private EditText etX;
    private EditText etY;

    private SeekBar sbWidth;
    private SeekBar sbAngle;

    //applyChanges(); /// TODO: PlankEditor: may be should apply values by clicking button

    public PlankEditor() {
        super(R.layout.plank_editor, PlankType.PLANK);
    }

    @Override
    public void editUnit(ConstructionUnit unit) {
        super.editUnit(unit);
        sbWidth.setMax((int) scene.getSurface().getGridRenderer().getGrid().getWidth() * SEEK_BAR_DECIMAL_ACCURACY);
    }

    @Override
    protected void initializeChildViews() {
        etWidth = (EditText) view.findViewById(R.id.editor_plank_etWidth);
        etAngle = (EditText) view.findViewById(R.id.editor_plank_etAngle);
       /* etX = (EditText) view.findViewById(R.id.editor_plank_etX);
        etY = (EditText) view.findViewById(R.id.editor_plank_etY);*/

        initEditText(etWidth);
        initEditText(etAngle);
       /* initEditText(etX);
        initEditText(etY);
*/
        sbWidth = (SeekBar) view.findViewById(R.id.editor_plank_sbWidth);
        sbAngle = (SeekBar) view.findViewById(R.id.editor_plank_sbAngle);


        sbAngle.setMax(180 * SEEK_BAR_DECIMAL_ACCURACY);

        linkSeekBarWithEdit(sbAngle, etAngle);
        linkSeekBarWithEdit(sbWidth, etWidth);
    }

    @Override
    public void updateValues() {
        if (unit != null) {///TODO: Temporary solution when creating an object
            Plank plank = (Plank) unit;
            etAngle.setText(String.valueOf(plank.getAngle()));
            etWidth.setText(String.valueOf(plank.getLength()));
           /* etX.setText(String.valueOf(plank.getPosition().x));
            etY.setText(String.valueOf(plank.getPosition().y));*/
        } else {
            etAngle.setText("0");
            etWidth.setText("0");
          /*  etX.setText("0");
            etY.setText("0");*/
        }
    }

    @Override
    public void applyChanges() {
        Plank plank = (Plank) unit;
        scene.select(plank);
        scene.resizeSelectedPlank(Float.parseFloat(etWidth.getText().toString()));
       // scene.rotateSelected(Float.parseFloat(etAngle.getText().toString()));
    }

    private void initEditText(final EditText text) {

        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    UnitEditorManager.getInstance().hideEditor(PlankEditor.this);
                    return true;
                }
                return false;
            }
        });
    }

    private void linkSeekBarWithEdit(SeekBar seekBar, EditText editText) {
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
                int value = (int) (Float.parseFloat(editable.toString()) * SEEK_BAR_DECIMAL_ACCURACY);
                int clampedValue = MathUtils.clamp(0, value, seek.getMax(), true);

                seek.setProgress(value);
                if (clampedValue != value) {
                    text.setText(String.valueOf(clampedValue / SEEK_BAR_DECIMAL_ACCURACY));
                }

            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                text.setText(String.valueOf(progress / SEEK_BAR_DECIMAL_ACCURACY));
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
