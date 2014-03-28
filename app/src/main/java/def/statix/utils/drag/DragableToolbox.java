package def.statix.utils.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by AdYa on 17.03.14.
 */
public class DragableToolbox extends LinearLayout implements DragSource {

    private DragController dragController;

    public DragableToolbox(Context context) {
        super(context);
    }

    public DragableToolbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragableToolbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragController.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        dragController.onTouchEvent(ev);
        return true;
    }


    // DragSource implementation goes here...
    @Override
    public boolean allowDrag() {
        return true;
    }

    @Override
    public void setDragController(DragController dragger) {
        dragController = dragger;
    }

    public DragController getDragController() {
        return dragController;
    }

    @Override
    public void onDropCompleted(View target, boolean success) {

    }

}
