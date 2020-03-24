package Group6.Percept.Vision;
import Group6.Geometry.Distance;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.Geometry.Vector;
import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Object.WorldStateObjects;
import Group6.WorldState.WorldState;
import Interop.Percept.Vision.FieldOfView;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Rays {

    private AgentState agentState;
    private FieldOfView fieldOfView;
    private List<Ray> rays = new ArrayList<>();

    public Rays(AgentState agentState, FieldOfView fieldOfView, int numOfRays) {

        this.agentState = agentState;
        this.fieldOfView = fieldOfView;

        Vector straightRay = new Vector(0, fieldOfView.getRange().getValue())
            .rotate(agentState.getDirection());

        double minAngle = - fieldOfView.getViewAngle().getRadians() / 2;
        double angleBetweenRays = fieldOfView.getViewAngle().getRadians() / numOfRays;

        for(int i = 0; i < numOfRays; i++) {
            rays.add(new Ray(
                agentState.getLocation(),
                straightRay
                    .rotate(minAngle + angleBetweenRays * i)
                    .add(agentState.getLocation())
                    .toPoint()
            ));
        }

    }

    public List<Ray> getAll() {
        return rays;
    }

    public List<LineSegment> toLineSegments() {
        return rays.stream().map(Ray::getLineSegment).collect(Collectors.toList());
    }

    public ObjectPercepts getObjectPercepts(WorldState worldState) {

        Point agentLocation = agentState.getLocation();
        Distance viewRange = new Distance(fieldOfView.getRange());

        WorldStateObjects objectsInRange = worldState
            .getAllObjects()
            .getInRange(agentLocation, viewRange)
            .getWithout(agentState);

        Set<ObjectPercept> objectPerceptSet = new HashSet<>();
        for(Ray ray: rays) {
            objectPerceptSet.addAll(ray.getObjectPercepts(objectsInRange).getAll());
        }

        return new ObjectPercepts(objectPerceptSet)
            .shiftPerspective(agentLocation)
            .getInFieldOfView(fieldOfView);

    }

}


