package Percept.Vision;

import Geometry.*;
import Utils.Require;

import java.util.Objects;

/**
 * Represents the visible part (perception) of an object.
 */
public class ObjectPercept {

    private ObjectPerceptType type;
    private Point point;

    public ObjectPercept(ObjectPerceptType type, Point point) {
        Require.notNull(type);
        Require.notNull(point);
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
