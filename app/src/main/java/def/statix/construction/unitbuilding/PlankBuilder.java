package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.BitmapFactory;

import def.statix.R;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by Lux on 22.02.14.
 */
public class PlankBuilder extends ConstructionUnitBuilder {

    @Override
    public void setRepresentation(Context context) {
        unit.loadImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.plank));
    }

    @Override
    public void setType(ConstructionUnitType type) {
        unit.setType(null); // there is no type of beam now. it is just beam. nothing else.
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }
}
