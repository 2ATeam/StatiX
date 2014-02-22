package def.statix.rendering;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import def.statix.construction.unitbuilding.BeamBuilder;
import def.statix.construction.unitbuilding.BindingBuilder;
import def.statix.construction.unitbuilding.Foreman;
import def.statix.construction.unittypes.BindingType;

/**
 * Created by Lux on 20.02.14.
 */
public class SceneController {

    private CopyOnWriteArrayList<Renderable> sceneObjects; // data model.
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
    }

    public void addBeam(float x, float y) {
        foreman.setBuilder(beamBuilder);
        foreman.constructUnit(context, x, y, null);
        sceneObjects.add(foreman.getUnit());
    }

    public void addBinding(float x, float y, BindingType type) {
        foreman.setBuilder(bindingBuilder);
        foreman.constructUnit(context, x, y, type);
        sceneObjects.add(foreman.getUnit());
    }

    public void initRenderingSurface(Context context) {
        this.context = context;
        this.renderingSurface = new RenderingSurface(context);
        this.renderingSurface.setModel(sceneObjects);
    }

    @Deprecated
    public void addRenderableObject(Renderable object) {
        sceneObjects.add(object);
    }

    public RenderingSurface getSurface(){
        return renderingSurface;
    }
}