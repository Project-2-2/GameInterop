package Utils;

import java.util.Objects;

/**
 * This class allows to efficiently express requirements.
 */
public class Require {
    public static void notNull(Object object) {
        Objects.requireNonNull(object);
    }
    public static void notNegative(double value) {
        if(value < 0) {
            throw new RuntimeException("The value must be not negative! Given: " + value);
        }
    }
    public static void realNumber(double value) {
        if(!Utils.isRealNumber(value)) {
            throw new RuntimeException("The value must be real number! Given: " + value);
        }
    }
}
