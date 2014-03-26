package def.statix.grid;

/**
 * Simplified Point class
 */
public class GridPoint {
    public float x;
    public float y;

    public GridPoint() {
        this(0, 0);
    }

    public GridPoint(float x, float y) {
        set(x, y);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(GridPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    public boolean equals(GridPoint point) {
        return point != null && this.equals(point.x, point.y);
    }

    public boolean equals(float x, float y) {
        return this.x == x && this.y == y;
    }
}
