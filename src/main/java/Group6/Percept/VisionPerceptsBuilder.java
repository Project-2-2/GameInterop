package Group6.Percept;

import Group6.Geometry.*;
import Group6.Percept.Vision.Rays;
import Group6.WorldState.*;
import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Object.GuardState;
import Group6.WorldState.Object.IntruderState;
import Interop.Percept.AreaPercepts;
import Interop.Percept.Vision.*;

public class VisionPerceptsBuilder {

    public VisionPrecepts buildPercepts(WorldState worldState, AgentState agentState, AreaPercepts areaPercepts) {
        FieldOfView fieldOfView = getFieldOfView(worldState.getScenario(), agentState, areaPercepts);
        return new VisionPrecepts(
            fieldOfView,
            new Rays(
                agentState,
                fieldOfView,
                worldState.getScenario().getViewRays()
            ).getObjectPercepts(worldState.getAllObjects()).toInterop()
        );
    }

    public FieldOfView getFieldOfView(Scenario scenario, AgentState agentState, AreaPercepts areaPercepts) {
        return new FieldOfView(
            getViewRange(scenario, agentState, areaPercepts).toInteropDistance(),
            scenario.getViewAngle().toInteropAngle()
        );
    }

    public Distance getViewRange(Scenario scenario, AgentState agentState, AreaPercepts areaPercepts) {

        Distance normalViewRange;
        Distance shadedViewRange;
        if(agentState instanceof IntruderState) {
            normalViewRange = scenario.getViewRangeIntruderNormal();
            shadedViewRange = scenario.getViewRangeIntruderShaded();
        } else if(agentState instanceof GuardState) {
            normalViewRange = scenario.getViewRangeGuardNormal();
            shadedViewRange = scenario.getViewRangeGuardShaded();
        } else {
            throw new RuntimeException("Unrecognized agent state: " + agentState.getClass().getName());
        }

        if(areaPercepts.isInDoor() || agentState.isInside(scenario.getShadedAreas())) {
            return shadedViewRange;
        }

        if(areaPercepts.isInSentryTower()) {
            return scenario.getViewRangeSentryEnd();
        }

        return normalViewRange;

    }

}
