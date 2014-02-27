package def.statix.rendering;

import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by Lux on 19.02.14.
 */
public class Renderable {

    protected PointF position;
    protected Sprite sprite;
    protected UnitOverlay overlay;

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.overlay = new UnitOverlay(sprite.getLocation(), sprite.getWidth(), sprite.getHeight());
        this.overlay.update();
    }

    public void rotate(float angle) {
        this.sprite.rotate(angle);
    }

    public void scale(float width, float height) {
        this.sprite.scale(width, height);
    }

    public void updateSprite() {
        this.sprite.update();
        this.overlay.update();
    }

    public PointF getLocation() {
        return sprite.getLocation();
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
        this.getSprite().translate(x, y);
    }

    public Rect getBoundingRect() {
        return overlay.getBoundingRect();
    }

    public boolean hitTest(int x, int y) {
        if (overlay.getBoundingRect().contains(x, y)) {
            return true;
        }
        return false;
    }
}
