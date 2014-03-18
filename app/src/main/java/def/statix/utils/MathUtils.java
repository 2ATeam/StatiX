package def.statix.utils;

public class MathUtils {
    /**
     * Clamps value between to values
     */
    public static float clamp(float min, float value, float max) {
        return (min <= value ? (value <= max ? value : max) : min);
    }

    public static int clamp(int min, int value, int max) {
        return (min <= value ? (value <= max ? value : max) : min);
    }
}
