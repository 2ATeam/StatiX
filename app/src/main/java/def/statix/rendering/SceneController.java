package def.statix.rendering;

import android.content.Context;
import android.graphics.PointF;

import java.util.ArrayList;

import def.statix.calculations.constructions.Construction;
import def.statix.construction.ConstructionUnit;
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

public class SceneController {

    private Construction construction;
    private UnconfirmedPlank unconfirmedPlank; // cannot be renderable.
    private ConstructionUnit selectedObject;
    private RenderingSurface renderingSurface;

    private Foreman foreman;
    private PlankBuilder plankBuilder;
    private BindingBuilder bindingBuilder;
    private ForceBuilder forceBuilder;

    private Context context;


    public SceneController(Context context) {
        this();
        renderingSurface = new RenderingSurface(context);
        renderingSurface.setModel(construction.getRenderables());
        this.context = context;
    }

    public SceneController() {
        construction = new Construction();
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

        plankBuilder.setPlankParams(unconfirmedPlank, unitLocation, renderingSurface.getUpPaint());
        addUnit(plankBuilder, unitLocation.x, unitLocation.y, PlankType.PLANK);
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
        construction.addUnit(foreman.getUnit());
        selectedObject = foreman.getUnit(); // added object becomes selected.
    }

    public void rotateSelected(float angle) {
        if (angle > 180)
            angle -= 180;
        selectedObject.rotate(angle);
    }

    public void scaleSelected(float width, float height) {
        selectedObject.scale(width, height);
    }

    public void resizeSelectedPlank(float newLength) {
        if (selectedObject instanceof Plank) {
            Plank plank = (Plank) selectedObject;
            plank.setLength(newLength);
            newLength = getSurface().getGridRenderer().convertToScreen(newLength);
            float oxAngle = (float) Math.atan2(plank.getEnd().y - plank.getBegin().y,
                    plank.getEnd().x - plank.getBegin().x);
            PointF normal = new PointF((float) Math.cos(oxAngle), (float) Math.sin(oxAngle));
            normal.x *= newLength;
            normal.y *= newLength;
            normal.x += plank.getBegin().x;
            normal.y += plank.getBegin().y;
            beginPlank(plank.getBegin().x, plank.getBegin().y);
            editPlank(normal);
            construction.removeUnit(selectedObject);
            confirmPlank();
        }
    }

    private PointF checkStick(PointF sticker) {
        float stickThreshold = 20.0f;
        for (Plank plank : construction.getPlanks()) {
            if (plank == selectedObject) // avoid self-checking.
                continue;

            ArrayList<PointF> joints = plank.getOverlay().getJoints();
            for (PointF joint : joints) {
                if (sticker.x >= joint.x - stickThreshold && sticker.x <= joint.x + stickThreshold &&
                        sticker.y >= joint.y - stickThreshold && sticker.y <= joint.y + stickThreshold) {
                    return new PointF(joint.x - sticker.x, joint.y - sticker.y);
                }
            }
        }
        return null;
    }

    public void translateSelected(float x, float y) {
        PointF stickOffsetVector;
        if (!selectedObject.isAttached()) {
            selectedObject.translate(x, y);
            for (PointF joint : selectedObject.getOverlay().getJoints()) {
                stickOffsetVector = checkStick(joint);
                if (stickOffsetVector != null) { // stick occurred!
                    selectedObject.offset(stickOffsetVector.x, stickOffsetVector.y);
                    selectedObject.setAttached(true);
                }
            }
        } else {
            float stickThreshold = 100.0f; // in screen coords.
            float dx = Math.abs(selectedObject.getSpriteLocation().x - x);
            float dy = Math.abs(selectedObject.getSpriteLocation().y - y);
            if (dx >= stickThreshold || dy >= stickThreshold) {
                selectedObject.setAttached(false);
            }
        }
    }

    public void applyTransformToSelected() {
        selectedObject.update();
    }

    public void removeSelected() {
        if (selectedObject != null) {
            construction.removeUnit(selectedObject);
            selectedObject = null;
        }
    }

    public void select(int x, int y) {
        selectedObject = null; // deselect it.
        for (ConstructionUnit sceneObject : construction) {
            if (sceneObject.hitTest(x, y)) {
                selectedObject = sceneObject;
                break;
            }
        }
        renderingSurface.setSelectedObject(selectedObject);
    }

    public void select(ConstructionUnit unit) {
        selectedObject = unit;
    }

    public boolean isObjectSelected() {
        return selectedObject != null;
    }

    public RenderingSurface getSurface() {
        return renderingSurface;
    }

    public ConstructionUnit getSelected() {
        return selectedObject;
    }

    public UnconfirmedPlank getUnconfirmedPlank() {
        return unconfirmedPlank;
    }

    public Construction getConstruction() {
        return construction;
    }
}