package Group6.Geometry;
import Group6.WorldState.AgentState;
import Interop.Percept.Vision.FieldOfView;

import java.util.Set;

public class ReyCast {
    private Set<LineSegment> reys;
    private int reyNum = 45;

    public ReyCast(AgentState agentState, FieldOfView fieldofView){

        reys = generateReys(agentState.getLocation(), agentState.getDirection(),reyNum, fieldofView);


    }

    private Set<LineSegment> generateReys(Point location, Direction direction, int reyNum, FieldOfView fieldofView) {
        Angle shift = new Angle(fieldofView.getViewAngle().getRadians()/reyNum);

        Angle initialShift = new Angle(fieldofView.getViewAngle().getDegrees()/2);
        // not sure if it should be minus or plus:
        Direction reyDirection = Direction.fromDegrees(direction.getDegrees()-initialShift.getDegrees());

        for(int i=0; i<reyNum; i++){


        }
        return reys;
    }

    public Set<LineSegment> getReys() {
        return reys;
    }

    public void setReys(Set<LineSegment> reys) {
        reys = reys;
    }
}

