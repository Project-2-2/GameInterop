package Group9;

import Interop.Utils.Utils;

public class PiMath {

    public static double getDistanceBetweenAngles(double from, double to)
    {
        return Utils.mod(to - from + Math.PI, Utils.TAU) - Math.PI;
    }

    public static double clamp(double value, double lower, double upper)
    {
        return Math.max(lower, Math.min(upper, value));
    }

    /**
     * Performs <code>a >= b</code> check, allows 1E-10 delta.
     * @param a
     * @param b
     * @return
     */
    public static boolean geq(double a, double b)
    {
        return (a > b) || Math.abs(a - b) < 1E-10;
    }

    /**
     * Performs <code>a <= b</code> check, allows 1E-10 delta.
     * @param a
     * @param b
     * @return
     */
    public static boolean leq(double a, double b)
    {
        return (a < b) || Math.abs(a - b) < 1E-10;
    }


}
