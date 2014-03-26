package def.statix.utils.equations;

import def.statix.utils.MathUtils;

/**
 * Created by AdYa on 25.03.14.
 */
public class LinearEquation {

    /**
     * Unknowns' coefficients.
     */
    private float[] a;

    /**
     * Constant term.
     */
    private float b;

    /**
     * Increases dimension of the equation by given increment.
     */
    private void increaseDimension(int increment) {
        if (increment <= 0) return;
        float[] tmp = a;
        a = new float[a.length + increment];
        for (int i = 0; i < tmp.length; i++) {
            a[i] = tmp[i];
        }
    }

    /**
     * Gets dimension of the equation.
     */
    public int getDimension() {
        return a.length;
    }

    public float getConstantTerm() {
        return b;
    }

    public void setConstantTerm(float constantTerm) {
        this.b = constantTerm;
    }

    public float getCoefficientAt(int index) {
        assert MathUtils.isWithin(-1, index, this.a.length, false);
        return a[index];
    }

    public float[] getCoefficients() {
        return a.clone();
    }

    /**
     * Adds coefficients to the end of the equation.
     */
    public void addCoefficients(float... coefficient) {
        assert coefficient != null;
        int oldLength = a.length;
        increaseDimension(coefficient.length);
        for (int i = oldLength; i < a.length; ++i) {
            setCoefficient(i, coefficient[i - coefficient.length - 1]);
        }
    }

    /**
     * Sets coefficient at specified index.
     */
    public void setCoefficient(int index, float coefficient) {
        assert MathUtils.isWithin(-1, index, this.a.length, false);
        this.a[index] = coefficient;
    }

    /**
     * Creates equation with specified constant term and coefficients.
     */
    public LinearEquation(float constantTerm, float... coefficients) {
        b = constantTerm;
        if (coefficients != null)
            a = coefficients;
        else
            a = new float[1];
    }

    /**
     * Creates zero equation.
     */
    public LinearEquation() {
        this(0, 0);
    }
}
