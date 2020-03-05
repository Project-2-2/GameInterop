package Group6.Percept;

import Group6.WorldState.AgentState;
import Group6.WorldState.WorldState;
import Interop.Percept.AreaPercepts;

public class AreaPerceptsBuilder {
    public AreaPercepts buildPrecepts(WorldState worldState, AgentState agentState) {
        return new AreaPercepts(
            false,
            false,
            false,
            false
        );
    }
}
