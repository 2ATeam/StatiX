package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import def.statix.R;
import def.statix.construction.Binding;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.rendering.UnitOverlay;

/**
 * Created by Lux on 22.02.14.
 */
public class BindingBuilder extends ConstructionUnitBuilder {

    private Context context;

    @Override
    public void setRepresentation(Context context, PointF location) {
        this.context = context;
        unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin), location);
    }

    @Override
    public void setType(ConstructionUnitType type) {
        unit.setType(type);
        // load specific image. if type is not of BindingType enumeration - the placeholder image will not be replaced.
        switch ((BindingType) type) {
            case FIXED: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.binding_fixed), unit.getSpriteLocation());
                break;
            }
            case MOVABLE: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.binding_movalbe), unit.getSpriteLocation());
                break;
            }
            case STATIC: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.binding_static), unit.getSpriteLocation());
                break;
            }
        }
        unit.getOverlay().createBindingOverlay();
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }

    @Override
    public void createNewUnit() {
        unit = new Binding();
    }
}
