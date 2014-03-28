package def.statix.calculations.constructions;

import android.graphics.PointF;

import def.statix.calculations.equations.LESSolver;
import def.statix.calculations.equations.LinearEquation;
import def.statix.calculations.equations.LinearEquationsSystem;
import def.statix.construction.Binding;
import def.statix.construction.Force;
import def.statix.construction.Plank;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ForceType;
import def.statix.utils.MathUtils;

/**
 * Created by AdYa on 26.03.14.
 */
public class StaticProblemSolver {

    private enum Projections {X, Y, M}

    private enum MomentDirection {NONE, CW, CCW}

    private static int IND_R = 0;
    private static int IND_X = 1;
    private static int IND_Y = 2;

    private Construction construction;
    private PointF momentCenter;
    private MomentDirection momentDirection;

    public float[] solve(Construction construction) {
        if (construction == null || !construction.canSolve()) return null;
        this.construction = construction;

        momentCenter = getMomentCenter();
        momentDirection = getMomentDirection();

        LinearEquationsSystem system = new LinearEquationsSystem(projectionX(), projectionY(), projectionM());

        LESSolver solver = new LESSolver();
        return solver.solve(system);
    }


    private LinearEquation projectionX() {
        float X[] = new float[Projections.values().length];
        int projIndex = Projections.X.ordinal();
        float[] tmp;
        for (Binding b : construction.getBindings()) {
            tmp = getProjections(b);
            switch ((BindingType) b.getType()) {
                case FIXED:
                    X[IND_X] += tmp[projIndex];
                    break;
                case MOVABLE:
                    X[IND_R] += tmp[projIndex];
                    break;
                case STATIC:
                    X[IND_X] += tmp[projIndex];
                    break;
            }
        }
        float B = 0;

        for (Force f : construction.getForces()) {
            tmp = getProjections(f);
            B += f.getValue() * tmp[projIndex];
        }
        return new LinearEquation(-B, X);
    }

    private LinearEquation projectionY() {
        float Y[] = new float[Projections.values().length];
        int projIndex = Projections.Y.ordinal();
        float[] tmp;
        for (Binding b : construction.getBindings()) {
            tmp = getProjections(b);
            switch ((BindingType) b.getType()) {
                case FIXED:
                    Y[IND_Y] += tmp[projIndex];
                    break;
                case MOVABLE:
                    Y[IND_R] += tmp[projIndex];
                    break;
                case STATIC:
                    Y[IND_Y] += tmp[projIndex];
                    break;
            }
        }
        float B = 0;

        for (Force f : construction.getForces()) {
            tmp = getProjections(f);
            B += f.getValue() * tmp[projIndex];
        }
        return new LinearEquation(-B, Y);
    }

    private LinearEquation projectionM() {
        float M[] = new float[Projections.values().length];
        for (Binding b : construction.getBindings()) {
            switch ((BindingType) b.getType()) {
                case FIXED:
                    M[IND_X] += getMomentProjection(b);
                    break;
                case MOVABLE:
                    M[IND_R] += getMomentProjection(b);
                    break;
                case STATIC:
                    M[IND_Y] += getMomentProjection(b);
                    break;
            }
        }
        float B = 0;

        for (Force f : construction.getForces()) {
            B += getMomentProjection(f);
        }
        return new LinearEquation(-B, M);
    }

    private float[] getProjections(Binding binding) {
        float[] res = new float[Projections.values().length];
        float sign = Math.signum(binding.getAngle());
        float angle = (float) Math.toRadians(Math.abs(binding.getAngle()));
        switch ((BindingType) binding.getType()) {

            case FIXED:
                res[Projections.X.ordinal()] = (float) Math.cos(angle) + -sign * (float) Math.sin(angle);
                res[Projections.Y.ordinal()] = (float) Math.cos(angle) + sign * (float) Math.sin(angle);
                break;
            case MOVABLE:
                res[Projections.X.ordinal()] = -sign * (float) Math.sin(angle);
                res[Projections.Y.ordinal()] = -sign * (float) Math.cos(angle);
                break;
            case STATIC:
                res[Projections.X.ordinal()] = (float) Math.cos(angle) + -sign * (float) Math.sin(angle);
                res[Projections.Y.ordinal()] = (float) Math.cos(angle) + sign * (float) Math.sin(angle);
                break;
        }
        return res;
    }

    private float[] getProjections(Force force) {
        float[] res = new float[Projections.values().length];
        float sign = 0;
        float angle = (float) Math.toRadians(Math.abs(angleRelativeX(force)));
        switch ((ForceType) force.getType()) {

            case CONCENTRATED:
                angle = angleRelativeX(force);
                sign = Math.signum(angle);
                angle = (float) Math.toRadians(angle);
                res[Projections.X.ordinal()] = -sign * (float) Math.cos(angle);
                res[Projections.Y.ordinal()] = -sign * (float) Math.sin(angle);
                break;
            case DISTRIBUTED:
                angle += Math.PI;
                res[Projections.X.ordinal()] = (float) Math.cos(angle);
                res[Projections.Y.ordinal()] = (float) Math.sin(angle);
                break;
            case MOMENT:
                res[Projections.X.ordinal()] = 0;
                res[Projections.Y.ordinal()] = 0;
                break;
        }
        return res;
    }

    private MomentDirection getMomentDirection() {
        for (Force f : construction.getForces()) {
            if (f.getType() == ForceType.MOMENT) {
                if (f.getAngle() == 0) return MomentDirection.CW;
                else return MomentDirection.CCW;
            }
        }
        return MomentDirection.NONE;
    }

    private PointF getMomentCenter() {
        for (Binding b : construction.getBindings()) {
            switch ((BindingType) b.getType()) {
                case FIXED:
                case STATIC:
                    return b.getPosition();
                default:
                    break;
            }
        }
        return null;
    }

    private float getMomentProjection(Force force) {
        if (momentCenter == null) return 0;
        if (force.getType() == ForceType.MOMENT)
            return force.getValue() * -(float) Math.cos(Math.toRadians(force.getAngle()));

        float xSign = 0;
        float ySign = 0;
        switch (momentDirection) {
            case NONE:
                return 0;
            case CW:
                ySign = -Math.signum(angleRelativeX(force)) * Math.signum(force.getPosition().x - momentCenter.x);
                xSign = Math.signum(angleRelativeX(force)) * Math.signum(angleRelativeX(force)) * Math.signum(force.getPosition().y - momentCenter.y);
                break;
            case CCW:
                ySign = Math.signum(angleRelativeX(force)) * Math.signum(force.getPosition().x - momentCenter.x);
                xSign = (float) (-Math.signum(Math.cos(Math.toRadians(angleRelativeX(force)))) * Math.signum(force.getPosition().y - momentCenter.y));
                break;
        }

        float[] pr = getProjections(force);
        float x = xSign * pr[Projections.X.ordinal()] * force.getValue(); // force x-projection
        float y = ySign * pr[Projections.Y.ordinal()] * force.getValue(); // force y-projection

        float M = 0; // total moment applied with x and y projections respective
        float plankPart;
        for (Plank p : construction.getPlanks()) {
            if (MathUtils.approxEquals(p.getAngle(), angleRelativeX(force))) continue;
            plankPart = (force.getType() == ForceType.DISTRIBUTED && force.getTargetPlank().equals(p) ? 0.5f : 1f);
            if (!MathUtils.approxEquals(p.getAngle(), 0)) { // if not parallel to OX
                M += x * p.getLength() * plankPart;
            }

            if (!MathUtils.approxEquals(p.getAngle(), 90)) { // if not parallel to OY
                M += y * p.getLength() * plankPart;
            }
        }
        return M;
    }


    private float getMomentProjection(Binding binding) {
        if (momentCenter == null) return 0;
        if (binding.getPosition().equals(momentCenter.x, momentCenter.y))
            return 0;

        float xSign = 0;
        float ySign = 0;
        switch (momentDirection) {
            case NONE:
                return 0;
            case CW:
                ySign = -Math.signum(binding.getPosition().x - momentCenter.x);
                xSign = Math.signum(binding.getPosition().y - momentCenter.y);
                break;
            case CCW:
                ySign = Math.signum(binding.getPosition().x - momentCenter.x);
                xSign = -Math.signum(binding.getPosition().y - momentCenter.y);
                break;
        }

        float[] pr = getProjections(binding);
        float x = xSign * pr[Projections.X.ordinal()]; // binding reaction x-projection
        float y = ySign * pr[Projections.Y.ordinal()]; // binding reaction y-projection

        float M = 0; // total moment applied with x and y projections

        for (Plank p : construction.getPlanks()) {
            if (MathUtils.approxEquals(p.getAngle(), binding.getAngle() + 90f)) continue;

            if (!MathUtils.approxEquals(p.getAngle(), 0)) { // if not parallel to OX
                M += x * p.getLength();
            }

            if (!MathUtils.approxEquals(p.getAngle(), 90)) { // if not parallel to OY
                M += y * p.getLength();
            }
        }
        return M;
    }


    /**
     * Gets angle between force vector and OX.
     */
    private float angleRelativeX(Force force) {
        assert force != null && force.getTargetPlank() != null : "Either force or it's targeted plank are null.";
        float pAngle = force.getTargetPlank().getAngle();
        float fAngle = force.getAngle();
        if (pAngle == 0)
            return fAngle;
        else if (fAngle == 0)
            return pAngle;
        else if (pAngle * fAngle < 0)
            return fAngle + pAngle;
        else
            return fAngle - pAngle;
    }

    /**
     * Gets angle between force vector and OY.
     */
    private float angleRelativeY(Force force) {
        return 90f - angleRelativeX(force);
    }
}
