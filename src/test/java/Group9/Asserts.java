package Group9;

import SimpleUnitTest.SimpleUnitTest;

public class Asserts extends SimpleUnitTest {

    public static void assertException(Runnable method, Class<? extends Exception> exception)
    {
        try {
            method.run();
            assertTrue(false);
        } catch (Exception expected)
        {
            assertTrue(exception.isAssignableFrom(expected.getClass()));
        }
    }

}
