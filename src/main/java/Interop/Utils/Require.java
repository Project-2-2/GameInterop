package Interop.Utils;

import java.util.Objects;

/**
 * This class allows to efficiently express requirements.
 *
 * @author Tomasz Darmetko
 */
public class Require {
    public static void notNull(Object object, String explanation) {
        Objects.requireNonNull(object, explanation);
    }
    public static void notNull(Object object) {
        Objects.requireNonNull(object);
    }
    public static void positive(double value, String explanation) {
        if(value <= 0) {
            throw new RuntimeException(
                "The value must be positive! Given: " + value + "\n" + explanation
            );
        }
    }
    public static void notNegative(double value, String explanation) {
        if(value < 0) {
            throw new RuntimeException(
                "The value must be not negative! Given: " + value + "\n" + explanation
            );
        }
    }
    public static void realNumber(double value, String explanation) {
        if(!Utils.isRealNumber(value)) {
            throw new RuntimeException(
                "The value must be real number! Given: " + value + "\n" + explanation
            );
        }
    }
}
