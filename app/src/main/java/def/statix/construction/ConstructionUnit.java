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
        this.position = new PointF();
    }

    public ConstructionUnit() {
        this.position = new PointF();
        // some default initialization will be here.
        // maybe applying the gravity or smth. like that.
    }

    public void setPosition(PointF location){
        this.position = location; // TODO: position should be converted from location(screen coordinates).
    }

    public void setPosition(float x, float y){
        this.position.set(x, y); // TODO: position should be converted from location(screen coordinates).
    }

    public void loadImage(Bitmap image) {
        overlayType = type == null; // if true - then we are dealing with the plank. Force or binding otherwise.
        setSprite(new Sprite(image, position.x - (image.getWidth() / 2),
                                    position.y - (image.getHeight() / 2)));
    }

    public ConstructionUnitType getType() {
        return type;
    }

    public void setType(ConstructionUnitType type) {
        this.type = type;
    }

    public void applyForce(Force force) {
        this.outgoingForce = force;
    }

    public Force getOutgoingForce() {
        return outgoingForce;
    }
}
