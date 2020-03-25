package Group6.Percept.Vision;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.WorldState.Contract.Object;
import Group6.WorldState.Object.WorldStateObjects;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomasz Darmetko
 */
public class Ray {

    private Point agentLocation;
    private LineSegment ray;

    public Ray(Point agentLocation, Point rayEnd) {
        this.agentLocation = agentLocation;
        this.ray = new LineSegment(agentLocation, rayEnd);
    }

    public Point getAgentLocation() {
        return agentLocation;
    }

    public LineSegment getLineSegment() {
        return ray;
    }

    public ObjectPercepts getObjectPercepts(WorldStateObjects objects) {

        Set<ObjectPercept> potentialPercepts = new HashSet<>();
        for (Object object: objects.getAll()) {

            Points intersections = object.getIntersections(ray);
            if(!intersections.hasPoints()) continue;
            potentialPercepts.addAll(intersections.toObjectPercepts(object.getType()));

        }

        // if ray does not detect anything then indicate empty space
        if(potentialPercepts.isEmpty()) {
            potentialPercepts.add(new ObjectPercept(
                ObjectPerceptType.EmptySpace,
                ray.getB()
            ));
        }

        return new ObjectPercepts(potentialPercepts).getVisibleInStraightLineFrom(agentLocation);

    }

}
