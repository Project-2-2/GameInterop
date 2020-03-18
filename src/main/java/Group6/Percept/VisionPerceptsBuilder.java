package Group6.Percept;

import Group6.Geometry.Distance;
import Group6.WorldState.*;
import Interop.Geometry.Angle;
import Interop.Percept.AreaPercepts;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.Collections;

public class VisionPerceptsBuilder {

    public VisionPrecepts buildPercepts(WorldState worldState, AgentState agentState, AreaPercepts areaPercepts) {
        return new VisionPrecepts(
            getFieldOfView(worldState.getScenario(), agentState, areaPercepts),
            new ObjectPercepts(Collections.emptySet())
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
