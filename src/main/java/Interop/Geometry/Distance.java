package Interop.Geometry;

import Interop.Utils.*;

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

    public Distance(double distance) {
        Require.realNumber(distance, "Distance must be real!");
        Require.notNegative(distance, "Distance can not be negative!");
        this.distance = distance;
    }

    public double getValue() {
        return distance;
    }

}
