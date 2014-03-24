package def.statix.grid;

/**
 * Created by AdYa on 22.03.14.
 */
public class Grid {

    /**
     * Length of the major sections.
     */
    private float majorLength;

    /**
     * Length of the minor sections.
     */
    private float minorLength;

    private float width;

    private float height;

    /**
     * Optional guideline coordinates.
     */
    private GridPoint guideline;

    /**
     * Indicates whether minor sections should be drew.
     */
    private boolean showMinor;

    /**
     * Indicates whether guidelines should be drew.
     */
    private boolean showGuides;

    /**
     * Indicates whether the sections should be drew
     */
    private boolean showSectionLines;

    /**
     * Indicates whether the labels should be displayed
     */
    private boolean showLabels;

    /**
     * Number of sections which can fit X-axis.
     */
    private int sectionXCount;

    /**
     * Number of sections which can fit Y-axis.
     */
    private int sectionYCount;

    /**
     * Number of minor sections in major section.
     */
    private int minorSectionsCount;

    /**
     * Calculates all measures of the grid.
     */
    private void calculateGrid() {
        sectionXCount = (int) (width / majorLength) + 1;
        sectionYCount = (int) (height / majorLength) + 1;
        minorLength = majorLength / minorSectionsCount;
    }

    public Grid() {
        this.majorLength = 5.0f;
        this.minorSectionsCount = 5;
        this.guideline = new GridPoint();
        this.showMinor = true;
        this.showGuides = false;
    }

    public Grid(float width, float height) {
        this();
        this.width = width;
        this.height = height;
        calculateGrid();
    }

    public float getMajorLength() {
        return this.majorLength;
    }

    public float getMinorLength() {
        return this.minorLength;
    }

    public void setWidth(float width) {
        this.width = width;
        calculateGrid();
    }

    public float getWidth() {
        return width;
    }

    public void setHeight(float height) {
        this.height = height;
        calculateGrid();
    }

    public float getHeight() {
        return height;
    }

    public void setGuideline(float guidelineX, float guidelineY) {
        this.guideline.set(guidelineX, guidelineY);
    }

    public void setGuideline(GridPoint guideline) {
        this.guideline.set(guideline);
    }

    public GridPoint getGuideline() {
        return this.guideline;
    }

    public int getSectionXCount() {
        return sectionXCount;
    }

    public int getSectionYCount() {
        return sectionYCount;
    }

    public int getMinorSectionsCount() {
        return minorSectionsCount;
    }

    public void setMinorSectionsCount(int minorSectionsCount) {
        this.minorSectionsCount = minorSectionsCount;
    }

    public void setShowMinor(boolean showMinor) {
        this.showMinor = showMinor;
    }

    public boolean isShowMinor() {
        return showMinor;
    }

    public void setShowGuides(boolean showGuides) {
        this.showGuides = showGuides;
    }

    public boolean isShowGuides() {
        return showGuides;
    }

    public boolean isShowSectionLines() {
        return showSectionLines;
    }

    public void setShowSectionLines(boolean showSectionLines) {
        this.showSectionLines = showSectionLines;
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

}
