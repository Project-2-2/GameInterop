package Group6.Geometry;

import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Utils.Require;

/**
 * Represents a point in the 2-dimensional, cartesian coordinate system.
 */
public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        Require.realNumber(x, "A point on cartesian coordinate system must have real coordinates!");
        Require.realNumber(y, "A point on cartesian coordinate system must have real coordinates!");
        this.x = x;
        this.y = y;
    }

    public Point(Interop.Geometry.Point interopPoint) {
        this(interopPoint.getX(), interopPoint.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Direction getClockDirection() {
        return Direction.fromClockAngle(toInteropPoint());
    }

    public Distance getDistance(Point point) {
        return new Distance(toInteropPoint(), point.toInteropPoint());
    }

    public Distance getDistanceFromOrigin() {
        return getDistance(new Point(0, 0));
    }

    public String toString() {
        return "Point{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }

    public Interop.Geometry.Point toInteropPoint() {
        return new Interop.Geometry.Point(x, y);
    }

}
