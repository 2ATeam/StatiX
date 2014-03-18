package def.statix.utils.drag;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by AdYa on 17.03.14.
 */
public class DragableToolbox extends LinearLayout implements DragSource, DropTarget {

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
        dragController.addDropTarget(this);
    }

    public DragController getDragController() {
        return dragController;
    }

    @Override
    public void onDropCompleted(View target, boolean success) {

    }

    // DropTarget implementation goes here...
    @Override
    public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    @Override
    public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    @Override
    public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    @Override
    public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    @Override
    public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        Toast.makeText(getContext(), "View can't be droped here, so revert dragging", 5).show();
        return false;
    }

    @Override
    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
        return null;
    }

}
