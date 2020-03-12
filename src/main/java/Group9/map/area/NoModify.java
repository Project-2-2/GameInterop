package Group9.map.area;

import Group9.agent.AgentContainer;
import Group9.tree.PointContainer;

public class NoModify extends EffectArea {

    public NoModify() {
        super(null);
    }

    @Override
    public double get(AgentContainer<?> agentContainer) {
        return 1;
    }

}
