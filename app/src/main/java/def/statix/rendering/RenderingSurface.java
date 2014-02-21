package def.statix.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Lux on 15.02.14.
 * General SurfaceView for rendering all the scene graphics.
 * Operates in the separate from the UI thread.
 */

public class RenderingSurface extends SurfaceView implements Runnable{

    private Thread renderingThread;
    private SurfaceHolder surfaceHolder;
    private ArrayList<Renderable> renderableData;
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
            assert canvas != null;
            canvas.drawARGB(255, 250, 150, 20);
            //TODO: refactoring needed. too long chain of method calls.
            for (Renderable renderable : renderableData) {
                canvas.drawBitmap(renderable.getSprite().getImage(),
                                  renderable.getSprite().getTransform(), paint);
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
}
