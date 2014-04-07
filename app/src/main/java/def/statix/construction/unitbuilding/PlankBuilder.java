package def.statix.construction.unitbuilding;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import def.statix.R;
import def.statix.construction.Plank;
import def.statix.construction.unittypes.ConstructionUnitType;
import def.statix.construction.unittypes.PlankType;
import def.statix.rendering.UnconfirmedPlank;

/**
 * Created by Lux on 22.02.14.
 */
public class PlankBuilder extends ConstructionUnitBuilder {

    private UnconfirmedPlank uncPlank;
    private Paint plankPaint;
    private PointF location;

    @Override
    public void setRepresentation(Context context, PointF location) {
        unit.setImage(createBitmap(context), location);
        unit.getOverlay().getJoints().clear();
        unit.getOverlay().addJoint(uncPlank.getBegin());
        unit.getOverlay().addJoint(uncPlank.getEnd());
        PointF plankCenter = new PointF(location.x + (unit.getBoundingRect().width() / 2) - 20,
                                        location.y + (unit.getBoundingRect().height() / 2) - 20);
        unit.getOverlay().addJoint(plankCenter);
        unit.getOverlay().createPlankOverlay();
    }

    @Override
    public void setType(ConstructionUnitType type) {
        unit.setType(PlankType.PLANK);
    }

    @Override
    public void setPosition(float x, float y) {
        unit.setPosition(x, y);
    }

    private Bitmap createBitmap(Context context) {
        if (uncPlank != null && plankPaint != null) {
            //Paint txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            PointF begin = uncPlank.getBegin();
            PointF end = uncPlank.getEnd();

            int strokeOffset = 40;
            int frameOffset = strokeOffset / 2;
            int width = (int) ((begin.x < end.x) ? (end.x - begin.x) : (begin.x - end.x));
            int height = (int) ((begin.y < end.y) ? (end.y - begin.y) : (begin.y - end.y));

            Bitmap image = Bitmap.createBitmap(width + strokeOffset,
                    height + strokeOffset, Bitmap.Config.ARGB_4444);

//            PointF txtPos = new PointF(image.getWidth() / 2 - plankPaint.getStrokeWidth(),
//                    image.getHeight() / 2 + plankPaint.getStrokeWidth()); /// TODO: use this!

            Canvas canvas = new Canvas(image);
            canvas.drawLine(begin.x - location.x + frameOffset,
                    begin.y - location.y + frameOffset,
                    end.x - location.x + frameOffset,
                    end.y - location.y + frameOffset, plankPaint);
            return image;
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.shaolin); // placeholder.
    }

    @Override
    public void createNewUnit() {
        if (uncPlank != null)
            unit = new Plank(uncPlank);
        else
            unit = new Plank();
    }

    public void setPlankParams(UnconfirmedPlank plank, PointF unitLocation, Paint plankPaint) {
        this.plankPaint = plankPaint;
        this.uncPlank = plank;
        this.location = unitLocation;
    }
}
