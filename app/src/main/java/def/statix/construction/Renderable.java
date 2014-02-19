package def.statix.construction;

import android.graphics.Point;

import def.statix.rendering.Sprite;

/**
 * Created by Lux on 19.02.14.
 */
public class Renderable {
    protected Point position;
    protected Sprite sprite;

    protected Point getPosition() {
        return position;
    }

    protected void setPosition(Point position) {
        this.position = position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
