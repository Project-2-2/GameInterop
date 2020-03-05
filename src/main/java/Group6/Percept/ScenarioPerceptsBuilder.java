package Group6.Percept;

import Group6.WorldState.AgentState;
import Group6.WorldState.WorldState;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.*;

public class ScenarioPerceptsBuilder {

    public ScenarioPercepts buildPercepts(WorldState worldState, AgentState agentState) {
        return new ScenarioPercepts(
            GameMode.CaptureOneIntruder,
            new Distance(1),
            Angle.fromRadians(1),
            new SlowDownModifiers(1,1,1),
            new Distance(1),
            1
        );
    }

    public ScenarioIntruderPercepts buildIntruderPercepts(WorldState worldState, AgentState agentState) {
        return new ScenarioIntruderPercepts(
            buildPercepts(worldState, agentState),
            1,
            new Distance(1),
            new Distance(1),
            1
        );
    }

    public ScenarioGuardPercepts buildGuardPercepts(WorldState worldState, AgentState agentState) {
        return new ScenarioGuardPercepts(
            buildPercepts(worldState, agentState),
            new Distance(1)
        );
    }

}
