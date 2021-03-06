package def.statix.construction;

/**
 * Created by Lux on 19.02.14.
 * This can be applied to any construction unit.
 */
public class Force extends ConstructionUnit {

///TODO: Think  about deleteing an 'outgoingForce' from ConstructionUnit - it's something unnecessary

    /**
     * Reference to the plank to which the force is applied.
     */
    private Plank targetPlank;
    private float value;

    /**
     * Applies force to specified plank.
     */
    public void applyToPlank(Plank plank) {
        targetPlank = plank;
    }

    /**
     * Get targeted plank.
     */
    public Plank getTargetPlank() {
        return targetPlank;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void setAngle(float angle) {
        this.angle = (angle > 180 ? angle - 180 : angle);
    }

    public float getValue() {
        return value;
    }
}
