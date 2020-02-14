/**
 * Simple unit test framework.
 *
 * Your test class should extend this class.
 * Then you can write your own tests in the main method of your class.
 * A single test should look like this:
 * it("should do this (explanation of what you are testing)", () -> {
 *     boolean testCondition = true;
 *     assertTrue(testCondition, "explanation of your assertion");
 * });
 *
 * Good luck!
 *
 * @author Tomasz Darmetko
 */
abstract class SimpleUnitTest {

    /**
     * This method is just a placeholder explaining to you what to do.
     * You should overwrite it in your own test.
     *
     * @param args No arguments foreseen.
     */
    public static void main(String[] args) {

        it("should do this (explanation of what you are testing)", () -> {
            boolean testCondition = true;
            assertTrue(testCondition, "explanation of your assertion");
        });

        System.out.print("\n\n" +
            "The line above is an example of a running test.\n" +
            "Your test class should extend SimpleUnitTest class.\n" +
            "Then you can write your own tests in the main method.\n" +
            "A single test should look like this:\n" +
            "it(\"should do this (explanation of what you are testing)\", () -> {\n" +
            "    boolean testCondition = true;\n" +
            "    assertTrue(testCondition, \"explanation of your assertion\");\n" +
            "});"
        );

    }

    /**
     * This method allows to display that a single test scenario succeeded or failed.
     *
     * @param expectation here you can explain what is your test testing
     * @param runnable this runnable will be executed; if it throws exception you will be notified
     */
    protected static void it(String expectation, Runnable runnable) {

        try {

            runnable.run();
            System.out.println("✔ It " + expectation);

        } catch(AssertionFailed e) {

            System.out.println("✘ It " + expectation);
            System.out.println();

            if(e.getMessage() != null) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            } else {
                System.out.println("Assertion failed!");
                e.printStackTrace();
            }

            System.out.println();
            System.exit(-1);

        } catch(Exception e) {

            System.out.println("✘ It " + expectation);
            System.out.println();
            System.out.println("Exception thrown: ");
            e.printStackTrace();
            System.exit(-1);

        }

    }

    /**
     * This method allows you to skipp a test that takes to long os is not relevant right now.
     *
     * @param expectation here you can explain what is your test testing
     * @param runnable this runnable will be executed; if it throws exception you will be notified
     */
    protected static void xit(String expectation, Runnable runnable) {
        System.out.println("? (Skipped) It " + expectation);
    }

    /**
     * This method allows to assert that certain condition is true.
     *
     * @param condition The condition that must be true.
     * @param explanation Explanation of the assertion.
     *
     * @throws AssertionFailed This exception with explanation of the assertion is thrown if condition is false.
     */
    protected static void assertTrue(boolean condition, String explanation) throws RuntimeException {
        if(!condition) throw new AssertionFailed("Assertion failed: \n" + explanation);
    }

    /**
     * This method allows to assert that certain condition is true.
     *
     * @param condition The condition that must be true.
     *
     * @throws AssertionFailed This exception is thrown if condition is false.
     */
    protected static void assertTrue(boolean condition) throws RuntimeException {
        if(!condition) throw new AssertionFailed();
    }

    /**
     * This method allows to assert that the given object is an instance of the given class representation.
     *
     * @param object The object to check.
     * @param classRepresentation The class representation.

     * @throws AssertionFailed This exception with explanation of the assertion is thrown if condition is false.
     */
    protected static void assertInstanceOf(Object object, Class classRepresentation) throws RuntimeException {
        assertTrue(
            object != null,
            "The given object is null.\n" +
            "Expected an instance of: " + classRepresentation.getName()
        );
        assertTrue(
            classRepresentation.isInstance(object),
            "The given object should be instance of: " + classRepresentation.getName() + "\n" +
                "The given instance is of class: " + object.getClass().getName()
        );
    }

    /**
     * This methods allows to assert that two longs or ints are equal.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(long actual, long expected) throws RuntimeException {
        assertTrue(
            actual == expected,
            "Two numbers are not equal!\n" +
                "Actual: " + actual + "\n" +
                "Expected: " + expected + "\n"
        );
    }

    /**
     * This methods allows to assert that two longs or ints are equal.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     */
    protected static void assertEqual(long actual, long expected, String explanation) throws RuntimeException {
        assertTrue(
            actual == expected,
            explanation +
                "\nTwo numbers are not equal!\n" +
                "Actual: " + actual + "\n" +
                "Expected: " + expected + "\n"
        );
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     * @param tolerance The allowed tolerance.
     * @param explanation An explanation of the assertion.
     */
    protected static void assertEqual(double actual, double expected, double tolerance, String explanation) throws RuntimeException {
        if(Math.abs(actual - expected) > tolerance) throw new AssertionFailed(
            "Assertion Failed: " + explanation + "\n" +
                "Actual value: \t" + actual + "\n" +
                "Expected value:\t" + expected + "\n" +
                "Tolerance:\t\t" + tolerance + "\n" +
                "Difference:\t\t" + Math.abs(actual - expected)
        );
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     * @param tolerance The allowed tolerance.
     */
    protected static void assertEqual(double actual, double expected, double tolerance) throws RuntimeException {
        assertEqual(actual, expected, tolerance, "");
    }

    /**
     * This methods allows to assert that two doubles are equal with certain tolerance.
     *
     * @param actual The actual value.
     * @param expected The expected value.
     * @param tolerance The allowed tolerance.
     */
    protected static void assertEqual(float[] actual, float[] expected, double tolerance) throws RuntimeException {
        assertEqual(actual.length, expected.length, "The arrays are not equal length!");
        for (int i = 0; i < expected.length; i++) {
            assertEqual(actual[i], expected[i], tolerance, "The value at index " + i + " is wrong!");
        }
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