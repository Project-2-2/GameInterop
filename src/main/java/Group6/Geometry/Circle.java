package Group6.Geometry;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Contract.Area;

public class Circle implements Area {

    private Point center;
    private double radius;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public boolean hasInside(Point point) {
        return center.getDistance(point).getValue() < radius;
    }

    public boolean isInRange(Point point, Distance distance) {
        return center.getDistance(point).getValue() < distance.getValue() + radius;
    }

    public Points getIntersections(LineSegment lineSegment) {
        return lineSegment.getIntersectionPointsWith(this);
    }

}
