package Group6.Geometry;

import Interop.Utils.Require;

/**
 * Represents an euclidean distance.
 */
public final class Distance {

    private double distance;

    public Distance(Point pointA, Point pointB) {
        this(Math.sqrt(
            Math.pow(pointA.getX() - pointB.getX(), 2) +
            Math.pow(pointA.getY() - pointB.getY(), 2)
        ));
    }

    public Distance(Interop.Geometry.Distance distance) {
        this(distance.getValue());
    }

    public Distance(double distance) {
        Require.realNumber(distance, "Distance must be real!");
        Require.notNegative(distance, "Distance can not be negative!");
        this.distance = distance;
    }

    public double getValue() {
        return distance;
    }

    public Interop.Geometry.Distance toInteropDistance() {
        return new Interop.Geometry.Distance(distance);
    }

}
