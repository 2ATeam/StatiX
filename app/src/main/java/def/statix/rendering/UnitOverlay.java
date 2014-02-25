package def.statix.rendering;

import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Lux on 25.02.14.
 */
public class UnitOverlay {
    private Rect boundingRect;
    private ArrayList<PointF> joints;
    private PointF location;
    private int width;
    private int height;

    public UnitOverlay() {
        joints = new ArrayList<>();
    }

    public UnitOverlay(float x, float y, int width, int height) {
        this();
        location = new PointF(x, y);
        this.width = width;
        this.height = height;
        update();
    }

    public UnitOverlay(PointF pos, int width, int height) {
        this();
        this.location = pos;
        this.width = width;
        this.height = height;
        update();
    }

    public void update() {
        int x = (int) location.x;
        int y = (int) location.y;
        boundingRect = new Rect(x, y, x + width, y + height);
    }

    public Rect getBoundingRect() {
        return boundingRect;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
