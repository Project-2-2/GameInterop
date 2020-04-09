package Group9.map.area;

import Group9.agent.container.AgentContainer;

public class NoModify extends EffectArea<Double> {

    public NoModify() {
        super(null, null);
    }

    @Override
    public Double get(AgentContainer<?> agentContainer) {
        return 1D;
    }

}
