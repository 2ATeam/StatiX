package def.statix.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Lux on 15.02.14.
 * General SurfaceView for rendering all the scene graphics.
 * Operates in the separate from the UI thread.
 */

public class RenderingSurface extends SurfaceView implements Runnable{

    private Thread renderingThread;
    private SurfaceHolder surfaceHolder;
    private CopyOnWriteArrayList<Renderable> renderableData; // a reference to the data to render.
    private UnconfirmedPlank unconfirmedPlank;
    private Renderable selectedObject; // a reference from scene controller, to determine the overlay target.
    private Paint unitPaint;
    private Paint ubPaint;
    private Paint stickPaint;
    private boolean isOK; // thread is running now.
    private static final boolean IS_DEBUG_INFO_VISIBLE = true;

    public RenderingSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
        unitPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG); // some bitmap smoothing here
        ubPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ubPaint.setColor(Color.BLUE);
        ubPaint.setStyle(Paint.Style.STROKE);
        ubPaint.setStrokeJoin(Paint.Join.ROUND);
        ubPaint.setStrokeWidth(10.0f);
        stickPaint = new Paint();
        stickPaint.setARGB(255, 0, 255, 0);
    }

    @Override
    public void run() {
        while (isOK) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = surfaceHolder.lockCanvas();
            assert canvas != null && renderableData != null;
            canvas.drawARGB(255, 90, 90, 90);
            Iterator<Renderable> iterator = renderableData.iterator();
            Renderable item;

            if (unconfirmedPlank != null) { // user is drawing a plank now...
                PointF start = unconfirmedPlank.getBegin();
                PointF stop = unconfirmedPlank.getEnd();
                canvas.drawLine(start.x, start.y, stop.x, stop.y, ubPaint);
            }

            while(iterator.hasNext()) {
                item = iterator.next();
                if (item == selectedObject){
                    UnitOverlay overlay = item.getOverlay();
                    if (IS_DEBUG_INFO_VISIBLE){
                        for (int i = 0; i < overlay.getJointStickMarkers().size(); i++){
                            boolean sticked = overlay.getJointStickMarkers().get(i);
                            if (sticked) {
                                canvas.drawCircle(overlay.getJoints().get(i).x, overlay.getJoints().get(i).y, 20.0f, stickPaint);
                            }
                        }
                    }
                    canvas.drawBitmap(overlay.getImage(), overlay.getLocation().x, overlay.getLocation().y, overlay.getPaint());
                }
                canvas.drawBitmap(item.getSprite().getImage(), item.getSpriteLocation().x, item.getSpriteLocation().y, unitPaint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause(){
        isOK = false;
        while (true) {
            try {
                renderingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        renderingThread = null;
    }

    public void resume(){
        isOK = true;
        renderingThread = new Thread(this);
        renderingThread.start();
    }

    public void setModel(CopyOnWriteArrayList<Renderable> model){
        this.renderableData = model;
    }

    public void setSelectedObject(Renderable selectedObject) {
        this.selectedObject = selectedObject;
    }

    public void setUnconfirmedPlank(UnconfirmedPlank unconfirmedPlank) {
        this.unconfirmedPlank = unconfirmedPlank;
    }

    public Paint getUbPaint() {
        return ubPaint;
    }

    public int getCanvasWidth() {
        Canvas canvas = surfaceHolder.lockCanvas();
        assert canvas != null;
        int width = canvas.getWidth();
        surfaceHolder.unlockCanvasAndPost(canvas);
        return width;
    }
}