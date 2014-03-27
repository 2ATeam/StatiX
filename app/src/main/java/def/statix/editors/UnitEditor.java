package def.statix.editors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import def.statix.construction.ConstructionUnit;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.rendering.SceneController;

public abstract class UnitEditor implements Observer {

    /**
     * Inflated view for this editor.
     */
    protected View view;
    protected SceneController scene;

    public View getView() {
        return view;
    }

    /**
     * Unit which is under editing
     */
    protected ConstructionUnit unit;
    protected ConstructionUnitType unitType;

    /**
     * Resource layout which will be inflated for this editor.
     */
    private int layoutResource;

    /**
     * Creates Editor with specific layout and acceptable ConstructionUnitType.
     *
     * @param editorLayout    Layout which will be inflated.
     * @param defaultUnitType Default type for creating units. Also it sets up acceptable type's class.
     */
    protected UnitEditor(int editorLayout, ConstructionUnitType defaultUnitType) {
        this.layoutResource = editorLayout;
        this.unitType = defaultUnitType;
    }

    /**
     * Sets type of the unit which is gonna be created.
     */
    public void createUnit(ConstructionUnitType type) {
        if (canEdit(type)) {
            unitType = type;
        }
    }

    public void clearUnit() {
        if (this.unit != null) this.unit.deleteObserver(this); // remove observer from the prev unit
        this.unit = null;
        updateValues();
    }

    public void editUnit(ConstructionUnit unit) {
        if (!canEdit(unit)) return;
        if (this.unit != null) this.unit.deleteObserver(this); // remove observer from the prev unit
        this.unit = unit;
        this.unit.addObserver(this); // start listen to the new object
        updateValues();
    }

    public ConstructionUnit getUnit() {
        return this.unit;
    }

    /**
     * Checks whether the unit can be edited by this editor.
     */
    public boolean canEdit(ConstructionUnit unit) {
        return (unit != null && canEdit(unit.getType()));
    }

    public boolean canEdit(ConstructionUnitType type) {
        return this.unitType.getClass().isInstance(type);
    }

    /**
     * Called right after the view was successfully inflated.
     * Implement this to initialize any widgets of the editor.
     */
    protected abstract void initializeChildViews();

    /**
     * Called after all views was initialized
     * Implement and call this method to update all editor values
     */
    public abstract void updateValues();

    /**
     * Implement this method to save all changes to unit
     */
    public abstract void applyChanges();

    public View createView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(layoutResource, container, false);
        initializeChildViews();
        updateValues();
        return view;
    }

    public boolean equals(UnitEditor editor) {
        return this.getClass().isInstance(editor) && ((this.unit == null && editor.unit == null) || this.unit.equals(editor.unit));
    }

    @Override
    public void update(Observable observable, Object o) {
        updateValues();
    }


    public void setScene(SceneController scene) {
        this.scene = scene;
    }

    public SceneController getScene() {
        return scene;
    }
}
