package Group9;

import Group9.math.Vector2;
import SimpleUnitTest.SimpleUnitTest;

public class Vector2Test extends SimpleUnitTest {

    public static void main(String[] args) {

        it("Vector2::new", Vector2Test::_test_new);
        it("Vector2::normalise", () -> {
            {
                Vector2 vector = new Vector2(0, 0);
                assertEqual(vector.normalise().length(), 1, 0);
            }

            {
                Vector2 vector = new Vector2(1, 5);
                assertEqual(vector.normalise().length(), 1, 1E-9);
            }

            {
                Vector2 vector = new Vector2(-1, 5);
                assertEqual(vector.normalise().length(), 1, 1E-9);
            }

            {
                Vector2 vector = new Vector2(1, -5);
                assertEqual(vector.normalise().length(), 1, 1E-9);
            }

            {
                Vector2 vector = new Vector2(1, 5);
                assertEqual(vector.normalise().length(), 1, 1E-9);
            }
        });
        it("Vector::length", Vector2Test::test_length);

    }

    private static void _test_new() {
        {
            Vector2 vector = new Vector2(0, 0);
            assertEqual(vector.getX(), 0, 0);
            assertEqual(vector.getY(), 0, 0);
        }

        {
            Vector2 vector = new Vector2(1, 5);
            assertEqual(vector.getX(), 1, 0);
            assertEqual(vector.getY(), 5, 0);
        }

        {
            Vector2 vector = new Vector2(-1, 5);
            assertEqual(vector.getX(), -1, 0);
            assertEqual(vector.getY(), 5, 0);
        }

        {
            Vector2 vector = new Vector2(1, -5);
            assertEqual(vector.getX(), 1, 0);
            assertEqual(vector.getY(), -5, 0);
        }

        {
            Vector2 vector = new Vector2(1, 5);
            assertEqual(vector.getX(), 1, 0);
            assertEqual(vector.getY(), 5, 0);
        }
    }

    private static void test_length() {
        {
            Vector2 vector = new Vector2(0, 0);
            assertEqual(vector.length(), 0, 0);
        }

        {
            Vector2 vector = new Vector2(1, 5);
            assertEqual(vector.length(), 5.099019514, 1E-9);
        }

        {
            Vector2 vector = new Vector2(-1, 5);
            assertEqual(vector.length(), 5.099019514, 1E-9);
        }

        {
            Vector2 vector = new Vector2(1, -5);
            assertEqual(vector.length(), 5.099019514, 1E-9);
        }

        {
            Vector2 vector = new Vector2(1, 5);
            assertEqual(vector.length(), 5.099019514, 1E-9);
        }
    }
}
