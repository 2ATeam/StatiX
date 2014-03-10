package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import def.statix.R;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.rendering.UnconfirmedPlank;

/**
 * Created by Lux on 22.02.14.
 */
public class PlankBuilder extends ConstructionUnitBuilder {

    private UnconfirmedPlank plank;
    private Paint plankPaint;
    private PointF location;

    private Bitmap createBitmap(Context context) {
        if (plank != null && plankPaint != null) {
            int strokeOffset = (int) plankPaint.getStrokeWidth() / 2;

            int height = (int) ((plank.getBegin().y < plank.getEnd().y) ?
                 (plank.getEnd().y - plank.getBegin().y) : (plank.getBegin().y - plank.getEnd().y));

            int width =  (int) ((plank.getBegin().x < plank.getEnd().x) ?
                 (plank.getEnd().x - plank.getBegin().x) : (plank.getBegin().x - plank.getEnd().x));

            Bitmap image = Bitmap.createBitmap(width + strokeOffset, height + strokeOffset, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(image);
            //have to translate plank coordinates to the origin. Because of using local canvas coords.
            canvas.drawLine(plank.getBegin().x - location.x + strokeOffset,
                            plank.getBegin().y - location.y + strokeOffset,
                            plank.getEnd().x   - location.x,
                            plank.getEnd().y   - location.y, plankPaint);
            return  image;
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.plank); // placeholder.
    }

    @Override
    public void setRepresentation(Context context) {
        unit.setImage(createBitmap(context));
    }

    @Override
    public void setType(ConstructionUnitType type) {
        unit.setType(null); // there is no type of beam now. it is just beam. nothing else.
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }

    public void setPlankParams(UnconfirmedPlank plank, PointF unitLocation, Paint plankPaint) {
        this.plankPaint = plankPaint;
        this.plank = plank;
        this.location = unitLocation;
    }
}
