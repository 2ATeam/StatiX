package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.BitmapFactory;

import def.statix.R;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;

/**
 * Created by Lux on 22.02.14.
 */
public class BindingBuilder extends ConstructionUnitBuilder{

    private Context context;

    @Override
    public void setRepresentation(Context context) {
        this.context = context;
        unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin));
    }

    @Override
    public void setType(ConstructionUnitType type) {
        unit.setType(type);
        // load specific image. if type is not of BindingType enumeration - the placeholder image will not be replaced.
        switch ((BindingType)type){
            case FIXED:{
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.binding_const));
                break;
            }
            case MOVABLE:{
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.binding_movalbe));
                break;
            }
            case STATIC:{
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.binding_stationary));
                break;
            }
        }
        unit.getOverlay().createForceOrBindingOverlay();
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }
}
