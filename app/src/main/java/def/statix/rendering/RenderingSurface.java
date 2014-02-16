package def.statix.rendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Lux on 15.02.14.
 */
public class RenderingSurface extends SurfaceView implements Runnable{

    private Thread renderingThread;
    private SurfaceHolder surfaceHolder;
    private boolean isOK;
    //test
    private Bitmap balka;
    private float x, y;

    //test
    public void setBalka(Bitmap balka) {
        this.balka = balka;
        x = y = 0;
    }

    //test
    public void moveBalka(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public RenderingSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
    }

    @Override
    public void run() {
        while (isOK) {
            if (!surfaceHolder.getSurface().isValid()) {
                continue;
            }

            Canvas canvas = surfaceHolder.lockCanvas();
            assert canvas != null;
            //test
                canvas.drawARGB(255, 250, 150, 20);
                canvas.drawBitmap(balka, x - (balka.getWidth() /  2), y - (balka.getHeight() / 2), null);
            //
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
