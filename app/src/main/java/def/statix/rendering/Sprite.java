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
    private PointF location; // screen coordinates
    private PointF centredLocation;
    private float angle;

    public Sprite(Bitmap image, PointF location) {
        this.image = image;
        this.matrix = new Matrix();
        this.matrix.postTranslate(location.x, location.y);
        this.location = location;
        this.angle = 0.0f;
    }

    public Sprite(Bitmap image, float x, float y) {
        this.image = image;
        this.matrix = new Matrix();
        this.location = new PointF(x, y);
        this.centredLocation = new PointF(x + (image.getWidth() / 2), y + (image.getHeight() / 2));
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
        matrix.postRotate(angle, centredLocation.x, centredLocation.y);
        this.angle = angle;
        Log.d("SPRITE", "rotated at " + angle);
    }

    public void translate(PointF newLocation) {
        matrix.postTranslate(newLocation.x, newLocation.y);
        location = newLocation;
        centredLocation.set(newLocation.x + (image.getWidth() / 2),
                            newLocation.y + (image.getHeight() / 2));
        Log.d("SPRITE", "translated at " + newLocation.toString());
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

    public PointF getCentredLocation() {
        return centredLocation;
    }

    public float getRotation() {
        return angle;
    }

    public void update() {
        image = Bitmap.createBitmap(image, 0, 0, getWidth(), getHeight(), matrix, true);
        Log.d("SPRITE", "transform applied");
    }

    public Bitmap getImage() {
        return image;
    }
}
