package Group6.Geometry;
import Group6.WorldState.AgentState;
import Interop.Percept.Vision.FieldOfView;
import java.lang.Math;

import java.util.*;

public class RayCast {

    public static List<LineSegment> generateRays(AgentState agentState, FieldOfView fieldOfView, int numOfRays) {

        Vector straightRay = new Vector(0, fieldOfView.getRange().getValue()).rotate(agentState.getDirection().getRadians());

        double minAngle = -fieldOfView.getViewAngle().getRadians() / 2;
        double angleBetweenRays = fieldOfView.getViewAngle().getRadians() / numOfRays;

        List<LineSegment> rays = new ArrayList<>();
        for(int i = 0; i < numOfRays; i++) {
            rays.add(new LineSegment(
                agentState.getLocation(),
                straightRay.rotate(minAngle + angleBetweenRays * i).add(agentState.getLocation()).toPoint()
            ));
        }

        return rays;
    }

}


