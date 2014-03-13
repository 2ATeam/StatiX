package def.statix.rendering;

import android.content.Context;
import android.graphics.PointF;

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
    private UnconfirmedPlank unconfirmedPlank; // cannot be renderable.
    private Renderable selectedObject;
    private RenderingSurface renderingSurface;
    private Foreman foreman;
    private PlankBuilder plankBuilder;
    private BindingBuilder bindingBuilder;
    private Context context;
    private PointF unitLocation;

    public SceneController(Context context) {
        this();
        this.renderingSurface = new RenderingSurface(context);
        this.renderingSurface.setModel(sceneObjects);
        this.context = context;
    }

    public SceneController() {
        sceneObjects = new CopyOnWriteArrayList<>();
        foreman = new Foreman();
        plankBuilder = new PlankBuilder();
        bindingBuilder = new BindingBuilder();
        selectedObject = null;
    }

    //start plank adding process:
    public void beginPlank(float x, float y) {
        unconfirmedPlank = new UnconfirmedPlank(new PointF(x, y));
        renderingSurface.setUnconfirmedPlank(unconfirmedPlank);
    }

    //continue plank adding process:
    public void editPlank(float dx, float dy) {
        float offsetThreshold = 10.0f;
        float newDx = dx >= offsetThreshold || dx <= -offsetThreshold ? dx : 0;
        float newDy = dy >= offsetThreshold || dy <= -offsetThreshold ? dy : 0;
        unconfirmedPlank.offset(newDx, newDy);
    }

    //end plank adding process. unconfirmed plank becomes renderable object.
    public void confirmPlank() {
        //calc top left corner offset of the plank:
        float x = unconfirmedPlank.getBegin().x < unconfirmedPlank.getEnd().x ?
                  unconfirmedPlank.getBegin().x : unconfirmedPlank.getEnd().x;

        float y = unconfirmedPlank.getBegin().y < unconfirmedPlank.getEnd().y ?
                  unconfirmedPlank.getBegin().y : unconfirmedPlank.getEnd().y;

        unitLocation = new PointF(x, y);

        plankBuilder.setPlankParams(unconfirmedPlank, unitLocation, renderingSurface.getUbPaint());
        addUnit(plankBuilder, unitLocation.x, unitLocation.y, null);
        unconfirmedPlank = null; // no need to render unconfirmed plank any more.
        renderingSurface.setUnconfirmedPlank(null);
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

    public UnconfirmedPlank getUnconfirmedPlank() {
        return unconfirmedPlank;
    }
}