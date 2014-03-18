package def.statix.construction;

/**
 * Created by Lux on 19.02.14.
 */
public class Plank extends ConstructionUnit{
    private float length;

    public Plank(float length) {
        this.length = Math.round(length);
    }

    public float getLength() {
        return length;
    }
}
