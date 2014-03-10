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

    private Context context; // TODO: refactor later.

    @Override
    public void setRepresentation(Context context) {
        //image is a placeholder
        unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin));
        this.context = context;
    }

    @Override
    public void setType(ConstructionUnitType type) {
        unit.setType(type);
        // load specific image. if type is not of BindingType enumeration - the placeholder image will not be replaced.
        assert type instanceof BindingType;
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
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }
}
