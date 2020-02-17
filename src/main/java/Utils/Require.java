package Utils;

public class Require {
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
