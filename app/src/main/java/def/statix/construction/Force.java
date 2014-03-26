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

    /**
     * Applies force to specified plank.
     */
    public void applyToPlank(Plank plank) {
        targetPlank = plank;
    }
}
