package def.statix.rendering;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

import def.statix.construction.Force;
import def.statix.construction.unitbuilding.PlankBuilder;
import def.statix.construction.unitbuilding.BindingBuilder;
import def.statix.construction.unitbuilding.ConstructionUnitBuilder;
import def.statix.construction.unitbuilding.Foreman;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by Lux on 20.02.14.
 */
public class SceneController {

    private CopyOnWriteArrayList<Renderable> sceneObjects; // data model.
    private UnconfirmedBeam unconfirmedPlank; // cannot be renderable.
    private Renderable selectedObject;

    private RenderingSurface renderingSurface;
    private Foreman foreman;
    private PlankBuilder beamBuilder;

    private BindingBuilder bindingBuilder;
    private Context context;

    public SceneController(Context context) {
        this();
        this.renderingSurface = new RenderingSurface(context);
        this.renderingSurface.setModel(sceneObjects);
        this.context = context;
    }

    public SceneController() {
        this.sceneObjects = new CopyOnWriteArrayList<>();
        this.foreman = new Foreman();
        this.beamBuilder = new PlankBuilder();
        this.bindingBuilder = new BindingBuilder();
        this.selectedObject = null;
    }

    public void beginPlank(float x, float y) {
        unconfirmedPlank = new UnconfirmedBeam(new PointF(x, y));
        renderingSurface.setUnconfirmedBeam(unconfirmedPlank);
    }

    public void editPlank(float x, float y) {
        unconfirmedPlank.setEnd(x, y);
    }

    public void confirmBeam(float x, float y) {
        addUnit(beamBuilder, x, y, null);
        unconfirmedPlank = null;
    }

    public void confirmBeam() {
        //addUnit(beamBuilder, x, y, null);
        /// TODO: implement beam building logic.
        unconfirmedPlank = null; // no need to render unconfirmed plank any more.
    }

    public void addBinding(float x, float y, BindingType type) {
        addUnit(bindingBuilder, x, y, type);
    }

    private void addUnit(ConstructionUnitBuilder builder, float x, float y, ConstructionUnitType type) {
        foreman.setBuilder(builder);
        foreman.constructUnit(context, x, y, type);
        sceneObjects.add(foreman.getUnit());
        selectedObject = foreman.getUnit(); // added object becomes selected.
    }

    public void addForce(Force force) {
        sceneObjects.add(force);
        selectedObject = force; // added force become selected.
    }

    public void rotateSelected(float angle) {
        selectedObject.rotate(angle);
    }

    public void scaleSelected(float width, float height) {
        selectedObject.scale(width, height);
    }

    public void translateSelected(float x, float y) {
        selectedObject.getSprite().translate(x, y);
        selectedObject.getOverlay().translate(x, y);
    }

    public void applyTransformToSelected() {
        selectedObject.update();
    }

    public void removeSelected(){
        if (selectedObject != null) {
            sceneObjects.remove(selectedObject);
            selectedObject = null;
        }
    }

    public void select(int x, int y) {
        selectedObject = null; // deselect it.
        for (Renderable sceneObject : sceneObjects) {
            if (sceneObject.hitTest(x, y)) {
                selectedObject = sceneObject;
                break;
            }
        }
        renderingSurface.setSelectedObject(selectedObject);
    }

    public boolean isObjectSelected() {
        return selectedObject != null;
    }

    public RenderingSurface getSurface() {
        return renderingSurface;
    }

    public Renderable getSelected() {
        return selectedObject;
    }

    public UnconfirmedBeam getUnconfirmedPlank() {
        return unconfirmedPlank;
    }
}