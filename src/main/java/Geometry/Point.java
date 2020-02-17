package Geometry;

import Utils.Require;

/**
 * Represents a point in the 2-dimensional, cartesian coordinate system.
 */
public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        Require.realNumber(x);
        Require.realNumber(y);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
