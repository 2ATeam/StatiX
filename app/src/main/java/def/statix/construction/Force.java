package def.statix.construction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import def.statix.R;
import def.statix.construction.unittypes.ForceType;
import def.statix.rendering.Renderable;
import def.statix.rendering.Sprite;

/**
 * Created by Lux on 19.02.14.
 * This can be applied to any construction unit.
 */
public class Force extends Renderable {
    private ForceType type;
    private PointF location;
    private Bitmap image;
    private Context context;

    public Force(Context context, ForceType type, float x, float y) {
        this.type = type;
        this.context = context;
        overlayType = false;
        image = null;
        location = new PointF(x, y);
        loadImage();
    }

    private void loadImage(){
        assert context != null;
        switch (type) {
            case CONCENTRATED: {
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.force_conc);
                break;
            }
            case MOMENT: {
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.force_moment);
                break;
            }
            case DISTRIBUTED: {
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.force_distributed);
                break;
            }
            default: {
                //placeholder.
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin);
            }
        }
        this.setSprite(new Sprite(image, location.x, location.y));
    }

    public ForceType getType() {
        return type;
    }

    public void setType(ForceType type) {
        this.type = type;
        loadImage(); // reload image is needed, due to the type change.
    }
}
