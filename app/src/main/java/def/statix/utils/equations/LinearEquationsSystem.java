package def.statix.utils.equations;

import def.statix.utils.MathUtils;

public class LinearEquationsSystem {

    private LinearEquation[] eq;

    public LinearEquationsSystem() {
        this(new LinearEquation[0]);
    }

    public LinearEquationsSystem(LinearEquation... equations) {
        eq = equations.clone();
    }

    /**
     * Completes an equation to specified size by adding zero coefficients to the end.
     */
    private void completeEquation(LinearEquation equation, int size) {
        assert size > 0;
        if (equation == null || size <= equation.getDimension()) return;
        float[] trailer = new float[size - equation.getDimension()];
        equation.addCoefficients(trailer);
    }

    /**
     * Completes all equations in the system to specified size.
     */
    private void completeSystem(int size) {
        for (LinearEquation e : eq) {
            if (eq != null)
                completeEquation(e, size);
        }
    }

    /**
     * Retrieves the highest dimension of all equations.
     */
    private int getHighestDimension() {
        int dim = 0;
        for (LinearEquation e : eq) {
            if (e != null && e.getDimension() > dim) {
                dim = e.getDimension();
            }
        }
        return dim;
    }

    /**
     * Increases dimension of the equation by given increment.
     */
    private void increaseDimension(int increment) {
        if (increment <= 0) return;
        LinearEquation[] tmp = eq;
        int oldLength = eq.length;
        eq = new LinearEquation[eq.length + increment];
        for (int i = 0; i < tmp.length; i++) {
            eq[i] = tmp[i];
        }
        for (int i = oldLength; i < eq.length; ++i) {
            eq[i] = new LinearEquation();
        }
    }

    /**
     * Gets dimension of the system.
     */
    public int getDimension() {
        return eq.length;
    }

    public LinearEquation getEquationAt(int index) {
        assert MathUtils.isWithin(-1, index, this.eq.length, false);
        return eq[index];
    }

    public LinearEquation[] getEquations() {
        return eq.clone();
    }

    /**
     * Adds number of equations to the system.
     * System will be completed to be sure that equations dimensions are the same for all equations.
     */
    public void addEquations(LinearEquation... equations) {
        assert equations != null;
        int oldLength = eq.length;
        increaseDimension(equations.length);
        for (int i = oldLength; i < eq.length; ++i) {
            setEquation(i, equations[i - oldLength]);
        }
        completeSystem(getHighestDimension());
    }

    /**
     * Sets equation at specified index.
     */
    public void setEquation(int index, LinearEquation equation) {
        assert MathUtils.isWithin(-1, index, this.eq.length, false) && equation != null;
        this.eq[index] = equation;
    }

    public float[] getConstantTermsVector() {
        float[] vector = new float[getDimension()];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = eq[i].getConstantTerm();
        }
        return vector;
    }

    /**
     * Check whether the system can be solved.
     * System can be solved only when number of equations equals equations dimension.
     */
    public boolean canSolve() {
        return getHighestDimension() == getDimension();
    }
}
