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
    private Paint rectPaint;
    private Paint ubPaint;
    private boolean isOK; // thread is running now.
    private boolean isDebugInfoVisible;

    public RenderingSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
        unitPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG); // some bitmap smoothing here
        rectPaint = new Paint();
        rectPaint.setARGB(75, 0, 255, 0);
        ubPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ubPaint.setColor(Color.BLUE);
        ubPaint.setStyle(Paint.Style.STROKE);
        ubPaint.setStrokeJoin(Paint.Join.ROUND);
        ubPaint.setStrokeWidth(10.0f);
        isDebugInfoVisible = true;
    }

    @Override
    public void run() {
        while (isOK) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = surfaceHolder.lockCanvas();
            assert canvas != null && renderableData != null;
            canvas.drawARGB(255, 40, 40, 40);
            Iterator<Renderable> iterator = renderableData.iterator();
            Renderable item;

            if (unconfirmedPlank != null) { // user is drawing a beam now...
                PointF start = unconfirmedPlank.getBegin();
                PointF stop = unconfirmedPlank.getEnd();
                canvas.drawLine(start.x, start.y, stop.x, stop.y, ubPaint);
            }

            while(iterator.hasNext()) {
                item = iterator.next();
                if (item == selectedObject){
                    UnitOverlay overlay = item.getOverlay();
                    canvas.drawBitmap(overlay.getImage(), overlay.getLocation().x, overlay.getLocation().y, overlay.getPaint());
                    if (isDebugInfoVisible){
                          canvas.drawRect(overlay.getBoundingRect(), rectPaint); // bounding rect
                    }
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

    public void setDebugInfoVisible(boolean isDebugInfoVisible) {
        this.isDebugInfoVisible = isDebugInfoVisible;
    }

    public void setUnconfirmedPlank(UnconfirmedPlank unconfirmedPlank) {
        this.unconfirmedPlank = unconfirmedPlank;
    }

    public Paint getUnitPaint() {
        return unitPaint;
    }

    public Paint getRectPaint() {
        return rectPaint;
    }

    public Paint getUbPaint() {
        return ubPaint;
    }
}
