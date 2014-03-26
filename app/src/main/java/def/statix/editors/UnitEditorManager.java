package def.statix.editors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;

import def.statix.construction.ConstructionUnit;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by AdYa on 18.03.14.
 */
public class UnitEditorManager {

    private static UnitEditorManager instance;

    public static UnitEditorManager getInstance() {
        if (instance == null) instance = new UnitEditorManager();
        return instance;
    }

    private ViewGroup editorContainer;
    private FragmentActivity parentActivity;
    private UnitEditor activeEditor;

    private UnitEditor[] editors;

    private void initEditors() {
        editors = new UnitEditor[]{new PlankEditor(),
                new BindingEditor(),
                new ForceEditor()};
        for (UnitEditor editor : editors) {
            editor.createView(parentActivity.getLayoutInflater(), editorContainer);
        }
    }

    /**
     * Checks whether the manager is ready to work.
     *
     * @NOTE: You should prepare it before use, otherwise you'll get an Exception.
     */
    public boolean isReady() {
        return !(parentActivity == null || editorContainer == null);
    }

    /**
     * Prepares manager to work
     */
    public void prepare(FragmentActivity fragmentActivity, int editorContainer) {
        parentActivity = fragmentActivity;
        this.editorContainer = (ViewGroup) ((parentActivity == null) ? null : parentActivity.findViewById(editorContainer));
        if (isReady()) {
            initEditors();
            parentActivity.getSupportFragmentManager().beginTransaction().add(editorContainer, new Fragment()).commit();
            this.editorContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Checks whether the manager is ready.
     * If it is not then the IllegalStateException will be thrown
     */
    private void checkState() {
        if (!isReady())
            throw new IllegalStateException("UnitEditorManager was not prepared to work");
    }

    public UnitEditor getActiveEditor() {
        checkState();
        return activeEditor;
    }

    public UnitEditor getEditorForUnit(ConstructionUnit unit) {
        checkState();
        for (UnitEditor editor : editors) {
            if (editor.canEdit(unit)) {
                editor.editUnit(unit);
                return editor;
            }
        }
        return null;
    }

    public UnitEditor getEditorForUnitType(ConstructionUnitType type) {
        checkState();
        for (UnitEditor editor : editors) {
            if (editor.canEdit(type)) {
                editor.clearUnit();
                editor.createUnit(type);
                return editor;
            }
        }
        return null;
    }

    private void replaceEditor(UnitEditor editor) {
        editorContainer.removeAllViews();
        editorContainer.addView(editor.getView());
    }

    public void hideActiveEditor() {
        hideEditor(activeEditor);
    }

    public void hideEditor(UnitEditor editor) {
        if (editor == null) return;
        checkState();
        if (activeEditor != null && activeEditor.equals(editor)) {
            editorContainer.setVisibility(View.GONE);
        }
    }

    public void showActiveEditor() {
        showEditor(activeEditor);
    }

    public void showEditor(UnitEditor editor) {
        if (editor == null) return;
        checkState();
        if (activeEditor == null || !activeEditor.equals(editor)) {
            replaceEditor(editor);
        }
        editorContainer.setVisibility(View.VISIBLE);
        activeEditor = editor;
    }
}
