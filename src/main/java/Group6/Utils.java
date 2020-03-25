package Group6;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Tomasz Darmetko
 */
public class Utils {
    public static double randomBetween(double from, double to) {
        return ThreadLocalRandom.current().nextDouble(from, to);
    }
}
