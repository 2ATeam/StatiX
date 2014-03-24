package def.statix.rendering;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Observable;

/**
 * Created by Lux on 19.02.14.
 */
public class Renderable extends Observable {

    protected PointF position;
    protected Sprite sprite;
    protected UnitOverlay overlay;

    protected boolean isResizeable;
    protected boolean isRotatable;
    protected boolean isTranslatable;
    protected boolean isSelectable;

    protected boolean isAttached;
    protected boolean overlayType;

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
        overlay = new UnitOverlay(sprite.getLocation(), sprite.getWidth(), sprite.getHeight(), overlayType);
    }

    public void rotate(float angle) {
        sprite.rotate(angle);
        overlay.rotate(angle);
    }

    public void scale(float width, float height) {
        sprite.scale(width, height);
    }

    public void update() {
        sprite.update();
        overlay.setSrcPosAndSize(sprite.getLocation(), sprite.getWidth(), sprite.getHeight());
        overlay.update();
        sprite.resetTransform();
        overlay.resetTransform();
    }

    public boolean hitTest(int x, int y) {
        if (overlay.getBoundingRect().contains(x, y)) {
            sprite.setHitPoint(x, y);
            overlay.setHitPoint(x, y);
            return true;
        }
        return false;
    }

    public void translate(float x, float y) {
        sprite.translate(x, y);
        overlay.translate(x, y);
    }

    public PointF getSpriteLocation() {
        return sprite.getLocation();
    }

    public PointF getOverlayLocation() {
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

    public void setResizeable(boolean isScalable) {
        this.isResizeable = isScalable;
    }

    public boolean isRotatable() {
        return isRotatable;
    }

    public void setRotatable(boolean isRotatable) {
        this.isRotatable = isRotatable;
    }

    public boolean isTranslatable() {
        return isTranslatable;
    }

    public void setTranslatable(boolean isTranslatable) {
        this.isTranslatable = isTranslatable;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }

    public boolean isAttached() {
        return isAttached;
    }

    public void setAttached(boolean isAttached) {
        this.isAttached = isAttached;
    }
}