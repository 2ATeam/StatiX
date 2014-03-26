package def.statix.rendering;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import def.statix.construction.Plank;
import def.statix.construction.unitbuilding.BindingBuilder;
import def.statix.construction.unitbuilding.ConstructionUnitBuilder;
import def.statix.construction.unitbuilding.ForceBuilder;
import def.statix.construction.unitbuilding.Foreman;
import def.statix.construction.unitbuilding.PlankBuilder;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;
import def.statix.construction.unittypes.PlankType;

/**
 * Created by Lux on 20.02.14.
 */
public class SceneController {

    private CopyOnWriteArrayList<Renderable> sceneObjects; // data model.
    private ArrayList<Plank> planks; // references from sceneObjects list.
    private UnconfirmedPlank unconfirmedPlank; // cannot be renderable.
    private Renderable selectedObject;
    private RenderingSurface renderingSurface;

    private Foreman foreman;
    private PlankBuilder plankBuilder;
    private BindingBuilder bindingBuilder;
    private ForceBuilder forceBuilder;

    private Context context;


    public SceneController(Context context) {
        this();
        renderingSurface = new RenderingSurface(context);
        renderingSurface.setModel(sceneObjects);
        this.context = context;
    }

    public SceneController() {
        sceneObjects = new CopyOnWriteArrayList<>();
        planks = new ArrayList<>();
        foreman = new Foreman();
        plankBuilder = new PlankBuilder();
        bindingBuilder = new BindingBuilder();
        forceBuilder = new ForceBuilder();
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

    public void editPlank(PointF newEnd) {
        unconfirmedPlank.setEnd(newEnd);
    }

    //end plank adding process. unconfirmed plank becomes renderable object.
    public void confirmPlank() {
        //calc top left corner offset of the plank:
        float x = unconfirmedPlank.getBegin().x < unconfirmedPlank.getEnd().x ?
                unconfirmedPlank.getBegin().x : unconfirmedPlank.getEnd().x;

        float y = unconfirmedPlank.getBegin().y < unconfirmedPlank.getEnd().y ?
                unconfirmedPlank.getBegin().y : unconfirmedPlank.getEnd().y;

        PointF unitLocation = new PointF(x, y);

        plankBuilder.setPlankParams(unconfirmedPlank, unitLocation, renderingSurface.getUbPaint());
        addUnit(plankBuilder, unitLocation.x, unitLocation.y, PlankType.PLANK);
        planks.add((Plank) foreman.getUnit()); //reference to the recently added plank.
        unconfirmedPlank = null; // no need to render unconfirmed plank any more.
        renderingSurface.setUnconfirmedPlank(null);
    }

    public void addBinding(float x, float y, BindingType type) {
        addUnit(bindingBuilder, x, y, type);
    }

    public void addForce(float x, float y, ForceType type) {
        addUnit(forceBuilder, x, y, type);
    }

    private void addUnit(ConstructionUnitBuilder builder, float x, float y, ConstructionUnitType type) {
        foreman.setBuilder(builder);
        foreman.constructUnit(context, x, y, type);
        sceneObjects.add(foreman.getUnit());
        selectedObject = foreman.getUnit(); // added object becomes selected.
    }

    public void rotateSelected(float angle) {
        selectedObject.rotate(angle);
    }

    public void scaleSelected(float width, float height) {
        selectedObject.scale(width, height);
    }

    public void resizeSelectedPlank(float newLength) {
        if (selectedObject instanceof Plank) {
            Plank plank = (Plank) selectedObject;
            float oxAngle = (float) Math.atan2(plank.getEnd().y - plank.getBegin().y,
                                               plank.getEnd().x - plank.getBegin().x);
            PointF normal = new PointF((float)Math.cos(oxAngle), (float)Math.sin(oxAngle));
            normal.x *= newLength;
            normal.y *= newLength;
            normal.x += plank.getBegin().x;
            normal.y += plank.getBegin().y;
            beginPlank(plank.getBegin().x, plank.getBegin().y);
            editPlank(normal);
            sceneObjects.remove(selectedObject);
            confirmPlank();
        }
    }


    private PointF checkStick(PointF sticker){
        float x1, y1, x2, y2;
        for (Plank plank : planks) {
            if (plank == selectedObject) // avoid self-checking.
                continue;
            ArrayList<PointF> joints = plank.getOverlay().getJoints();
            x1 = joints.get(0).x;
            y1 = joints.get(0).y;
            y2 = joints.get(1).y;
            x2 = joints.get(1).x;

            if (x1 == x2)
                x1 += 10.0f;
            if (y1 == y2)
                y1 += 10.0f;

            float intersectFactor = Math.abs(((sticker.y - y1) / (y2 - y1)) - ((sticker.x - x1) / (x2 - x1)));
            Log.d("DEBUG", "intersect factor: " + intersectFactor);
            // factor should be less or equal to threshold.
            if (intersectFactor <= 0.2f) {
                float A, B, C, X, Y;
                A = y1 - y2;
                B = x2 - x1;
                C = x1 * y2 - x2 * y1;
                Y = (-A * sticker.x - C) / B;
                X = (-B * sticker.y - C) / A;
                return new PointF(X, Y);
            }
        }
        return null;
    }

    public void translateSelected(float x, float y) {
        PointF stick, joint;
        if (!selectedObject.isAttached()) {
            selectedObject.translate(x, y);
            for (int i = 0; i < selectedObject.getOverlay().getJoints().size(); i++) {
                joint = selectedObject.getOverlay().getJoints().get(i);
                stick = checkStick(joint);
                if (stick != null) {
                    selectedObject.getOverlay().setJointSticked(i, true);
                    selectedObject.offset(stick.x - joint.x, stick.y - joint.y);
                    selectedObject.setAttached(true);
                }
            }
        }
        else {
            float dx = Math.abs(selectedObject.getSpriteLocation().x - x);
            float dy = Math.abs(selectedObject.getSpriteLocation().y - y);
            if (dx >= 150.0f || dy >= 150.0f) {
                selectedObject.setAttached(false);
            }
        }
    }

    public void applyTransformToSelected() {
        selectedObject.update();
    }

    public void removeSelected() {
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