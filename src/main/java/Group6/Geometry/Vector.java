package Group6.Geometry;

import Interop.Utils.Require;

public class Vector {

    private double x;
    private double y;

    public Vector() {
    }

    public Vector(double x, double y) {
        Require.realNumber(x, "A vector in cartesian coordinate system must have real coordinates!");
        Require.realNumber(y, "A vector in cartesian coordinate system must have real coordinates!");
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector divide(double scalar) {
        return new Vector(
            x / scalar,
            y / scalar
        );
    }

    public Vector multiply(double scalar) {
        return new Vector(
            x * scalar,
            y * scalar
        );
    }

    public Vector add(Vector vector) {
        return new Vector(
            x + vector.x,
            y + vector.y
        );
    }

    public Vector subtract(Vector vector) {
        return new Vector(
            x - vector.x,
            y - vector.y
        );
    }

    public Vector abs() {
        return new Vector(
            Math.abs(x),
            Math.abs(y)
        );
    }

    public Vector mod(double scalar) {
        return new Vector(
            x % scalar,
            y % scalar
        );
    }

    public Vector rotate(double theta) {
        return this.rotateAround(new Vector(), theta);
    }

    public double dotProduct(Vector vector) {
        return x * vector.x + y * vector.y;
    }

    /**
     * x' = x*cos q - y*sin q
     * y' = x*sin q + y*cos q
     * z' = z
     *
     * https://www.cs.helsinki.fi/group/goa/mallinnus/3dtransf/3drot.html
     */
    public Vector rotateAround(Vector center, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        Vector centered = this.subtract(center);
        Vector rotated = new Vector(
            centered.x * cosTheta - centered.y * sinTheta,
            centered.x * sinTheta + centered.y * cosTheta
        );
        return rotated.add(center);
    }

    public double getLength() {
        return Math.sqrt(
            Math.pow(x, 2.0) +
            Math.pow(y, 2.0)
        );
    }

    public double getEuclideanDistanceTo(Vector vector) {
        return this.subtract(vector).getLength();
    }

    public Vector toUnitVector() {
        double length = getLength();
        if (length == 0) return new Vector();
        return this.divide(length);
    }

    public Point toPoint() {
        return new Point(this);
    }

    public boolean isEqualTo(Vector vector, double tolerance) {
        Vector absDiff = this.subtract(vector).abs();
        return absDiff.x <= tolerance
            && absDiff.y <= tolerance;
    }

    public String toString() {
        return "Vector{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }

}