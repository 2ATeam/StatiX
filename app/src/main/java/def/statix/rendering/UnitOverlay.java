package def.statix.rendering;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Lux on 02.03.14.
 */
public final class UnitOverlay extends Sprite{

    private static final int JOINT_DIAMETER = 20;
    private RectF boundingRect;
    private ArrayList<PointF> joints;
    private Paint overlayPaint;
    private Paint jointsPaint;
    private int width;
    private int height;
    private int sourceWidth;
    private int sourceHeight;
    private PointF srcLocation;

    public UnitOverlay(PointF sourceLocation, int sourceWidth, int sourceHeight, boolean overlayType) {
        super(sourceLocation.x - JOINT_DIAMETER, sourceLocation.y - JOINT_DIAMETER);
        this.srcLocation = sourceLocation;
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
        joints = new ArrayList<>();
        overlayPaint = new Paint();
        overlayPaint.setARGB(200, 0, 0, 200);
        jointsPaint = new Paint();
        jointsPaint.setARGB(255, 255, 0, 0);
        width = sourceWidth + (2 * JOINT_DIAMETER); // some space for joints.
        height = sourceHeight + (2 * JOINT_DIAMETER);
        updateBoundingRect();
    }

    public void createPlankOverlay() {
        this.image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        float jointRadius = JOINT_DIAMETER / 2;

        PointF firstJoint = new PointF(joints.get(0).x, joints.get(0).y);
        PointF secondJoint = new PointF(joints.get(1).x, joints.get(1).y);
        firstJoint.offset(-location.x + JOINT_DIAMETER, -location.y + JOINT_DIAMETER);
        secondJoint.offset(-location.x + JOINT_DIAMETER, -location.y + JOINT_DIAMETER);

        canvas.drawRect(JOINT_DIAMETER, JOINT_DIAMETER, width - JOINT_DIAMETER,
                                                        height - JOINT_DIAMETER, overlayPaint);
        canvas.drawCircle(firstJoint.x, firstJoint.y, jointRadius, jointsPaint);
        canvas.drawCircle(secondJoint.x, secondJoint.y, jointRadius, jointsPaint);
    }

    /// TODO: refactor this. This is not according the D.R.Y. principle.
    public void createForceOrBindingOverlay(){
        this.image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        float jointRadius = JOINT_DIAMETER / 2;
        PointF joint = new PointF(width / 2, jointRadius);
        joints.add(joint);
        canvas.drawCircle(joint.x, joint.y, jointRadius, jointsPaint);
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
    }

    public Paint getPaint() {
        return overlayPaint;
    }

    public RectF getBoundingRect() {
        return boundingRect;
    }

    public void setSrcPosAndSize(PointF sourceLocation, int sourceWidth, int sourceHeight){
        this.srcLocation = sourceLocation;
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
}

    public void addJoint(PointF joint) {
        joints.add(joint);
    }

    public ArrayList<PointF> getJoints() {
        return joints;
    }
}