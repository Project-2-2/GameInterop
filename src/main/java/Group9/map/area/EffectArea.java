package Group9.map.area;

import Group9.agent.AgentContainer;
import Group9.tree.Container;
import Group9.tree.PointContainer;

/**
 * Note: The map file standard is kinda a mess. You sometimes have modifiers, and sometimes you just have absolute
 * values. In order to make everything a lot more straightforward the get method should always return the modifier. So for
 * example if you have the base value 2, and the modified value is 2.5 then get should return 1.25.
 */
public abstract class EffectArea implements Container {

    private final PointContainer pointContainer;

    public EffectArea(PointContainer pointContainer)
    {
        this.pointContainer = pointContainer;
    }

    abstract public double get(AgentContainer<?> agentContainer); //TODO

    @Override
    public PointContainer getContainer() {
        return pointContainer;
    }

}
