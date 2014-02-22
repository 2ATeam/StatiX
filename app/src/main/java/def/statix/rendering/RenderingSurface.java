package def.statix.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
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
    private Paint paint;
    private boolean isOK;

    public RenderingSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG); // some bitmap smoothing here :D
    }

    @Override
    public void run() {
        while (isOK) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }
            Canvas canvas = surfaceHolder.lockCanvas();
            assert canvas != null && renderableData != null;
            canvas.drawARGB(255, 250, 150, 20);
            Iterator<Renderable> iterator = renderableData.iterator();
            Renderable item;
            while(iterator.hasNext()) {
                item = iterator.next();
                //TODO: refactoring needed. too long chain of method calls.
                canvas.drawBitmap(item.getSprite().getImage(),
                                  item.getSprite().getTransform(), paint);
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
