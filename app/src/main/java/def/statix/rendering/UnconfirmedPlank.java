package def.statix.rendering;

import android.graphics.PointF;

/**
 * Created by Lux on 08.03.14.
 */
public class UnconfirmedPlank {

    private PointF begin;
    private PointF end;

    public UnconfirmedPlank(PointF begin) {
        this.begin = begin;
        this.end = new PointF(begin.x, begin.y);
    }

    public UnconfirmedPlank(PointF begin, PointF end) {
        this.begin = begin;
        this.end = end;
    }

    //heavy math here :D
    public double getLneght(){
        return Math.sqrt((end.x - begin.x) * (end.x - begin.x) + (end.y - begin.y) * (end.y - begin.y));
    }

    public PointF getBegin() {
        return begin;
    }

    public PointF getEnd() {
        return end;
    }

    public void setEnd(PointF end) {
        this.end = end;
    }

    public void setEnd(float x, float y) {
        this.end.set(x, y);
    }
}