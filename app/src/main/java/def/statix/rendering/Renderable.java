package def.statix.rendering;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Lux on 19.02.14.
 */
public class Renderable {

    protected PointF position;
    protected Sprite sprite;
    protected UnitOverlay overlay;
    protected boolean isResizeable;
    protected boolean isRotatable;
    protected boolean overlayType;

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
        overlay = new UnitOverlay(sprite.getLocation(), sprite.getWidth(), sprite.getHeight(), overlayType);
        overlay.setTransform(sprite.getTransform());
    }

    public void rotate(float angle) {
        this.sprite.rotate(angle);
    }

    public void scale(float width, float height) {
        this.sprite.scale(width, height);
    }

    public void update() {
        sprite.update();
        overlay.setSrcPosAndLoc(sprite.getLocation(), sprite.getWidth(), sprite.getHeight());
        overlay.update();
        sprite.resetTransform();
    }

    public boolean hitTest(int x, int y) {
        if (overlay.getBoundingRect().contains(x, y)) {
            return true;
        }
        return false;
    }

    public PointF getSpriteLocation() {
        return sprite.getLocation();
    }

    public PointF getOverlayLocation(){
        return overlay.getLocation();
    }

    protected Sprite getSprite() {
        return sprite;
    }

    public PointF getPosition() {
        return position;
    }

    protected void setPosition(PointF position) {
        this.position = position;
    }

    protected void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public RectF getBoundingRect() {
        return overlay.getBoundingRect();
    }

    public UnitOverlay getOverlay() {
        return overlay;
    }

    public boolean isResizeable() {
        return isResizeable;
    }

    public void setResizeable(boolean isScaleable) {
        this.isResizeable = isScaleable;
    }

    public boolean isRotatable() {
        return isRotatable;
    }

    public void setRotatable(boolean isRotateable) {
        this.isRotatable = isRotateable;
    }
}
