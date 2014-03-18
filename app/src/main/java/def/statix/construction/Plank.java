package def.statix.construction;

/**
 * Created by Lux on 19.02.14.
 */
public class Plank extends ConstructionUnit{
    private float length;

    public Plank() {
        this(0);
    }
    public Plank(float lenght) {
        super();
        this.length = lenght;
    }

    public float getLength() {
        return length;
    }

    public float getAngle() {
        return 0; /// TODO: WHERE IS THE ANGLE???? T_T
    }
}
