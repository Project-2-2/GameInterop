package Interop.Percept.Vision;

import Interop.Geometry.*;
import Interop.Utils.Require;

/**
 * Represents the visible part (perception) of an object.
 *
 * An agent vision is implemented based on the idea of ray casting.
 * See: https://en.wikipedia.org/wiki/Ray_casting
 *
 * Notice! A ray cast that does not intersect anything should be reported as an empty space.
 * @see ObjectPerceptType#EmptySpace
 *
 * You should also take a look at:
 * @see Interop.Percept.Vision.FieldOfView
 */
public final class ObjectPercept {

    private ObjectPerceptType type;
    private Point point;

    public ObjectPercept(ObjectPerceptType type, Point point) {
        Require.notNull(type);
        Require.notNull(point);
        Require.positive(
            point.getDistanceFromOrigin().getValue(),
            "The distance of percept to an agent must not be negative!\n" +
                "Moreover, the agent can not perceive itself.\n" +
                "Therefore, the distance to a percept must never be 0!"
        );
        this.type = type;
        this.point = point;
    }

    /**
     * The type of a visible object that a ray intersected.
     */
    public ObjectPerceptType getType() {
        return type;
    }

    /**
     * The point of intersection of a ray casted by an agent and a visible object.
     */
    public Point getPoint() {
        return point;
    }

    public String toString() {
        return "ObjectPercept{" +
            "type=" + type +
            ", point=" + point +
            '}';
    }

}
