package def.statix.construction;

/**
 * Created by Lux on 19.02.14.
 */
public class Beam extends ConstructionUnit{
    private float length;

    public Beam(float lenght) {
        this.length = lenght;
    }

    public float getLength() {
        return length;
    }
}
