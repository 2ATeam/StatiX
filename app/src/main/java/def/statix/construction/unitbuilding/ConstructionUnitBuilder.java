package def.statix.construction.unitbuilding;

import android.content.Context;

import def.statix.construction.ConstructionUnit;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by Lux on 22.02.14.
 * Builder pattern implemented in this package.
 */
public abstract class ConstructionUnitBuilder {
    protected ConstructionUnit unit;

    public abstract void setRepresentation(Context context);
    public abstract void setType(ConstructionUnitType type);
    public abstract void setPosition(float x, float y);

    public void createNewUnit() {
        unit = new ConstructionUnit();
    }

    public ConstructionUnit getUnit() {
        return unit;
    }
}
