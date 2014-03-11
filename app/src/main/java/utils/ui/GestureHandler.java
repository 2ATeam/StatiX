package utils.ui;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Analysing incoming touch events and calculates gesture measures
 * To calculate rotation you must set rotation center
 * <p/>
 * Created by AdYa on 11.03.14.
 */

public class GestureHandler {
    private PointF start = new PointF(); // start position
    private PointF disp = new PointF(); // displacement


    private boolean enabledRotation = true;
    private PointF rotCenter = new PointF();
    private float rotAngle = 0;

    public void setRotationEnabled(boolean isEnabled) {
        enabledRotation = isEnabled;
    }

    public void setRotationCenter(float x, float y) {
        rotCenter.set(x, y);
    }

    public void setRotationCenter(PointF center) {
        rotCenter.set(center.x, center.y);
    }


    private void getRotationEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                rotAngle = angleBetween(rotCenter, start, new PointF(event.getX(), event.getY()));
                break;
        }
    }

    private float angleBetween(PointF center, PointF current, PointF previous) {
        float ax = center.x - current.x,
                ay = center.y - current.y;
        float bx = center.x - previous.x,
                by = center.y - previous.y;

        float ab = ax * bx + ay * by;
        double aa = Math.sqrt(ax * ax + ay * ay);
        double bb = Math.sqrt(bx * bx + by * by);
        double angle = Math.acos(ab / (aa * bb));
        return (float) Math.toDegrees(angle);

    }

    /**
     * Analysing incoming MotionEvent and calculates gesture measures
     *
     * @param event
     */
    public void onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disp.set(0, 0);
                start.set(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                disp.set(event.getX() - start.x,
                        event.getY() - start.y);
                break;
        }
        if (enabledRotation) getRotationEvent(event);
    }

    public float getDX() {
        return disp.x;
    }

    public float getDY() {
        return disp.y;
    }

    public float getStartX() {
        return start.x;
    }

    public float getStartY() {
        return start.y;
    }

    public float getEndY() {
        return start.y + disp.y;
    }

    public float getEndX() {
        return start.x + disp.x;
    }

    public float getAngle() {
        return rotAngle;
    }
}
