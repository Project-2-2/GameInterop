package Group6;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static double randomBetween(double from, double to) {
        return ThreadLocalRandom.current().nextDouble(from, to);
    }
}
