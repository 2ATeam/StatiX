package def.statix.utils.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by AdYa on 28.12.13.
 */
public class GestureAdapter implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final float GESTURE_SENSIVITY = 30;

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        notifyDoubleTap(motionEvent.getX(), motionEvent.getY());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        notifyTapStarted(motionEvent.getX(), motionEvent.getY());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        notifyLongTap(motionEvent.getX(), motionEvent.getY());
    }

    @Override
    public boolean onFling(MotionEvent downMotionEvent, MotionEvent moveMotionEvent, float velocityX, float velocityY) {

        float startX = downMotionEvent.getX();
        float startY = downMotionEvent.getY();
        float endX = moveMotionEvent.getX();
        float endY = moveMotionEvent.getY();


        if (Math.abs(velocityX) - Math.abs(velocityY) < 0) {
            if (Math.abs(startY - endY) < GESTURE_SENSIVITY) return true;
            if (endY > startY)
                notifyDownFling();
            else
                notifyUpFling();
        } else {
            if (Math.abs(startX - endX) < GESTURE_SENSIVITY) return true;
            if (endX > startX)
                notifyRightFling();
            else
                notifyLeftFling();
        }

        return true;
    }


/// Listeners go below...be aware...

    public static interface FlingListener {
        public void onUpFling();

        public void onDownFling();

        public void onLeftFling();

        public void onRightFling();
    }

    private ArrayList<FlingListener> flingListeners = new ArrayList<>();

    public void setOnFlingListener(FlingListener listener) {
        flingListeners.add(listener);
    }

    private void notifyUpFling() {
        for (FlingListener listener : flingListeners) {
            listener.onUpFling();
        }
    }

    private void notifyDownFling() {
        for (FlingListener listener : flingListeners) {
            listener.onDownFling();
        }
    }

    private void notifyLeftFling() {
        for (FlingListener listener : flingListeners) {
            listener.onLeftFling();
        }
    }

    private void notifyRightFling() {
        for (FlingListener listener : flingListeners) {
            listener.onRightFling();
        }
    }

    public static interface TapListener {
        public void onDoubleTap(float touchX, float touchY);

        public void onLongTap(float touchX, float touchY);

        public void onTapStarted(float touchX, float touchY);
    }

    private ArrayList<TapListener> tapListeners = new ArrayList<>();

    public void setOnTapListener(TapListener listener) {
        tapListeners.add(listener);
    }

    private void notifyLongTap(float x, float y) {
        for (TapListener listener : tapListeners) {
            listener.onLongTap(x, y);
        }
    }

    private void notifyDoubleTap(float x, float y) {
        for (TapListener listener : tapListeners) {
            listener.onDoubleTap(x, y);
        }
    }

    private void notifyTapStarted(float x, float y) {
        for (TapListener listener : tapListeners) {
            listener.onTapStarted(x, y);
        }
    }
}
