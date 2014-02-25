package def.statix.rendering;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Lux on 19.02.14.
 */

public class Sprite {

    private Bitmap image;
    private Matrix matrix;
    private PointF location; // screen coordinates.
    private PointF pivotPoint; // pivot point.
    private float angle;

    public Sprite(Bitmap image, float x, float y) {
        this.image = image;
        this.matrix = new Matrix();
        this.location = new PointF(x, y);
        this.pivotPoint = new PointF(x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        this.matrix.postTranslate(location.x, location.y);
        this.angle = 0;
    }

    public void scale(float newWidth, float newHeight) {
        matrix.reset();
        matrix.postScale(newWidth / image.getWidth(), newHeight / image.getHeight());
        matrix.postTranslate(location.x, location.y);
        Log.d("SPRITE", "scaled at " + newWidth + "x" + newHeight);
    }

    public void rotate(float angle) {
        matrix.postRotate(angle, pivotPoint.x, pivotPoint.y);
        this.angle = angle;
        Log.d("SPRITE", "rotated at " + angle);
    }

    public void translate(float x, float y) {
        location.set(x - (image.getWidth() / 2), y - (image.getHeight() / 2)); // for better UX.
    }

    public Matrix getTransform() {
        return this.matrix;
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

    public PointF getPivotPoint() {
        return pivotPoint;
    }

    public float getAngle() {
        return angle;
    }

    public void update() {
        image = Bitmap.createBitmap(image, 0, 0, getWidth(), getHeight(), matrix, true);
        //pivot point is translated to the center of the unit by default.
        pivotPoint.set(location.x + (image.getWidth() / 2), location.y + (image.getHeight() / 2));
    }

    public Bitmap getImage() {
        return image;
    }
}
