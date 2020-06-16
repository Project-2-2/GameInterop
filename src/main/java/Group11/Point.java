package Group11;

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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Direction getClockDirection() {
        return Direction.fromClockAngle(this.toPoint());
    }

    public Distance getDistance(Point point) {
        return new Distance(this.toPoint(), point.toPoint());
    }

    public Distance getDistanceFromOrigin() {
        return getDistance(new Point(0, 0));
    }

    public void setX(double x){ this.x=x;}

    public void setY(double y){ this.y=y;}

    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    public Interop.Geometry.Point toPoint(){
        return new Interop.Geometry.Point(this.x,this.y);
    }

}