package def.statix.construction;

/**
 * Created by Lux on 19.02.14.
 */
public class Plank extends ConstructionUnit {
    private float length;

    public Plank() {
        this(0);
    }

    public Plank(float lenght) {
        super();
        this.length = length;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length; /// TODO: planks length editing
        notifyObservers();
    }

    public float getAngle() {
        return 0; /// TODO: WHERE IS THE ANGLE???? T_T
    }

    public void setAngle(float angle) {
        /// TODO: planks angle editing
        notifyObservers();
    }
}
