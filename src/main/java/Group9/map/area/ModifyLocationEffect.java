package Group9.map.area;

import Group9.agent.container.AgentContainer;
import Group9.map.objects.MapObject;
import Group9.math.Vector2;
import Group9.tree.PointContainer;

public abstract class ModifyLocationEffect extends EffectArea<Vector2> {

    public ModifyLocationEffect(MapObject parent, PointContainer pointContainer) {
        super(parent, pointContainer);
    }

    @Override
    public abstract Vector2 get(AgentContainer<?> agentContainer);

}
