package Interop.Percept.Smell;

import Interop.Geometry.Distance;

/**
 * Represents perception by a smell.
 */
public final class SmellPercept {

    private SmellPerceptType type;
    private Distance distance;

    public SmellPercept(SmellPerceptType type, Distance distance) {
        this.type = type;
        this.distance = distance;
    }

    public SmellPerceptType getType() {
        return type;
    }

    public Distance getDistance() {
        return distance;
    }

}
