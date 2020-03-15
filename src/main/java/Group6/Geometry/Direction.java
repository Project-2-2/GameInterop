package Group6.Geometry;

import Interop.Geometry.Point;
import Interop.Utils.Utils;

/**
 * Represents a direction angle. This is an angle that indicates direction.
 * Therefore, the angle is limited to the range [0, 2*PI).
 */
public final class Direction extends Angle {

    /**
     * Please, use explicit named constructor:
     * @see #fromRadians
     * @see #fromDegrees
     */
    private Direction(double radians) {
        super(radians);
        if(radians < 0 || radians >= 2 * Math.PI) {
            throw new RuntimeException(
                "The direction angle must be between 0 (including) and 2*PI (excluding) radians!\n" +
                "The angle given in radians: " + radians
            );
        }
    }

    public Direction getChangedBy(Angle angle) {
        return new Direction(Utils.mod(getRadians() + angle.getRadians(), Utils.TAU));
    }

    public static Direction fromRadians(double radians) {
        return new Direction(radians);
    }

    public static Direction fromDegrees(double degrees) {
        return fromRadians(Math.toRadians(degrees));
    }

    public static Direction fromInteropDirection(Direction direction) {
        return fromRadians(direction.getRadians());
    }

    public static Direction fromClockAngle(Point point) {
        return new Direction(Utils.clockAngle(point.getX(), point.getY()));
    }

    public static Direction random() {
        return Direction.fromRadians(Group6.Utils.randomBetween(0, Utils.TAU));
    }

    public Interop.Geometry.Direction toInteropDirection() {
        return Interop.Geometry.Direction.fromRadians(getRadians());
    }

}
