package Group6.Percept;

import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Scenario;
import Group6.WorldState.WorldState;
import Interop.Percept.Scenario.*;

public class ScenarioPerceptsBuilder {

    public ScenarioPercepts buildPercepts(Scenario scenario, AgentState agentState) {
        return new ScenarioPercepts(
            scenario.getGameMode(),
            scenario.getCaptureDistance().toInteropDistance(),
            scenario.getMaxRotationAngle().toInteropAngle(),
            new SlowDownModifiers(
                scenario.getSlowDownModifierWindow(),
                scenario.getSlowDownModifierDoor(),
                scenario.getSlowDownModifierSentryTower()
            ),
            scenario.getRadiusPheromone().toInteropDistance(),
            scenario.getPheromoneCooldown()
        );
    }

    public ScenarioIntruderPercepts buildIntruderPercepts(WorldState worldState, AgentState agentState) {
        Scenario scenario = worldState.getScenario();
        return new ScenarioIntruderPercepts(
            buildPercepts(scenario, agentState),
            scenario.getWinConditionIntruderRounds(),
            scenario.getMaxMoveDistanceIntruder().toInteropDistance(),
            scenario.getMaxSprintDistanceIntruder().toInteropDistance(),
            scenario.getSprintCooldown()
        );
    }

    public ScenarioGuardPercepts buildGuardPercepts(WorldState worldState, AgentState agentState) {
        Scenario scenario = worldState.getScenario();
        return new ScenarioGuardPercepts(
            buildPercepts(scenario, agentState),
            scenario.getMaxMoveDistanceGuard().toInteropDistance()
        );
    }

}
