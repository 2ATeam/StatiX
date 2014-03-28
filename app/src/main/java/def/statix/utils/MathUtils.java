package def.statix.utils;

public class MathUtils {
    public static final float APPROXIMATION_ERROR = 0.1f;

    /**
     * Clamps value between to values.
     *
     * @param min       Lower bound of the range.
     * @param value     Clamping value.
     * @param max       Upper bound of the range.
     * @param inclusive Indicates whether the range's bounds are included.
     */
    public static float clamp(float min, float value, float max, boolean inclusive) {
        if (inclusive) return (min <= value ? (value <= max ? value : max) : min);
        else return (min < value ? (value < max ? value : max) : min);
    }

    /**
     * Clamps value between to values.
     *
     * @param min       Lower bound of the range.
     * @param value     Clamping value.
     * @param max       Upper bound of the range.
     * @param inclusive Indicates whether the range's bounds are included.
     */
    public static int clamp(int min, int value, int max, boolean inclusive) {
        if (inclusive) return (min <= value ? (value <= max ? value : max) : min);
        else return (min < value ? (value < max ? value : max) : min);
    }

    /**
     * Checks whether the value belongs to specified range.
     *
     * @param min       Lower bound of the range.
     * @param value     Value to check.
     * @param max       Upper bound of the range.
     * @param inclusive Indicates whether the range's bounds are included.
     */
    public static boolean isWithin(int min, int value, int max, boolean inclusive) {
        if (inclusive) return (min <= value && value <= max);
        else return (min < value && value < max);
    }

    /**
     * Checks whether the value belongs to specified range.
     *
     * @param min       Lower bound of the range.
     * @param value     Value to check.
     * @param max       Upper bound of the range.
     * @param inclusive Indicates whether the range's bounds are included.
     */
    public static boolean isWithin(float min, float value, float max, boolean inclusive) {
        if (inclusive) return (min <= value && value <= max);
        else return (min < value && value < max);
    }

    /**
     * Compares two values with approximation error
     */
    public static boolean approxEquals(float first, float second, float approxError) {
        return (Math.abs(first - second) <= Math.abs(approxError));
    }

    /**
     * Compares two values with default approximation error
     */
    public static boolean approxEquals(float first, float second) {
        return approxEquals(first, second, APPROXIMATION_ERROR);
    }
}
