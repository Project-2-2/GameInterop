package Percept.Smell;

import Geometry.Angle;
import Geometry.Direction;

/**
 * Represents perception by a smell.
 */
public class SmellPercept {

    private SmellPerceptType type;
    private Intensity intensity;
    private Direction direction;

    public SmellPercept(SmellPerceptType type, Intensity intensity, Direction direction) {
        this.type = type;
        this.intensity = intensity;
        this.direction = direction;
    }

    public SmellPerceptType getType() {
        return type;
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public Direction getDirection() {
        return direction;
    }

}
