package def.statix.rendering;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;

/**
 * Created by Lux on 19.02.14.
 */

public class Sprite {

    protected Bitmap image;
    protected Matrix matrix;
    protected PointF location; // screen coordinates.
    protected PointF pivotPoint;
    protected float angle;
    private float hitOffsetX;
    private float hitOffsetY;

    public Sprite(Bitmap image, float x, float y) {
        this.image = image;
        matrix = new Matrix();
        location = new PointF(x, y);
        pivotPoint = new PointF(x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        matrix.postTranslate(location.x, location.y);
        angle = 0;
    }

    public Sprite(float x, float y) {
        matrix = new Matrix();
        location = new PointF(x, y);
        pivotPoint = new PointF();
        matrix.postTranslate(location.x, location.y);
        angle = 0;
    }

    public void scale(float newWidth, float newHeight) {
        matrix.reset();
        matrix.postScale(newWidth / image.getWidth(), newHeight / image.getHeight());
        matrix.postTranslate(location.x, location.y);
    }

    public void rotate(float angle) {
        matrix.postRotate(angle, pivotPoint.x, pivotPoint.y);
        this.angle = angle;
    }

    public void translate(float x, float y) {
        location.set(x - hitOffsetX, y - hitOffsetY); // for better UX.
    }

    public void update() {
        image = Bitmap.createBitmap(image, 0, 0, getWidth(), getHeight(), matrix, true);
        //pivot point is translated to the center of the unit by default.
        pivotPoint.set(location.x + (image.getWidth() / 2), location.y + (image.getHeight() / 2));
    }

    public Matrix getTransform() {
        return this.matrix;
    }

    public void resetTransform(){
        matrix.reset();
    }

    public void setTransform(Matrix transform){
        this.matrix = transform;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight(){
        return image.getHeight();
    }

    public PointF getLocation(){
        return location;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }

    public PointF getPivotPoint() {
        return pivotPoint;
    }

    public void setPivotPoint(PointF pivotPoint) {
        this.pivotPoint = pivotPoint;
    }

    public float getAngle() {
        return angle;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setHitPoint(int x, int y) {
        hitOffsetX = x - location.x;
        hitOffsetY = y - location.y;
    }
}