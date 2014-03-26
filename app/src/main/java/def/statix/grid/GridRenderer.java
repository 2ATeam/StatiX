package def.statix.grid;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by AdYa on 22.03.14.
 */
public class GridRenderer {

    public enum PrimaryAxis {AXIS_X, AXIS_Y}

    /**
     * Axis which will be the original when scaling grid.
     */
    private PrimaryAxis primaryAxis;

    /**
     * Scale factor which is used to convert coordinates system from screen to grid and vice versa.
     */
    private float scaleFactor;

    private float screenWidth;
    private float screenHeight;

    private Grid grid;

    private float[] majorXSections;
    private float[] majorYSections;
    private float[] minorSections;

    private Paint gridAxisPaint;
    private Paint gridMinorPaint;
    private Paint gridMajorPaint;
    private Paint gridGuidePaint;
    private Paint gridLabelPaint;

    public GridRenderer(Grid grid) {
        assert grid != null : "Cannot initialize with null Grid.";
        this.grid = grid;
        this.primaryAxis = PrimaryAxis.AXIS_X;

        gridAxisPaint = new Paint();
        gridAxisPaint.setARGB(255, 255, 255, 255);
        gridAxisPaint.setStrokeWidth(2.5f);
        gridGuidePaint = new Paint();
        gridGuidePaint.setARGB(180, 220, 255, 255);
        gridMajorPaint = new Paint();
        gridMajorPaint.setARGB(120, 255, 255, 255);
        gridMinorPaint = new Paint();
        gridMinorPaint.setARGB(60, 200, 200, 200);
        gridLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridLabelPaint.setARGB(255, 255, 255, 255);
    }

    public GridRenderer(Grid grid, float screenWidth, float screenHeight) {
        this(grid);
        setScreen(screenWidth, screenHeight);
    }

    public void setGrid(Grid grid) {
        assert grid != null : "Can't set grid = null.";
        this.grid = grid;
        scaleGrid();
    }

    public Grid getGrid() {
        return grid;
    }

    public void setPrimaryAxis(PrimaryAxis axis) {
        this.primaryAxis = axis;
    }

    private float convertToScreen(float gridValue) {
        return gridValue * scaleFactor;
    }

    public GridPoint convertToScreen(GridPoint gridPoint) {
        return convertToScreen(gridPoint.x, gridPoint.y);
    }

    public GridPoint convertToScreen(float gridX, float gridY) {
        return new GridPoint(convertToScreen(gridX), screenHeight - convertToScreen(gridY));
    }

    private float convertToGrid(float screenValue) {
        return screenValue / scaleFactor;
    }

    public GridPoint convertToGrid(GridPoint screenPoint) {
        return convertToGrid(screenPoint.x, screenPoint.y);
    }

    public GridPoint convertToGrid(float screenX, float screenY) {
        return new GridPoint(convertToGrid(screenX), convertToGrid(screenHeight - screenY));
    }

    public void setScreen(float screenWidth, float screenHeight) {
        assert screenWidth != 0 && screenHeight != 0 : "Screen size can't be 0.";
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        scaleGrid();
    }

    public void setGridGuideLine(float screenX, float screenY) {
        grid.setGuideline(convertToGrid(screenX, screenY));
    }

    public void drawGrid(Canvas canvas) {
        assert grid != null && canvas != null;

        drawGridAxis(canvas);
        if (grid.isShowSectionLines()) drawGridSectionsLines(canvas);
        else drawGridSections(canvas);
        if (grid.isShowGuides()) drawGridGuides(canvas);
        drawGridLabels(canvas);
    }

    private void scaleGrid() {
        switch (primaryAxis) {
            case AXIS_X:
                if (grid.getWidth() == 0) return;
                scaleFactor = screenWidth / grid.getWidth();
                grid.setHeight(convertToGrid(screenHeight));
                break;
            case AXIS_Y:
                if (grid.getHeight() == 0) return;
                scaleFactor = screenHeight / grid.getHeight();
                grid.setWidth(convertToGrid(screenWidth));
                break;
        }
        precalculateSections();
    }

    private void precalculateSections() {
        majorXSections = new float[grid.getSectionXCount()];
        majorYSections = new float[grid.getSectionYCount()];
        minorSections = new float[grid.getMinorSectionsCount()];

        for (int i = 0; i < grid.getSectionXCount(); ++i) {
            majorXSections[i] = i * convertToScreen(grid.getMajorLength());
        }
        for (int i = 0; i < grid.getSectionYCount(); ++i) {
            majorYSections[i] = screenHeight - i * convertToScreen(grid.getMajorLength());
        }
        for (int j = 0; j < grid.getMinorSectionsCount(); ++j) {
            minorSections[j] = j * convertToScreen(grid.getMinorLength());
        }
    }

    private void drawGridAxis(Canvas canvas) {
      /*X*/
        canvas.drawLine(0, screenHeight, screenWidth, screenHeight, gridAxisPaint);
      /*Y*/
        canvas.drawLine(0, 0, 0, screenHeight, gridAxisPaint);
    }

    private void drawGridSections(Canvas canvas) {
        int length = 20;
        for (float i : majorXSections) {
            canvas.drawLine(i, screenHeight, i, screenHeight - length, gridMajorPaint);
            if (grid.isShowMinor()) {
                for (float j : minorSections) {
                    canvas.drawLine(i + j, screenHeight, i + j, screenHeight - length / 2, gridMinorPaint);
                }
            }
        }
        for (float i : majorYSections) {
            canvas.drawLine(0, i, length, i, gridMajorPaint);
            if (grid.isShowMinor()) {
                for (float j : minorSections) {
                    canvas.drawLine(0, i - j, length / 2, i - j, gridMinorPaint);
                }

            }
        }
    }

    private void drawGridSectionsLines(Canvas canvas) {
        for (float i : majorXSections) {
            canvas.drawLine(i, 0, i, screenHeight, gridMajorPaint);
            if (grid.isShowMinor()) {
                for (float j : minorSections) {
                    canvas.drawLine(i + j, 0, i + j, screenHeight, gridMinorPaint);
                }
            }
        }
        for (float i : majorYSections) {
            canvas.drawLine(0, i, screenWidth, i, gridMajorPaint);
            if (grid.isShowMinor()) {
                for (float j : minorSections) {
                    canvas.drawLine(0, i - j, screenWidth, i - j, gridMinorPaint);
                }

            }
        }
    }

    private void drawGridGuides(Canvas canvas) {
        GridPoint g = convertToScreen(grid.getGuideline());
        canvas.drawLine(g.x, 0, g.x, screenHeight, gridGuidePaint);
        canvas.drawLine(0, g.y, screenWidth, g.y, gridGuidePaint);
        canvas.drawText(String.format("%.2f", grid.getGuideline().x), g.x * 1.01f, 0.96f * screenHeight, gridLabelPaint);
        canvas.drawText(String.format("%.2f", grid.getGuideline().y), 0.04f * screenWidth, g.y - 5, gridLabelPaint);
    }

    private void drawGridLabels(Canvas canvas) {
        for (float i : majorXSections) {
            canvas.drawText(String.format("%.2f", convertToGrid(i)), i + 2, 0.98f * screenHeight, gridLabelPaint);
        }
        for (float i : majorYSections) {
            canvas.drawText(String.format("%.2f", convertToGrid(screenHeight - i)), 2, i - 5, gridLabelPaint);
        }
    }

}
