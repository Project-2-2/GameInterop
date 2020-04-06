package Group6;

import Group6.Geometry.*;
import Group6.Geometry.Collection.Points;
import Group6.Percept.Vision.ObjectPercept;
import Group6.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.ObjectPerceptType;
import SimpleUnitTest.SimpleUnitTest;

/**
 * @author Tomasz Darmetko
 */
public class ExtendedUnitTest extends SimpleUnitTest {

    public static int assertions = 0;

    /**
     * This method allows to assert that certain condition is true.
     *
     * @param condition The condition that must be true.
     * @param explanation Explanation of the assertion.
     *
     * @throws AssertionFailed This exception with explanation of the assertion is thrown if condition is false.
     */
    protected static void assertFalse(boolean condition, String explanation) throws RuntimeException {
        assertions++;
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
        assertions++;
        if(!actual.isEqualTo(expected, Tolerance.epsilon)) throw new AssertionFailed(
            "Assertion Failed! Two points are not equal! " + "\n" + explanation + "\n\n" +
                "Actual X:   \t" + actual.getX() + "\n" +
                "Expected X: \t" + expected.getX() + "\n\n" +
                "Actual Y:   \t" + actual.getY() + "\n" +
                "Expected Y: \t" + expected.getY()
        );
    }

    protected static void assertEqual(ObjectPercepts actual, ObjectPercepts expected) throws RuntimeException {
        for (ObjectPerceptType type: expected.getTypes()) { // all expected types
            if(actual.getByType(type).getAll().size() != expected.getByType(type).getAll().size()) {
                assertions++;
                throw new AssertionFailed(
                    "Assertion Failed! The expected amount of percepts of type " + type + " is not equal to the actual amount!\n" +
                        "Type: " + type + "\n" +
                        "Actual size: " + actual.getByType(type).getAll().size() + "\n" +
                        "Expected size: " + expected.getByType(type).getAll().size() + "\n\n" +
                        "Actual: " + actual.getByType(type).getAll() + "\n" +
                        "Expected: " + expected.getByType(type) + "\n"
                );
            }
            try {
                assertEqual(actual.getByType(type).toPoints(), expected.getByType(type).toPoints());
            } catch (AssertionFailed e) {
                throw new AssertionFailed("The actual points of percepts of type " + type + " are not equal to the expected points!", e);
            }
        }
        for (ObjectPerceptType type: actual.getTypes()) { // all actual types - there can be more than expected
            if(actual.getByType(type).getAll().size() != expected.getByType(type).getAll().size()) {
                assertions++;
                throw new AssertionFailed(
                    "Assertion Failed! The actual amount of percepts of type " + type + " is not equal to the expected amount!\n" +
                        "Type: " + type + "\n" +
                        "Actual size: " + actual.getByType(type).getAll().size() + "\n" +
                        "Expected size: " + expected.getByType(type).getAll().size() + "\n\n" +
                        "Actual: " + actual.getByType(type).getAll() + "\n" +
                        "Expected: " + expected.getByType(type) + "\n"
                );
            }
            try {
                assertEqual(actual.getByType(type).toPoints(), expected.getByType(type).toPoints());
            } catch (AssertionFailed e) {
                throw new AssertionFailed("The actual points of percepts of type " + type + " are not equal to the expected points!", e);
            }
        }
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(Points actual, Points expected) throws RuntimeException {
        assertions++;
        if(actual.getAll().size() != expected.getAll().size()) throw new AssertionFailed(
            "Assertion Failed! Two points collection are not equal size!\n" +
              "Actual size: " + actual.getAll().size() + "\n" +
              "Expected size: " + expected.getAll().size() + "\n\n" +
              "Actual: " + actual + "\n" +
              "Expected: " + expected + "\n"
        );
        for(Point actualPoint: actual.getAll()) { // make sure that all actual points have corresponding expected point
            try {
                assertEqual(actualPoint, expected.getClosest(actualPoint));
            } catch (AssertionFailed e) {
                throw new AssertionFailed("The closest expected point to an actual point were not equal!", e);
            }
        }
        for(Point expectedPoint: expected.getAll()) { // make sure that all expected points have corresponding actual point
            try {
                assertEqual(actual.getClosest(expectedPoint), expectedPoint);
            } catch (AssertionFailed e) {
                throw new AssertionFailed("The closest actual point to an expected point were not equal!", e);
            }
        }
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(Point actual, Point expected) throws RuntimeException {
        assertions++;
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

    protected static void assertEqual(double actual, double expected, double tolerance) throws RuntimeException {
        assertions++;
        SimpleUnitTest.assertEqual(actual, expected, tolerance);
    }

    protected static void assertEqual(long actual, long expected) throws RuntimeException {
        assertions++;
        SimpleUnitTest.assertEqual(actual, expected);
    }

    protected static void assertTrue(boolean condition) throws RuntimeException {
        assertions++;
        SimpleUnitTest.assertTrue(condition);
    }

    protected static void assertTrue(boolean condition, String explanation) throws RuntimeException {
        assertions++;
        SimpleUnitTest.assertTrue(condition, explanation);
    }

    protected static void assertInstanceOf(Object object, Class classRepresentation) throws RuntimeException {
        assertions++;
        SimpleUnitTest.assertInstanceOf(object, classRepresentation);
    }

    protected static void assertEqual(double actual, double expected, double tolerance, String explanation) throws RuntimeException {
        assertions++;
        SimpleUnitTest.assertEqual(actual, expected, tolerance, explanation);
    }

    /**
     * Exception indicating failed assertion.
     */
    static private class AssertionFailed extends RuntimeException {
        public AssertionFailed() {
        }
        public AssertionFailed(String s) {
            super("\n" + s);
        }
        public AssertionFailed(String s, AssertionFailed previous) {
            this(s + "\nMore specifically:" + previous.getMessage());
        }
    }

}
