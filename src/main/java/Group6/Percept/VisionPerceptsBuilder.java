package Group6.Percept;

import Group6.Geometry.*;
import Group6.Geometry.Collection.Quadrilaterals;
import Group6.WorldState.*;
import Interop.Geometry.Angle;
import Interop.Percept.AreaPercepts;
import Interop.Percept.Vision.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class VisionPerceptsBuilder {

    public VisionPrecepts buildPercepts(WorldState worldState, AgentState agentState, AreaPercepts areaPercepts) {
        Set<ObjectPercept> objectPercepts = getReyIntersection(worldState.getScenario(),agentState.getReyCast());
        return new VisionPrecepts(
            getFieldOfView(worldState.getScenario(), agentState, areaPercepts),
            new ObjectPercepts(objectPercepts)
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


    public Set<ObjectPercept> getReyIntersection(Scenario scenario, ReyCast reyCast){
        Set<ObjectPercept> quadrilaterals = Collections.emptySet();
        Set<ObjectPercept> objectPercepts = Collections.emptySet();
        ArrayList<Quadrilaterals> quadrilateralsSet = new ArrayList<>();
        quadrilateralsSet.add(scenario.getWalls());
        quadrilateralsSet.add(scenario.getDoors());
        quadrilateralsSet.add(scenario.getWindows());
        quadrilateralsSet.add(scenario.getSentryTowers());
        quadrilateralsSet.add(scenario.getShadedAreas());
        //loop through each rey
        for(int j=0; j<reyCast.getReys().size(); j++) {
            LineSegment rey = reyCast.getReys().get(j);
            //loop through each quadrilateral type
            for(int n =0; n<quadrilateralsSet.size(); n++) {
                Quadrilaterals quad = quadrilateralsSet.get(n);
                ObjectPerceptType objType;
                //decide the objectType
                switch(n){
                    case 0: objType = ObjectPerceptType.Wall; break;
                    case 1: objType = ObjectPerceptType.Door; break;
                    case 2: objType = ObjectPerceptType.Window; break;
                    case 3: objType = ObjectPerceptType.SentryTower; break;
                    case 4: objType = ObjectPerceptType.ShadedArea;  break;
                    default: objType = ObjectPerceptType.EmptySpace;
                }
                //loop through each quadrilateral
                for (int i = 0; i < quad.getAll().size(); i++) {
                    Quadrilateral tempQuad = quad.getAll().get(i);
                    ArrayList<LineSegment> lineSegments = new ArrayList<>(tempQuad.getAllSides());
                    //loop through quadrilateral's line segments
                    for (int m = 0; m < lineSegments.size(); m++) {
                        //if it interects add the point to the percepts set.
                        if (rey.isIntersecting(lineSegments.get(m))) {
                            Point p = rey.getIntersectionPointWith(lineSegments.get(m));
                            quadrilaterals.add(new ObjectPercept(objType, p.toInteropPoint()));
                        }
                    }
                }
            }
        }
        return quadrilaterals;
    }

}
