package Group9.map.area;

import Group9.agent.container.AgentContainer;
import Group9.tree.Container;
import Group9.tree.PointContainer;

public abstract class EffectArea<T> implements Container<PointContainer> {

    private final PointContainer pointContainer;

    public EffectArea(PointContainer pointContainer)
    {
        this.pointContainer = pointContainer;
    }

    abstract public T get(AgentContainer<?> agentContainer);

    @Override
    public PointContainer getContainer() {
        return pointContainer;
    }

}
