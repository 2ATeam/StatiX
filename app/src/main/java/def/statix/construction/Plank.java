package def.statix.construction;

import android.graphics.PointF;

import def.statix.rendering.UnconfirmedPlank;

/**
 * Created by Lux on 19.02.14.
 */
public class Plank extends ConstructionUnit{

    private float length;
    private PointF begin;
    private PointF end;

    public Plank() {
        this(0);
    }

    public Plank(float length) {
        super();
        begin = new PointF();
        end = new PointF();
        this.length = length;
    }

    public Plank(UnconfirmedPlank plank) {
        super();
        this.begin = plank.getBegin();
        this.end = plank.getEnd();
        this.length = plank.getLength();
    }

    public PointF getBegin() {
        return begin;
    }

    public PointF getEnd() {
        return end;
    }

    public float getLength() {
        return length;
    }

    public float getAngle() {
        return 0; /// TODO: WHERE IS THE ANGLE???? T_T
    }
}
