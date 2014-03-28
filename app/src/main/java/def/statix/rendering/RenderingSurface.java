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

import def.statix.R;
import def.statix.grid.Grid;
import def.statix.grid.GridRenderer;

/**
 * Created by Lux on 15.02.14.
 * General SurfaceView for rendering all the scene graphics.
 * Operates in the separate from the UI thread.
 */

public class RenderingSurface extends SurfaceView implements Runnable {

    private Thread renderingThread;
    private SurfaceHolder surfaceHolder;
    private CopyOnWriteArrayList<Renderable> renderableData; // a reference to the data to render.
    private UnconfirmedPlank unconfirmedPlank;
    private Renderable selectedObject; // a reference from scene controller, to determine the overlay target.
    private Paint unitPaint;
    private Paint rectPaint;
    private Paint upPaint;
    private boolean isOK; // thread is running now.
    private static final boolean IS_DEBUG_INFO_VISIBLE = false;
    private GridRenderer gridRenderer;
    private Grid grid;

    public RenderingSurface(Context context) {
        super(context);
        surfaceHolder = getHolder();
        unitPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG | Paint.ANTI_ALIAS_FLAG); // some bitmap smoothing here
        upPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        upPaint.setColor(Color.BLUE);
        upPaint.setStyle(Paint.Style.STROKE);
        upPaint.setStrokeJoin(Paint.Join.ROUND);
        upPaint.setStrokeWidth(10.0f);
        rectPaint = new Paint();
        rectPaint.setARGB(200, 0, 200, 0);

        /// TODO: Dirty screen sizes...
        float width = getContext().getResources().getDimension(R.dimen.grid_screen_width);
        float height = getContext().getResources().getDimension(R.dimen.grid_screen_height);
        this.grid = new Grid(width / 10, height / 10);
        this.grid.setShowSectionLines(true);
        gridRenderer = new GridRenderer(grid, width, height);
        ///TODO: setup screen size and grid size to fit rendering surface...
    }

    public GridRenderer getGridRenderer() {
        return gridRenderer;
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

            gridRenderer.drawGrid(canvas);
            if (unconfirmedPlank != null) { // user is drawing a beam now...
                PointF start = unconfirmedPlank.getBegin();
                PointF stop = unconfirmedPlank.getEnd();
                canvas.drawLine(start.x, start.y, stop.x, stop.y, upPaint);
            }

            while (iterator.hasNext()) {
                item = iterator.next();
                if (item == selectedObject) {
                    UnitOverlay overlay = item.getOverlay();
                    if (IS_DEBUG_INFO_VISIBLE) {
                        for (int i = 0; i < overlay.getJointStickMarkers().size(); i++) {
                           canvas.drawRect(item.getBoundingRect(), rectPaint);
                        }
                    }
                    canvas.drawBitmap(overlay.getImage(), overlay.getLocation().x, overlay.getLocation().y, overlay.getPaint());
                }
                canvas.drawBitmap(item.getSprite().getImage(), item.getSpriteLocation().x, item.getSpriteLocation().y, unitPaint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        isOK = false;
        //noinspection LoopStatementThatDoesntLoop
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

    public void resume() {
        isOK = true;
        renderingThread = new Thread(this);
        renderingThread.start();
    }


    public void setModel(CopyOnWriteArrayList<Renderable> model) {
        this.renderableData = model;
    }

    public void setSelectedObject(Renderable selectedObject) {
        this.selectedObject = selectedObject;
    }

    public void setUnconfirmedPlank(UnconfirmedPlank unconfirmedPlank) {
        this.unconfirmedPlank = unconfirmedPlank;
    }

    public Paint getUpPaint() {
        return upPaint;
    }

    public int getCanvasWidth() {
        Canvas canvas = surfaceHolder.lockCanvas();
        assert canvas != null;
        int width = canvas.getWidth();
        surfaceHolder.unlockCanvasAndPost(canvas);
        return width;
    }
}