package Group6.Geometry;

import Group6.Geometry.Direction;
import Group6.Geometry.Distance;
import Interop.Utils.Require;

/**
 * Represents a point in the 2-dimensional, cartesian coordinate system.
 */
public class Point extends Vector {

    private double x;
    private double y;

    public Point(double x, double y) {
        super(x, y);
    }

    public Point(Interop.Geometry.Point interopPoint) {
        this(interopPoint.getX(), interopPoint.getY());
    }

    public Point(Vector vector) {
        this(vector.getX(), vector.getY());
    }

    public Direction getClockDirection() {
        return Direction.fromClockAngle(toInteropPoint());
    }

    public Distance getDistance(Point point) {
        return new Distance(getEuclideanDistanceTo(point));
    }

    public Distance getDistanceFromOrigin() {
        return getDistance(new Point(0, 0));
    }

    public Interop.Geometry.Point toInteropPoint() {
        return new Interop.Geometry.Point(x, y);
    }

    public boolean isEqualTo(Vector vector) {
        return super.isEqualTo(vector, Tolerance.epsilon);
    }

    public boolean isEqualTo(Vector vector, double tolerance) {
        return super.isEqualTo(vector, tolerance);
    }

    public String toString() {
        return "Point{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }

}
