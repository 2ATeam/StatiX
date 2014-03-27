package def.statix.construction;

import android.graphics.Bitmap;
import android.graphics.PointF;

import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.PlankType;
import def.statix.rendering.Renderable;
import def.statix.rendering.Sprite;

/**
 * Created by Lux on 19.02.14.\
 * Basic entity, which represents an archetype for all physical objects on the scene.
 */

public abstract class ConstructionUnit extends Renderable {

    protected static long _instance_counter = 0;
    protected long ID;

    private ConstructionUnitType type;
    protected float angle;

    public ConstructionUnit(Force outgoingForce, PointF location) {
        position = new PointF(location.x, location.y);
        isAttached = false;
        ID = ++_instance_counter;
    }

    public ConstructionUnit() {
        this(null, new PointF());
    }

    public void setPosition(PointF location) {
        assert location != null;
        setPosition(location.x, location.y);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y); // TODO: position should be converted from location(screen coordinates).
        notifyObservers();
    }

    public void setImage(Bitmap image) {
        /// TODO: Get rid of it....
        overlayType = type == PlankType.PLANK; // if true - then we are dealing with the plank. Force or binding otherwise.
        setSprite(new Sprite(image, position.x, position.y));
    }

    public ConstructionUnitType getType() {
        return type;
    }

    public void setType(ConstructionUnitType type) {
        this.type = type;
        notifyObservers();
    }

    @Override
    public boolean equals(Object o) {
        return (this.getClass().isInstance(o) && ((ConstructionUnit) o).ID == this.ID);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = (angle >= 180 ? angle - 180f : angle);
        notifyObservers();
    }
}
