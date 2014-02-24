package def.statix.rendering;

import android.graphics.PointF;

/**
 * Created by Lux on 19.02.14.
 */
public class Renderable {
    protected PointF position;
    protected Sprite sprite;

    protected PointF getPosition() {
        return position;
    }

    protected void setPosition(PointF position) {
        this.position = position;
    }

    protected Sprite getSprite() {
        return sprite;
    }

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    protected PointF getLocation() {
        return sprite.getLocation();
    }

    public void rotate(float angle){
        this.sprite.rotate(angle);
    }
}
