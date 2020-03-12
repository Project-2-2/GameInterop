package Group9.map.area;

import Group9.agent.container.AgentContainer;
import Group9.math.Vector2;
import Group9.tree.PointContainer;

public abstract class ModifyLocationEffect extends EffectArea<Vector2> {


    public ModifyLocationEffect(PointContainer pointContainer) {
        super(pointContainer);
    }

    @Override
    public abstract Vector2 get(AgentContainer<?> agentContainer);

}
