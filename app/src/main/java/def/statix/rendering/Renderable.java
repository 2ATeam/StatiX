package def.statix.rendering;

import android.graphics.PointF;
import android.graphics.Rect;

/**
 * Created by Lux on 19.02.14.
 */
public class Renderable {

    protected PointF position;
    protected Sprite sprite;
    protected Rect boundingRect;

    public Renderable() {
        boundingRect = new Rect();
    }

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.calcBoundingRect();
    }

    public void rotate(float angle){
        this.sprite.rotate(angle);
    }

    public void scale(float width, float height) {
        this.sprite.scale(width, height);
    }

    public void calcBoundingRect(){
        int x = (int)sprite.getLocation().x;
        int y = (int)sprite.getLocation().y;
        boundingRect = new Rect(x, y, x + sprite.getWidth(), y + sprite.getHeight());
    }

    public void updateSprite(){
        this.sprite.update();
        this.calcBoundingRect();
    }

    public Rect getBoundingRect() {
        return boundingRect;
    }

    protected PointF getLocation() {
        return sprite.getLocation();
    }

    protected Sprite getSprite() {
        return sprite;
    }

    protected PointF getPosition() {
        return position;
    }

    protected void setPosition(PointF position) {
        this.position = position;
    }

    protected void setPosition(float x, float y) {
        this.position.set(x, y);
        this.getSprite().translate(x, y);
    }

    public boolean hitTest(int x, int y) {
        if (boundingRect.contains(x, y)) {
            return true;
        }
        return false;
    }
}
