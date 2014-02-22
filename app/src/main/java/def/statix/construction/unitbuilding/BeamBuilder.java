package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.BitmapFactory;

import def.statix.R;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by Lux on 22.02.14.
 */
public class BeamBuilder extends ConstructionUnitBuilder {

    @Override
    public void loadBitmap(Context context) {
        //TODO: replace the placeholder image.
        unit.loadImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
    }

    @Override
    public void setType(ConstructionUnitType type) {
        unit.setType(null); // there is no type of beam now. it is just beam. nothing else.
    }
}