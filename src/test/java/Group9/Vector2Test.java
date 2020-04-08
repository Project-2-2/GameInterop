package Group9;

import Group9.math.Vector2;
import Interop.Geometry.Point;
import SimpleUnitTest.SimpleUnitTest;

public class Vector2Test extends SimpleUnitTest {

    public static void main(String[] args) {

        it("Vector2::new", Vector2Test::test_new);
        it("Vector2::Origin::new", Vector2Test::test_origin_new);
        it("Vector2::normalise", Vector2Test::test_normalise);
        it("Vector::length", Vector2Test::test_length);
        it("Vector2::mul", Vector2Test::test_mul);
        it("Vector2::flip", Vector2Test::test_flip);
        it("Vector2::from", Vector2Test::test_from);
        it("Vector2::distance", Vector2Test::test_distance);
        it("Vector2::rotated", Vector2Test::test_rotated);
        it("Vector2::add", Vector2Test::test_add);
        it("Vector2::angle", Vector2Test::test_angle);
        it("Vector2::dot", Vector2Test::test_dot);

    }

    private static void test_new() {
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

    private static void test_normalise() {
        {
            Vector2 vector = new Vector2(0, 0);
            assertEqual(vector.normalise().length(), 0, 0);
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
    }

    private static void test_mul() {
        Vector2 pp = new Vector2(3, 4);
        Vector2 np = new Vector2(-3, 4);
        Vector2 pn = new Vector2(3, -4);
        Vector2 nn = new Vector2(-3, -4);

        // pp * np
        {
            Vector2 r1 = pp.mul(np);
            Vector2 r2 = pp.mul(np.getX(), np.getY());

            assertTrue(r1.getX() == -9 && r1.getY() == 16, "3 * (-3) && 4 * 4");
            assertTrue(r2.getX() == -9 && r2.getY() == 16, "3 * (-3) && 4 * 4");
        }

        // pp * pn
        {
            Vector2 r1 = pp.mul(pn);
            Vector2 r2 = pp.mul(pn.getX(), pn.getY());

            assertTrue(r1.getX() == 9 && r1.getY() == -16, "3 * 3 && 4 * (-4)");
            assertTrue(r2.getX() == 9 && r2.getY() == -16, "3 * 3 && 4 * (-4)");
        }

        // pp * nn
        {
            Vector2 r1 = pp.mul(nn);
            Vector2 r2 = pp.mul(nn.getX(), nn.getY());

            assertTrue(r1.getX() == -9 && r1.getY() == -16, "3 * (-3) && 4 * (-4)");
            assertTrue(r2.getX() == -9 && r2.getY() == -16, "3 * (-3) && 4 * (-4)");
        }

        // np * pn
        {
            Vector2 r1 = np.mul(pn);
            Vector2 r2 = np.mul(pn.getX(), pn.getY());

            assertTrue(r1.getX() == -9 && r1.getY() == -16, "-3 * 3 && 4 * (-4)");
            assertTrue(r2.getX() == -9 && r2.getY() == -16, "-3 * 3 && 4 * (-4)");
        }

        // np * nn
        {
            Vector2 r1 = np.mul(nn);
            Vector2 r2 = np.mul(nn.getX(), nn.getY());

            assertTrue(r1.getX() == 9 && r1.getY() == -16, "-3 * (-3) && 4 * (-4)");
            assertTrue(r2.getX() == 9 && r2.getY() == -16, "-3 * (-3) && 4 * (-4)");
        }

        // pn * nn
        {
            Vector2 r1 = pn.mul(nn);
            Vector2 r2 = pn.mul(nn.getX(), nn.getY());

            assertTrue(r1.getX() == -9 && r1.getY() == 16, "3 * (-3) && -4 * (-4)");
            assertTrue(r2.getX() == -9 && r2.getY() == 16, "3 * (-3) && -4 * (-4)");
        }
    }

    private static void test_flip() {
        {
            Vector2 flipped = new Vector2(0, 0).flip();
            assertEqual(flipped.getX(), 0, 0);
            assertEqual(flipped.getY(), 0, 0);
        }

        {
            Vector2 flipped = new Vector2(1, 1).flip();
            assertEqual(flipped.getX(), -1, 0);
            assertEqual(flipped.getY(), -1, 0);
        }

        {
            Vector2 flipped = new Vector2(-1, 1).flip();
            assertEqual(flipped.getX(), 1, 0);
            assertEqual(flipped.getY(), -1, 0);
        }

        {
            Vector2 flipped = new Vector2(1, -1).flip();
            assertEqual(flipped.getX(), -1, 0);
            assertEqual(flipped.getY(), 1, 0);
        }
    }

    private static void test_from() {
        {
            Point point = new Point(0, 0);
            Vector2 vector = Vector2.from(point);
            assertEqual(vector.getX(), 0, 0);
            assertEqual(vector.getY(), 0, 0);
        }

        {
            Point point = new Point(1, 2);
            Vector2 vector = Vector2.from(point);
            assertEqual(vector.getX(), 1, 0);
            assertEqual(vector.getY(), 2, 0);
        }

        {
            Point point = new Point(-1, 2);
            Vector2 vector = Vector2.from(point);
            assertEqual(vector.getX(), -1, 0);
            assertEqual(vector.getY(), 2, 0);
        }

        {
            Point point = new Point(1, -2);
            Vector2 vector = Vector2.from(point);
            assertEqual(vector.getX(), 1, 0);
            assertEqual(vector.getY(), -2, 0);
        }
    }

    private static void test_distance() {
        {
            Vector2 start = new Vector2(3, 4);
            Vector2 end = new Vector2(3, 4);

            assertEqual(start.distance(end), 0, 0);
        }

        {
            Vector2 start = new Vector2(0, 0);
            Vector2 end = new Vector2(3, 4);

            assertEqual(start.distance(end), 5, 0);
        }

        {
            Vector2 start = new Vector2(0, 0);
            Vector2 end = new Vector2(-3, -4);

            assertEqual(start.distance(end), 5, 0);
        }

        {
            Vector2 start = new Vector2(0, 0);
            Vector2 end = new Vector2(-3, 4);

            assertEqual(start.distance(end), 5, 0);
        }

        {
            Vector2 start = new Vector2(0, 0);
            Vector2 end = new Vector2(3, -4);

            assertEqual(start.distance(end), 5, 0);
        }
    }

    private static void test_rotated() {
        {
            Vector2 rotated = new Vector2(0, 1).rotated(Math.PI / 2);
            assertEqual(rotated.getX(), -1, 1E-9);
            assertEqual(rotated.getY(), 0, 1E-9);
        }

        {
            Vector2 rotated = new Vector2(0, 1).rotated(Math.PI);
            assertEqual(rotated.getX(), 0, 1E-9);
            assertEqual(rotated.getY(), -1, 1E-9);
        }

        {
            Vector2 rotated = new Vector2(0, 1).rotated(Math.PI * 1.5);
            assertEqual(rotated.getX(), 1, 1E-9);
            assertEqual(rotated.getY(), 0, 1E-9);
        }

        {
            Vector2 rotated = new Vector2(0, 1).rotated(Math.PI * 2);
            assertEqual(rotated.getX(), 0, 1E-9);
            assertEqual(rotated.getY(), 1, 1E-9);
        }
    }

    private static void test_add() {
        {
            Vector2 a = new Vector2(3, 5);

            Vector2 r1 = a.add(new Vector2(2, 6));
            Vector2 r2 = a.add(2, 6);

            assertEqual(r1.getX(), 5, 0);
            assertEqual(r1.getY(), 11, 0);

            assertEqual(r2.getX(), 5, 0);
            assertEqual(r2.getY(), 11, 0);
        }

        {
            Vector2 a = new Vector2(-3, -5);

            Vector2 r1 = a.add(new Vector2(2, 6));
            Vector2 r2 = a.add(2, 6);

            assertEqual(r1.getX(), -1, 0);
            assertEqual(r1.getY(), 1, 0);

            assertEqual(r2.getX(), -1, 0);
            assertEqual(r2.getY(), 1, 0);
        }
    }

    private static void test_angle() {
        assertEqual(new Vector2(1, 0).angle(new Vector2(0, 1)), Math.PI / 2, 1E-9);
        assertEqual(new Vector2(1, 0).angle(new Vector2(-1, 0)), Math.PI, 1E-9);
        assertEqual(new Vector2(1, 0).angle(new Vector2(0, -1)), -Math.PI / 2, 1E-9);
        assertEqual(new Vector2(1, 0).angle(new Vector2(1, 0)), 0, 1E-9);
    }

    private static void test_dot() {
        assertEqual(new Vector2.Origin().dot(new Vector2(5, 2)), 0, 0);

        assertEqual(new Vector2(3, 4).dot(new Vector2(5, 2)), 23, 0);
        assertEqual(new Vector2(3, 4).dot(new Vector2(-5, 2)), -7, 0);
        assertEqual(new Vector2(3, 4).dot(new Vector2(5, -2)), 7, 0);
        assertEqual(new Vector2(3, 4).dot(new Vector2(-5, -2)), -23, 0);

        assertEqual(new Vector2(-3, 4).dot(new Vector2(5, 2)), -7, 0);
        assertEqual(new Vector2(-3, 4).dot(new Vector2(-5, 2)), 23, 0);
        assertEqual(new Vector2(-3, 4).dot(new Vector2(5, -2)), -23, 0);
        assertEqual(new Vector2(-3, 4).dot(new Vector2(-5, -2)), 7, 0);

        assertEqual(new Vector2(3, -4).dot(new Vector2(5, 2)), 7, 0);
        assertEqual(new Vector2(3, -4).dot(new Vector2(-5, 2)), -23, 0);
        assertEqual(new Vector2(3, -4).dot(new Vector2(5, -2)), 23, 0);
        assertEqual(new Vector2(3, -4).dot(new Vector2(-5, -2)), -7, 0);

        assertEqual(new Vector2(-3, -4).dot(new Vector2(5, 2)), -23, 0);
        assertEqual(new Vector2(-3, -4).dot(new Vector2(-5, 2)), 7, 0);
        assertEqual(new Vector2(-3, -4).dot(new Vector2(5, -2)), -7, 0);
        assertEqual(new Vector2(-3, -4).dot(new Vector2(-5, -2)), 23, 0);
    }

    private static void test_origin_new() {
        Vector2 a = new Vector2.Origin();
        assertEqual(a.getX(), 0, 0);
        assertEqual(a.getY(), 0, 0);
    }
}
