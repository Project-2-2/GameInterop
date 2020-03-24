package Group6.Geometry.Collection;

import Group6.Geometry.Point;
import Group6.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Points {

    private Set<Point> points;

    public Points() {
        this.points = new HashSet<>();
    }

    public Points(Set<Point> points) {
        this.points = Collections.unmodifiableSet(points);
    }

    public Set<Point> getAll() {
        return points;
    }

    public boolean hasPoints() {
        return !points.isEmpty();
    }

    public Point getClosest(Point point) {
        Point closest = null;
        double closestDistance = Double.MAX_VALUE;
        for(Point candidate: points) {
            double candidateDistance = point.getDistance(candidate).getValue();
            if(candidateDistance < closestDistance) continue;
            closest = candidate;
            closestDistance = candidateDistance;
        }
        if(closest == null) throw new RuntimeException("No closest point as there are no points in collection!");
        return closest;
    }

    public Set<ObjectPercept> toObjectPercepts(ObjectPerceptType perceptType) {
        Set<ObjectPercept> objectPercepts = new HashSet<>();
        for (Point point: points) {
            objectPercepts.add(new ObjectPercept(perceptType, point));
        }
        return objectPercepts;
    }

}
