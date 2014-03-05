package def.statix.rendering;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lux on 02.03.14.
 */
public final class UnitOverlay extends Sprite{

    private static final int JOINT_DIAMETER = 20;
    private RectF boundingRect;
    private ArrayList<PointF> joints;
    private Paint overlayPaint;
    private int width;
    private int height;
    private int sourceWidth;
    private int sourceHeight;
    private PointF srcLocation;
    private boolean isPlankOverlayType;

    public UnitOverlay(PointF sourceLocation, int sourceWidth, int sourceHeight, boolean overlayType) {
        super(sourceLocation.x - JOINT_DIAMETER, sourceLocation.y - JOINT_DIAMETER);
        this.srcLocation = sourceLocation;
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
        isPlankOverlayType = overlayType;
        joints = new ArrayList<>();
        overlayPaint = new Paint();
        overlayPaint.setARGB(200, 0, 0, 200);
        width = sourceWidth + (2 * JOINT_DIAMETER); // some space for joints.
        height = sourceHeight + (2 * JOINT_DIAMETER);
        updateBoundingRect();
        createPlankOverlay();
    }

    private void createPlankOverlay() {
        this.image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        float jointRadius = JOINT_DIAMETER / 2;
        if (isPlankOverlayType){
            PointF joint1 = new PointF(jointRadius, height / 2);
            PointF joint2 = new PointF(width - jointRadius, height / 2);
            joints.add(joint1);
            joints.add(joint2);
            canvas.drawCircle(joint1.x, joint1.y, jointRadius, overlayPaint);
            canvas.drawCircle(joint2.x, joint2.y, jointRadius, overlayPaint);
        }
        else {
            PointF joint = new PointF(width / 2, jointRadius);
            joints.add(joint);
            canvas.drawCircle(joint.x, joint.y, jointRadius, overlayPaint);
        }
        canvas.drawRect(JOINT_DIAMETER, JOINT_DIAMETER, width - JOINT_DIAMETER, height - JOINT_DIAMETER, overlayPaint);
    }

    @Override
    public void update() {
        super.update();
        updateBoundingRect();
    }

    public void updateBoundingRect() {
        // same dimensions as source.
        boundingRect = new RectF(srcLocation.x, srcLocation.y,
                srcLocation.x + sourceWidth,
                srcLocation.y + sourceHeight);
        Log.d("DEBUG", boundingRect.toString());
        Log.d("DEBUG", "height: " + sourceHeight + " width: " + sourceWidth);
        Log.d("DEBUG", "posX: " + srcLocation.x + " posY: " + srcLocation.y);
    }

    public Paint getPaint() {
        return overlayPaint;
    }

    public RectF getBoundingRect() {
        return boundingRect;
    }

    public void setPlankOverlayType(boolean isPlankOverlay) {
        this.isPlankOverlayType = isPlankOverlay;
    }

    public void setSrcPosAndLoc(PointF sourceLocation, int sourceWidth, int sourceHeight){
        this.srcLocation = sourceLocation;
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
    }
}
