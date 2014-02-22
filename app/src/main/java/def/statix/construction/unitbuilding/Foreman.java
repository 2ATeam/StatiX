package def.statix.construction.unitbuilding;

import android.content.Context;

import def.statix.construction.ConstructionUnit;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by Lux on 22.02.14.
 */
public class Foreman {
    private ConstructionUnitBuilder builder;

    public void setBuilder(ConstructionUnitBuilder builder){
        this.builder = builder;
    }

    public ConstructionUnit getUnit(){
        assert builder.getUnit() != null;
        return builder.getUnit();
    }

    //if we constructing beam, type parameter is redundant(type of constructed beam is always null).
    public void constructUnit(Context context, ConstructionUnitType type){
        builder.createNewUnit();
        builder.loadBitmap(context);
        builder.setType(type);
    }
}
