package def.statix.utils;

public class MathUtils {
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
     * Extends given array by specified increment value.
     */
    private static float[] extendArraySize(float[] array, int increment) {
        float[] temp = array.clone();
        array = new float[array.length + increment];
        System.arraycopy(temp, 0, array, 0, temp.length);
        return array;
    }
}
