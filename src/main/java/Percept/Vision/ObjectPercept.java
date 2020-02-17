package Percept.Vision;

import Geometry.*;

import java.util.Objects;

/**
 * Represents the visible part (perception) of an object.
 */
public class ObjectPercept {

    private ObjectPerceptType type;
    private Point point;

    public ObjectPercept(ObjectPerceptType type, Point point) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(point);
        this.type = type;
        this.point = point;
    }

    public ObjectPerceptType getType() {
        return type;
    }

    public Point getPoint() {
        return point;
    }

}
