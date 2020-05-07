package Group9.map.area;

import Group9.agent.container.AgentContainer;
import Group9.map.ViewRange;
import Group9.map.objects.MapObject;
import Group9.tree.PointContainer;

public class ModifyViewRangeEffect extends EffectArea<ViewRange> {

    private final ViewRange viewRange;

    public ModifyViewRangeEffect(MapObject parent, PointContainer pointContainer, ViewRange viewRange) {
        super(parent, pointContainer);
        this.viewRange = viewRange;
    }

    @Override
    public ViewRange get(AgentContainer<?> agentContainer) {
        return this.viewRange;
    }
}
