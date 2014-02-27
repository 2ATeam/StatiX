package def.statix.rendering;

import android.content.Context;

import java.util.concurrent.CopyOnWriteArrayList;

import def.statix.construction.unitbuilding.BeamBuilder;
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
    private Renderable selectedObject;

    private RenderingSurface renderingSurface;
    private Foreman foreman;
    private BeamBuilder beamBuilder;

    private BindingBuilder bindingBuilder;
    private Context context;

    public SceneController(Context context) {
        this(); // constructor delegation. your cap.
        this.renderingSurface = new RenderingSurface(context);
        this.renderingSurface.setModel(sceneObjects);
        this.context = context;
    }

    public SceneController() {
        this.sceneObjects = new CopyOnWriteArrayList<>();
        this.foreman = new Foreman();
        this.beamBuilder = new BeamBuilder();
        this.bindingBuilder = new BindingBuilder();
        this.selectedObject = null;
    }

    public void addBeam(float x, float y) {
        addUnit(beamBuilder, x, y, null);
    }

    public void addBinding(float x, float y, BindingType type) {
        addUnit(bindingBuilder, x, y, type);
    }
    
    private void addUnit(ConstructionUnitBuilder builder, float x, float y, ConstructionUnitType type){
        foreman.setBuilder(builder);
        foreman.constructUnit(context, x, y, type);
        sceneObjects.add(foreman.getUnit());
        selectedObject = foreman.getUnit(); // added object becomes selected.
    }

    public void rotateSelected(float angle){
        selectedObject.rotate(angle);
    }

    public void scaleSelected(float width, float height) {
        selectedObject.scale(20.0f, 20.0f);
    }

    public void translateSelected(float x, float y){
        selectedObject.getSprite().translate(x, y);
    }

    public void applyTransformToSelected(){
        selectedObject.updateSprite();
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

    public boolean isObjectSelected(){
        return selectedObject != null;
    }

    public RenderingSurface getSurface(){
        return renderingSurface;
    }
}