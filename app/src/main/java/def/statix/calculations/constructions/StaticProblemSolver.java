package def.statix.calculations.constructions;

import android.graphics.PointF;

import def.statix.calculations.equations.LESSolver;
import def.statix.calculations.equations.LinearEquation;
import def.statix.calculations.equations.LinearEquationsSystem;
import def.statix.construction.Binding;
import def.statix.construction.Force;
import def.statix.construction.unittypes.BindingType;
import def.statix.construction.unittypes.ForceType;

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

    public void solve(Construction construction) {
        if (construction == null || !construction.canSolve()) return;
        this.construction = construction.clone();

        momentCenter = getMomentCenter();
        momentDirection = getMomentDirection();

        LinearEquationsSystem system = new LinearEquationsSystem(projectionX(), projectionY(), projectionM());

        LESSolver solver = new LESSolver();
        solver.solve(system);
    }


    private LinearEquation projectionX() {
        float X[] = new float[Projections.values().length];
        int projIndex = Projections.X.ordinal();
        float[] tmp;
        for (Binding b : construction.getBindings()) {
            tmp = getBindingProjections(b);
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
            tmp = getForceProjections(f);
            B += tmp[projIndex];
        }
        return new LinearEquation(-B, X);
    }

    private LinearEquation projectionY() {
        float Y[] = new float[Projections.values().length];
        int projIndex = Projections.Y.ordinal();
        float[] tmp;
        for (Binding b : construction.getBindings()) {
            tmp = getBindingProjections(b);
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
            tmp = getForceProjections(f);
            B += tmp[projIndex];
        }
        return new LinearEquation(-B, Y);
    }

    private LinearEquation projectionM() {
        float M[] = new float[Projections.values().length];
        int projIndex = Projections.M.ordinal();
        float[] tmp;
        for (Binding b : construction.getBindings()) {
            tmp = getBindingProjections(b);
            switch ((BindingType) b.getType()) {
                case FIXED:
                    M[IND_Y] += tmp[projIndex];
                    break;
                case MOVABLE:
                    M[IND_R] += tmp[projIndex];
                    break;
                case STATIC:
                    M[IND_Y] += tmp[projIndex];
                    break;
            }
        }
        float B = 0;

        for (Force f : construction.getForces()) {
            tmp = getForceProjections(f);
            B += tmp[projIndex];
        }
        return new LinearEquation(-B, M);
    }

    private float[] getBindingProjections(Binding binding) {
        float[] res = new float[3];
        float sign = Math.signum(binding.getAngle());
        float angle = (float) Math.toRadians(Math.abs(binding.getAngle()));
        switch ((BindingType) binding.getType()) {

            case FIXED:
                res[Projections.X.ordinal()] = (float) Math.cos(angle) + -sign * (float) Math.sin(angle);
                res[Projections.Y.ordinal()] = (float) Math.cos(angle) + sign * (float) Math.sin(angle);
                res[Projections.M.ordinal()] = getMomentProjection(binding);
                break;
            case MOVABLE:
                res[Projections.X.ordinal()] = -sign * (float) Math.sin(angle);
                res[Projections.Y.ordinal()] = -sign * (float) Math.cos(angle);
                res[Projections.M.ordinal()] = getMomentProjection(binding);
                break;
            case STATIC:
                res[Projections.X.ordinal()] = (float) Math.cos(angle) + -sign * (float) Math.sin(angle);
                res[Projections.Y.ordinal()] = (float) Math.cos(angle) + sign * (float) Math.sin(angle);
                res[Projections.M.ordinal()] = getMomentProjection(binding);
                break;
        }
        return res;
    }

    private float[] getForceProjections(Force force) {
        float[] res = new float[3];
        float sign = Math.signum(force.getAngle());
        float angle = (float) Math.toRadians(Math.abs(force.getAngle()));
        switch ((ForceType) force.getType()) {

            case CONCENTRATED:
                angle = angleRelativeX(force);
                sign = Math.signum(angle);
                angle = (float) Math.toRadians(angle);
                res[Projections.X.ordinal()] = -sign * (float) Math.cos(angle);
                res[Projections.Y.ordinal()] = -sign * (float) Math.sin(angle);
                res[Projections.M.ordinal()] = getMomentProjection(force);
                break;
            case DISTRIBUTED:
                angle += Math.PI / 2;
                res[Projections.X.ordinal()] = (float) Math.cos(angle);
                res[Projections.Y.ordinal()] = (float) Math.sin(angle);
                res[Projections.M.ordinal()] = getMomentProjection(force);
                break;
            case MOMENT:
                res[Projections.X.ordinal()] = 0;
                res[Projections.Y.ordinal()] = 0;
                res[Projections.M.ordinal()] = (float) Math.cos(angle);
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
        if (force.getPosition().x < momentCenter.x) { // if force is on the left side of the moment
            if (force.getPosition().y <)
        }
        switch ((ForceType) force.getType()) {

            case CONCENTRATED:
                return 0;
            case DISTRIBUTED:
                return 0;
            case MOMENT:
                return force.getValue();
        }
        return 0;
    }

    private float getMomentProjection(Binding binding) {
        if (momentCenter == null) return 0;

        return 0;
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
