package Interop.Utils;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.lang.Double.NaN;

/**
 * Some useful common functions and constants.
 *
 * @author Tomasz Darmetko
 */
public class Utils {

    /**
     * TAU represents full rotation in radians.
     *
     * PI radians is equal to 180 degrees, or half rotation, therefore TAU = 2 * PI;
     *
     * More info: https://tauday.com/tau-manifesto
     */
    public static final double TAU = Math.PI * 2;
    public static final double HALF_PI = Math.PI / 2;

    /**
     * Outputs easy to read string representing double.
     */
    public static String round(double n) {
        if(n == Double.NEGATIVE_INFINITY || n == Double.POSITIVE_INFINITY) {
            return "Infinity";
        } else if(Double.isNaN(n)) {
            return "NaN";
        }
        BigDecimal bd = new BigDecimal(n);
        bd = bd.round(new MathContext(3));
        return bd.toString();
    }

    /**
     * Returns modulo with reminder having sign of the divisor.
     * This is in contrast to the java "%" operator that returns reminder with the sign of the dividend.
     * Java also implements Math.floorMod() that works only for integers.
     *
     * More info: https://en.wikipedia.org/wiki/Modulo_operation
     *
     * @param dividend The number to divide.
     * @param divisor The number to divide by.
     *
     * @return The remainder.
     */
    public static double mod(double dividend, double divisor) {
        if(divisor == 0) return NaN;
        if(divisor == 1 || divisor == -1 || dividend == 0) return 0;
        boolean differentSign = (divisor < 0 && dividend > 0) || (divisor > 0 && dividend < 0);
        double mod = Math.signum(divisor) * Math.abs(differentSign ? dividend % divisor + divisor : dividend % divisor);
        if(mod == divisor) return 0;
        return mod;
    }

    /**
     * This function returns angle in radians between the positive y-axis and the ray to the point (x, y).
     *
     * Think about this as reading an analog clock where handle is pointing towards the point (x, y).
     * This function will return angle in radians between 12 o'clock and the clock handle.
     *
     * The function goes from 0 (including) to 2*PI (excluding) and wraps back to 0.
     *
     * The function returns NaN for point (0, 0).
     *
     * See: https://en.wikipedia.org/wiki/Atan2
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     *
     * @return The angle in radians.
     */
    public static double clockAngle(double x, double y) {
        if(x == 0 && y == 0) return NaN;
        return mod(-Math.atan2(y, x) + HALF_PI, TAU);
    }

    /**
     * See: https://stackoverflow.com/questions/1878907/the-smallest-difference-between-2-angles
     *
     * @param from The staring angle.
     * @param to The end angle.
     *
     * @return The signed distance from the starting angle to the end angle.
     */
    public static double getSignedDistanceBetweenAngles(double from, double to) {
        return Utils.mod(to - from + Math.PI, Utils.TAU) - Math.PI;
    }

    /**
     * @param alpha The one angle.
     * @param beta The other angle.
     * @return The absolute distance from the one angle to the other angle.
     */
    public static double getDistanceBetweenAngles(double alpha, double beta) {
        return Math.abs(getSignedDistanceBetweenAngles(alpha, beta));
    }

    /**
     * Checks whether the given value is a real number.
     *
     * @param value The value to check.
     * @return Whether the given value is a real number.
     */
    public static boolean isRealNumber(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

}
