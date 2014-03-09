package def.statix.construction;

/**
 * Created by Lux on 19.02.14.
 */
public class Plank extends ConstructionUnit{
    private float length;

    public Plank(float lenght) {
        this.length = lenght;
    }

    public float getLength() {
        return length;
    }
}
