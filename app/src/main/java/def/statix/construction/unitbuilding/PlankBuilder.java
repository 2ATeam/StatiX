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
            PointF begin = plank.getBegin();
            PointF end = plank.getEnd();
            int strokeOffset = 40;
            int frameOffset = strokeOffset / 2;
            int width =  (int) ((begin.x < end.x) ? (end.x - begin.x) : (begin.x - end.x));
            int height = (int) ((begin.y < end.y) ? (end.y - begin.y) : (begin.y - end.y));
            Bitmap image = Bitmap.createBitmap(width + strokeOffset, height + strokeOffset, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(image);
            canvas.drawLine(begin.x - location.x + frameOffset,
                            begin.y - location.y + frameOffset,
                            end.x   - location.x + frameOffset,
                            end.y   - location.y + frameOffset, plankPaint);
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
        unit.setType(null); // there is no type of plank now. it is just plank. nothing else.
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
