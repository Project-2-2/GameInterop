package Group6.Percept;

import Group6.WorldState.AgentState;
import Group6.WorldState.Scenario;
import Group6.WorldState.WorldState;
import Interop.Percept.AreaPercepts;

public class AreaPerceptsBuilder {
    public AreaPercepts buildPrecepts(WorldState worldState, AgentState agentState) {
        Scenario scenario = worldState.getScenario();
        return new AreaPercepts(
            scenario.getWindows().isInside(agentState.getLocation()),
            scenario.getDoors().isInside(agentState.getLocation()),
            scenario.getSentryTowers().isInside(agentState.getLocation()),
            agentState.isJustTeleported()
        );
    }
}
