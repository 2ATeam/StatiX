package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.BitmapFactory;

import def.statix.R;
import def.statix.construction.Force;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;

/**
 * Created by Lux on 17.03.14.
 */
public class ForceBuilder extends ConstructionUnitBuilder{

    private Context context; // TODO: refactor later.

    @Override
    public void createNewUnit() {
        unit = new Force();
    }

    @Override
    public void setRepresentation(Context context) {
        this.context = context;
        unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin));
    }

    @Override
    public void setType(ConstructionUnitType type) {
        assert context != null;
        switch ((ForceType)type) {
            case CONCENTRATED: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.force_conc));
                break;
            }
            case MOMENT: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.force_moment));
                break;
            }
            case DISTRIBUTED: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.force_distributed));
                break;
            }
            default: {
                //placeholder.
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin));
            }
        }
        unit.getOverlay().createForceOrBindingOverlay();
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }
}