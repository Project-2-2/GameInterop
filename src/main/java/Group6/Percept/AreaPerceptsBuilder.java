package Group6.Percept;

import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Scenario;
import Group6.WorldState.WorldState;
import Interop.Percept.AreaPercepts;

public class AreaPerceptsBuilder {
    public AreaPercepts buildPrecepts(WorldState worldState, AgentState agentState) {
        Scenario scenario = worldState.getScenario();
        return new AreaPercepts(
            agentState.isInside(scenario.getWindows()),
            agentState.isInside(scenario.getDoors()),
            agentState.isInside(scenario.getSentryTowers()),
            agentState.isJustTeleported()
        );
    }
}
