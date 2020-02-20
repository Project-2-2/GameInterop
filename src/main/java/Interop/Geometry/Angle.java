package Interop.Geometry;

import Interop.Utils.Utils;

/**
 * Represents an angle.
 */
public class Angle {

    /**
     * The angle in radians.
     */
    private double radians;

    /**
     * Please, use explicit named constructor:
     * @see #fromRadians
     * @see #fromDegrees
     */
    protected Angle(double radians) {
        if(!Utils.isRealNumber(radians)) {
            throw new RuntimeException("Wrong angle! Angle needs to be a real number, but given: " + radians);
        }
        this.radians = radians;
    }

    public static Angle fromRadians(double radians) {
        return new Angle(radians);
    }

    public static Angle fromDegrees(double degrees) {
        return fromRadians(Math.toRadians(degrees));
    }

    public final double getRadians() {
        return radians;
    }

    public final double getDegrees() {
        return Math.toDegrees(radians);
    }

    public final Angle getDistance(Angle angle) {
        return fromRadians(
            Utils.getDistanceBetweenAngles(getRadians(), angle.getRadians())
        );
    }

}
