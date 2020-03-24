package Group6;

import Group6.Geometry.*;
import Group6.Geometry.Collection.Points;
import SimpleUnitTest.SimpleUnitTest;

public class ExtendedUnitTest extends SimpleUnitTest {

    /**
     * This method allows to assert that certain condition is true.
     *
     * @param condition The condition that must be true.
     * @param explanation Explanation of the assertion.
     *
     * @throws AssertionFailed This exception with explanation of the assertion is thrown if condition is false.
     */
    protected static void assertFalse(boolean condition, String explanation) throws RuntimeException {
        if(condition) throw new AssertionFailed("Assertion failed: \n" + explanation);
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(LineSegment actual, LineSegment expected) throws RuntimeException {
        assertEqual(actual.getA(), expected.getA(), "Point A must be equal.");
        assertEqual(actual.getB(), expected.getB(), "Point B must be equal.");
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(Vector actual, Vector expected, String explanation) throws RuntimeException {
        if(!actual.isEqualTo(expected, Tolerance.epsilon)) throw new AssertionFailed(
            "Assertion Failed! Two points are not equal! " + "\n" + explanation + "\n\n" +
                "Actual X:   \t" + actual.getX() + "\n" +
                "Expected X: \t" + expected.getX() + "\n\n" +
                "Actual Y:   \t" + actual.getY() + "\n" +
                "Expected Y: \t" + expected.getY()
        );
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(Points actual, Points expected) throws RuntimeException {
        if(actual.getAll().size() != expected.getAll().size()) throw new AssertionFailed(
            "Assertion Failed! Two points collection are not equal size!\n" +
              "Actual size: " + actual.getAll().size() + "\n" +
              "Expected size: " + expected.getAll().size() + "\n\n" +
              "Actual: " + actual + "\n" +
              "Expected: " + expected + "\n"
        );
        for(Point expectedPoint: expected.getAll()) {
            assertEqual(actual.getClosest(expectedPoint), expectedPoint);
        }
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(Point actual, Point expected) throws RuntimeException {
        if(!actual.isEqualTo(expected)) throw new AssertionFailed(
            "Assertion Failed! Two points are not equal! " + "\n\n" +
            "Actual X:   \t" + actual.getX() + "\n" +
            "Expected X: \t" + expected.getX() + "\n\n" +
            "Actual Y:   \t" + actual.getY() + "\n" +
            "Expected Y: \t" + expected.getY()
        );
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(double actual, double expected, String explanation) throws RuntimeException {
        assertEqual(actual, expected, Tolerance.epsilon, explanation);
    }


    /**
     * Exception indicating failed assertion.
     */
    static private class AssertionFailed extends RuntimeException {
        public AssertionFailed() {
        }
        public AssertionFailed(String s) {
            super(s);
        }
    }

}
