package Group9.map.area;

import Group9.agent.container.AgentContainer;
import Group9.map.objects.MapObject;
import Group9.tree.Container;
import Group9.tree.PointContainer;

public abstract class EffectArea<T> implements Container<PointContainer> {

    private final MapObject parent;
    private final PointContainer pointContainer;

    public EffectArea(MapObject parent, PointContainer pointContainer)
    {
        this.parent = parent;
        this.pointContainer = pointContainer;
    }

    public MapObject getParent() {
        return parent;
    }

    abstract public T get(AgentContainer<?> agentContainer);

    @Override
    public PointContainer getContainer() {
        return pointContainer;
    }

}
