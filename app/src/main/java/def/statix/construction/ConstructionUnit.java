package def.statix.construction;

import android.graphics.Bitmap;
import android.graphics.PointF;

import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.rendering.Renderable;
import def.statix.rendering.Sprite;

/**
 * Created by Lux on 19.02.14.\
 * Basic entity, which represents an archetype for all physical objects on the scene.
 */

public class ConstructionUnit extends Renderable {

    private Force outgoingForce;
    private ConstructionUnitType type;

    public ConstructionUnit(Force outgoingForce, PointF location) {
        this.outgoingForce = outgoingForce;
        this.position = location; // TODO: position should be converted from location(screen coordinates).
    }

    public ConstructionUnit() {
        outgoingForce = new Force();
        // some default initialization will be here.
        // maybe applying the gravity or smth. like that.
    }

    public void loadImage(Bitmap image) {
        setSprite(new Sprite(image, position));
    }

    public ConstructionUnitType getType() {
        return type;
    }

    public void setType(ConstructionUnitType type) {
        this.type = type;
    }
}
