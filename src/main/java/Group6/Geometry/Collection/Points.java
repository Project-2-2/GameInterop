package Group6.Geometry.Collection;

import Group6.Geometry.Point;
import Group6.Percept.Vision.ObjectPercept;
import Group6.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Tomasz Darmetko
 */
public class Points {

    private Set<Point> points;

    public Points() {
        this.points = new HashSet<>();
    }

    public Points(Point ...points) {
        this(new HashSet<>(Arrays.asList(points)));
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
        if(points.isEmpty()) throw new RuntimeException("No closest point as there are no points in collection!");
        Point closest = null;
        double closestDistance = Double.MAX_VALUE;
        for(Point candidate: points) {
            double candidateDistance = point.getDistance(candidate).getValue();
            if(candidateDistance > closestDistance) continue;
            closest = candidate;
            closestDistance = candidateDistance;
        }
        if(closest == null) throw new RuntimeException("Something went wrong when getting closest point!");
        return closest;
    }

    public Points filter(Predicate<? super Point> predicate) {
        return new Points(points.stream().filter(predicate).collect(Collectors.toSet()));
    }

    public Set<ObjectPercept> toObjectPercepts(ObjectPerceptType perceptType) {
        Set<ObjectPercept> objectPercepts = new HashSet<>();
        for (Point point: points) {
            objectPercepts.add(new ObjectPercept(perceptType, point));
        }
        return objectPercepts;
    }

    public String toString() {
        return "Points{size=" + points.size() + ", points=" + points + "}";
    }

}
