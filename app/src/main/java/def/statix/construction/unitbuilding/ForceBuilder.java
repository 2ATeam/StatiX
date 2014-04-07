package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import def.statix.R;
import def.statix.construction.Force;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.ForceType;

/**
 * Created by Lux on 17.03.14.
 */
public class ForceBuilder extends ConstructionUnitBuilder {

    private Context context; // TODO: refactor later.

    @Override
    public void setRepresentation(Context context, PointF location) {
        this.context = context;
        unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin), location);
    }

    @Override
    public void setType(ConstructionUnitType type) {
        assert context != null;
        unit.setType(type);
        switch ((ForceType) type) {
            case CONCENTRATED: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.force_concentrated), unit.getSpriteLocation());
                break;
            }
            case MOMENT: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.force_moment), unit.getSpriteLocation());
                break;
            }
            case DISTRIBUTED: {
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.force_distributed), unit.getSpriteLocation());
                break;
            }
            default: {
                //placeholder.
                unit.setImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin), unit.getSpriteLocation());
            }
        }
        unit.getOverlay().createForceOverlay((ForceType) type);
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }

    @Override
    public void createNewUnit() {
        unit = new Force();
    }
}