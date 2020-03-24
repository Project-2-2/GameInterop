package Interop.Geometry;

import Interop.Utils.Require;

/**
 * Represents a point in the 2-dimensional, cartesian coordinate system.
 */
public final class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        Require.realNumber(x, "A point on cartesian coordinate system must have real coordinates!");
        Require.realNumber(y, "A point on cartesian coordinate system must have real coordinates!");
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Direction getClockDirection() {
        return Direction.fromClockAngle(this);
    }

    public Distance getDistance(Point point) {
        return new Distance(this, point);
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

}
