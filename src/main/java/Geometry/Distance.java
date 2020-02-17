package Geometry;

import Utils.*;

/**
 * Represents an euclidean distance.
 */
public class Distance {

    private double distance;

    public Distance(double distance) {
        Require.realNumber(distance);
        Require.notNegative(distance);
        this.distance = distance;
    }

    public double getValue() {
        return distance;
    }

}
