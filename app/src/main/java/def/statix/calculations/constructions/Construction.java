package def.statix.calculations.constructions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import def.statix.construction.Binding;
import def.statix.construction.ConstructionUnit;
import def.statix.construction.Force;
import def.statix.construction.Plank;
import def.statix.construction.unittypes.BindingType;
import def.statix.rendering.Renderable;
import def.statix.utils.MathUtils;

/**
 * Storage of construction units.
 */
public class Construction implements Iterable<ConstructionUnit> {

    private CopyOnWriteArrayList<Renderable> renderables;
    private ArrayList<Plank> planks;
    private ArrayList<Binding> bindings;
    private ArrayList<Force> forces;


    /**
     * Array which stores number of bindings for each type.
     */
    private int[] bindingsAmount;
    private int[][] bindingLimits;

    public Construction() {
        renderables = new CopyOnWriteArrayList<>();
        planks = new ArrayList<>();
        bindings = new ArrayList<>();
        forces = new ArrayList<>();
        bindingsAmount = new int[BindingType.values().length];
        bindingLimits = new int[BindingType.values().length][3];
        bindingLimits[BindingType.FIXED.ordinal()] = new int[]{1, 3, 0};
        bindingLimits[BindingType.MOVABLE.ordinal()] = new int[]{1, 0, 0};
        bindingLimits[BindingType.STATIC.ordinal()] = new int[]{0, 0, 1};
    }

    private void updateBindingsNumber(int diff, BindingType type) {
        int index = type.ordinal();
        bindingsAmount[index] = MathUtils.clamp(0, bindingsAmount[index] + diff, bindings.size(), true);
    }

    private void addPlank(Plank plank) {
        assert plank != null;
        planks.add(plank);
    }

    private void addForce(Force force) {
        assert force != null;
        forces.add(force);
    }

    private boolean addBinding(Binding binding) {
        assert binding != null;
        if (getAvailableBindingCount((BindingType) binding.getType()) > 0) {
            bindings.add(binding);
            updateBindingsNumber(1, (BindingType) binding.getType());
            return true;
        }
        return false;
    }

    public void addUnit(ConstructionUnit unit) {
        if (unit instanceof Plank) addPlank((Plank) unit);
        else if (unit instanceof Force) addForce((Force) unit);
        else if (unit instanceof Binding) addBinding((Binding) unit);
        renderables.add(unit);
    }

    private void removePlank(Plank plank) {
        assert plank != null;
        planks.remove(plank);
    }

    private void removeForce(Force force) {
        assert force != null;
        forces.remove(force);
    }

    private void removeBinding(Binding binding) {
        assert binding != null;
        bindings.remove(binding);
        updateBindingsNumber(-1, (BindingType) binding.getType());
    }

    public void removeUnit(ConstructionUnit unit) {
        if (unit instanceof Plank) removePlank((Plank) unit);
        else if (unit instanceof Force) removeForce((Force) unit);
        else if (unit instanceof Binding) removeBinding((Binding) unit);
        renderables.remove(unit);
    }

    public ArrayList<Force> getForces() {
        return forces;
    }

    public ArrayList<Binding> getBindings() {
        return bindings;
    }

    public ArrayList<Plank> getPlanks() {
        return planks;
    }

    public ArrayList<ConstructionUnit> getUnits() {
        ArrayList<ConstructionUnit> units = new ArrayList<>();
        units.addAll(forces);
        units.addAll(bindings);
        units.addAll(planks);
        return units;
    }

    public CopyOnWriteArrayList<Renderable> getRenderables() {
        return renderables;
    }

    /**
     * Gets maximum number of available bindings
     */
    public int getAvailableBindingCount(BindingType bindingType) {
        int index = bindingType.ordinal();
        int res = 0;
        int accepted; // accepted limit for this type
        boolean found;
        for (int i = 0; i < bindingLimits[index].length; ++i) { // iterate through all rules for inspected type
            accepted = 0;
            found = true;
            if (bindingsAmount[index] >= bindingLimits[index][i])
                continue; // if in current rule amount is greater than limit skip it..
            for (int j = 0; j < bindingLimits.length; ++j) { // iterate through rules for all types
                if (j == index) continue;   // except that which we are inspecting
                if (bindingsAmount[j] > bindingLimits[j][i]) { // if there is maximum number for this rule skip it..
                    found = false;
                    break;
                }
            }
            if (found)
                accepted = bindingLimits[index][i];
            if (res <= accepted) res = accepted;
        }
        return res - bindingsAmount[index]; // number of left planks
    }

    public boolean canSolve() {
        boolean found;
        for (int j = 0; j < bindingLimits[0].length; j++) {
            found = true;
            for (int i = 0; i < bindingsAmount.length; i++) {
                if (bindingsAmount[i] != bindingLimits[i][j]) {
                    found = false;
                    break;
                }
            }
            if (found) return true;
        }
        return false;
    }

    @Override
    public Construction clone() {
        Construction c = new Construction();
        c.planks = copyArray(this.planks);
        c.bindings = copyArray(this.bindings);
        c.forces = copyArray(this.forces);
        return c;
    }

    private <T> ArrayList<T> copyArray(ArrayList<T> list) {
        ArrayList<T> c = new ArrayList<>();
        for (T t : list)
            c.add(t);
        return c;
    }

    @Override
    public Iterator<ConstructionUnit> iterator() {
        return getUnits().iterator();
    }

    public void clear() {
        renderables.clear();
        planks.clear();
        forces.clear();
        bindings.clear();
        bindingsAmount = new int[BindingType.values().length];
    }
}
