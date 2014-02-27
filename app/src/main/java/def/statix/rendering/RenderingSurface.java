package def.statix.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
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
    private Paint unitPaint;
    private Paint boundingBoxPaint;
    private boolean isOK;

    public RenderingSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
        // some bitmap smoothing here:
        unitPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG);
        boundingBoxPaint = new Paint();
        boundingBoxPaint.setARGB(50, 0, 0, 200);
    }

    public RenderingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
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
            while(iterator.hasNext()) {
                item = iterator.next();
                canvas.drawRect(item.getBoundingRect(), boundingBoxPaint);
                canvas.drawBitmap(item.getSprite().getImage(), item.getLocation().x,
                                                               item.getLocation().y, unitPaint);
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
}
