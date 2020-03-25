package Group6.Geometry;

import Group6.Geometry.Contract.Area;
import Interop.Utils.Utils;

/**
 * Represents a point in the 2-dimensional, cartesian coordinate system.
 *
 * @author Tomasz Darmetko
 */
public class Point extends Vector {

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
        return Direction.fromClockAngle(this);
    }

    public Distance getDistance(Point point) {
        return new Distance(getEuclideanDistanceTo(point));
    }

    public Distance getDistanceFromOrigin() {
        return getDistance(new Point(0, 0));
    }

    public boolean isInside(Area area) {
        return area.hasInside(this);
    }

    public Interop.Geometry.Point toInteropPoint() {
        return new Interop.Geometry.Point(getX(), getY());
    }

    public boolean isEqualTo(Vector vector) {
        return super.isEqualTo(vector, Tolerance.epsilon);
    }

    public boolean isEqualTo(Vector vector, double tolerance) {
        return super.isEqualTo(vector, tolerance);
    }

    public String toString() {
        return "Point{" +
            "x=" + Utils.round(getX()) +
            ", y=" + Utils.round(getY()) +
            '}';
    }

}
