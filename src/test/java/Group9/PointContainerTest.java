package Group9;

import Group9.math.Vector2;
import Group9.tree.PointContainer;
import SimpleUnitTest.SimpleUnitTest;

public class PointContainerTest extends SimpleUnitTest {

    public static void main(String[] args) {

        it("PointContainer::Polygon::new", PointContainerTest::test_polygon_new);
        it("PointContainer::Polygon::getArea", PointContainerTest::test_polygon_getArea);
        it("PointContainer::Polygon::translate", PointContainerTest::test_polygon_translate);
        it("PointContainer::Polygon::clone", PointContainerTest::test_polygon_clone);
        it("PointContainer::Circle::new", PointContainerTest::test_circle_new);
        it("PointContainer::Circle::translate", PointContainerTest::test_circle_translate);
        it("PointContainer::Circle::clone", PointContainerTest::test_circle_clone);
        it("PointContainer::Line::new", PointContainerTest::test_line_new);
        it("PointContainer::Line::translate", PointContainerTest::test_line_translate);
        it("PointContainer::Line::getNormal", PointContainerTest::test_line_getNormal);
        it("PointContainer::Line::clone", PointContainerTest::test_line_clone);

        // Polygon Circle Line
        it("PointContainer::intersect(Polygon,Polygon)", PointContainerTest::test_intersect_polygon_polygon);
        it("PointContainer::intersect(Polygon,Circle)", PointContainerTest::test_intersect_polygon_circle);
        it("PointContainer::intersect(Polygon,Line)", PointContainerTest::test_intersect_polygon_line);
        it("PointContainer::intersect(Circle,Circle)", PointContainerTest::test_intersect_circle_circle);
        it("PointContainer::intersect(Circle, Line)", PointContainerTest::test_intersect_circle_line);
        it("PointContainer::intersect(Line, Line)", PointContainerTest::test_intersect_line_line);

    }

    private static void test_polygon_new() {
        PointContainer.Polygon polygon = new PointContainer.Polygon(
                new Vector2(-1, -1),
                new Vector2(-1, 1),
                new Vector2(1, 1),
                new Vector2(1, -1)
        );

        assertEqual(polygon.getPoints().length, 4, 0);
        assertTrue(polygon.getPoints()[0].getX() == -1 && polygon.getPoints()[0].getY() == -1);
        assertTrue(polygon.getPoints()[1].getX() == -1 && polygon.getPoints()[1].getY() == 1);
        assertTrue(polygon.getPoints()[2].getX() == 1 && polygon.getPoints()[2].getY() == 1);
        assertTrue(polygon.getPoints()[3].getX() == 1 && polygon.getPoints()[3].getY() == -1);
    }

    private static void test_polygon_getArea() {
        PointContainer.Polygon polygon = new PointContainer.Polygon(
                new Vector2(-1, -1),
                new Vector2(-1, 1),
                new Vector2(1, 1),
                new Vector2(1, -1)
        );

        assertEqual(polygon.getArea(), 4, 0);
    }

    private static void test_polygon_translate() {
        PointContainer.Polygon polygon = new PointContainer.Polygon(
                new Vector2(-1, -1),
                new Vector2(-1, 1),
                new Vector2(1, 1),
                new Vector2(1, -1)
        );
        polygon.translate(new Vector2(4, -4));
        assertTrue(polygon.getPoints()[0].getX() == 3 && polygon.getPoints()[0].getY() == -5);
        assertTrue(polygon.getPoints()[1].getX() == 3 && polygon.getPoints()[1].getY() == -3);
        assertTrue(polygon.getPoints()[2].getX() == 5 && polygon.getPoints()[2].getY() == -3);
        assertTrue(polygon.getPoints()[3].getX() == 5 && polygon.getPoints()[3].getY() == -5);
    }

    private static void test_circle_new() {
        PointContainer.Circle circle = new PointContainer.Circle(
                new Vector2(-2, 2), 4
        );

        assertTrue(circle.getCenter().getX() == -2 && circle.getCenter().getY() == 2);
        assertTrue(circle.getRadius() == 4);
    }

    private static void test_circle_translate() {
        PointContainer.Circle circle = new PointContainer.Circle(
                new Vector2(-2, 2), 4
        );
        circle.translate(new Vector2(4, -4));
        assertTrue(circle.getCenter().getX() == 2 && circle.getCenter().getY() == -2);
    }

    private static void test_line_new() {
        {
            PointContainer.Line line = new PointContainer.Line(
                    new Vector2(-2, 2), new Vector2(2, 2)
            );
            assertEqual(line.getStart().getX(), -2, 0);
            assertEqual(line.getStart().getY(), 2, 0);

            assertEqual(line.getEnd().getX(), 2, 0);
            assertEqual(line.getEnd().getY(), 2, 0);
        }

        {
            PointContainer.Line line = new PointContainer.Line(
                    new Vector2(2, 2), new Vector2(-2, 2)
            );
            assertEqual(line.getStart().getX(), -2, 0);
            assertEqual(line.getStart().getY(), 2, 0);

            assertEqual(line.getEnd().getX(), 2, 0);
            assertEqual(line.getEnd().getY(), 2, 0);
        }
    }

    private static void test_line_translate() {
        PointContainer.Line line = new PointContainer.Line(
                new Vector2(-2, 2), new Vector2(2, 2)
        );
        line.translate(new Vector2(-5, 5));
        assertEqual(line.getStart().getX(), -7, 0);
        assertEqual(line.getStart().getY(), 7, 0);

        assertEqual(line.getEnd().getX(), -3, 0);
        assertEqual(line.getEnd().getY(), 7, 0);
    }

    private static void test_line_getNormal() {
        Vector2 line = new PointContainer.Line(new Vector2.Origin(), new Vector2(0, 1)).getNormal();
        assertEqual(line.getX(), -1, 0);
        assertEqual(line.getY(), 0, 0);
    }

    private static void test_polygon_clone() {
        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Polygon b = a.clone();

            assertTrue(a != b);
            assertEqual(a.getPoints().length, b.getPoints().length);
            for (Vector2 pa : a.getPoints()) {
                for (Vector2 pb : b.getPoints()) {
                    assertTrue(pa != pb);
                }
            }

        }
    }

    private static void test_circle_clone() {
        PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin(), 2);

        PointContainer.Circle b = a.clone();

        assertTrue(a != b);
        assertTrue(a.getCenter() != b.getCenter());
    }

    private static void test_line_clone() {
        PointContainer.Line a = new PointContainer.Line(new Vector2(-2, 2), new Vector2(2, 2));

        PointContainer.Line b = a.clone();

        assertTrue(a != b);
        assertTrue(a.getStart() != b.getStart() && a.getStart() != b.getEnd());
        assertTrue(a.getEnd() != b.getStart() && a.getEnd() != b.getEnd());
    }

    private static void test_intersect_polygon_polygon() {
        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Polygon b = a.clone();
            b.translate(new Vector2(0.5, 0.5));

            assertTrue(PointContainer.intersect(a, b));

        }

        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Polygon b = a.clone();
            b.translate(new Vector2(1.1, 1.1));

            assertTrue(!PointContainer.intersect(a, b));

        }
    }

    private static void test_intersect_polygon_circle() {
        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 10);
            assertTrue(PointContainer.intersect(a, b));

        }

        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 0.3);
            assertTrue(PointContainer.intersect(a, b));

        }

        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 0.5);
            b.translate(new Vector2(-0.49, -0.49));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 0.5);
            b.translate(new Vector2(-0.51, -0.51));
            PointContainer.intersect(a, b);
            assertTrue(!PointContainer.intersect(a, b));
        }
    }

    private static void test_intersect_polygon_line() {
        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Line b = new PointContainer.Line(new Vector2(-0.5, -0.5), new Vector2(0.5, 0.5));
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Line b = new PointContainer.Line(new Vector2(0.25, 0.25), new Vector2(0.5, 0.5));
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Polygon a = new PointContainer.Polygon(
                    new Vector2.Origin(), new Vector2(0, 1), new Vector2(1, 1), new Vector2(1, 0)
            );

            PointContainer.Line b = new PointContainer.Line(new Vector2(-1, 0), new Vector2(-1, 1));
            assertTrue(!PointContainer.intersect(a, b));
        }
    }

    private static void test_intersect_circle_circle() {
        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin(), 1);
            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 1);
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin(), 2);
            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 1);
            assertTrue(PointContainer.intersect(a, b));
        }
        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin().sub(-0.5, -0.5), 1);
            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 1);
            assertTrue(PointContainer.intersect(a, b));
        }
        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin().sub(-1, -1), 1);
            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 1);
            assertTrue(!PointContainer.intersect(a, b));
        }
        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin().sub(100, -500), 1);
            PointContainer.Circle b = new PointContainer.Circle(new Vector2.Origin(), 1);
            assertTrue(!PointContainer.intersect(a, b));
        }
    }

    private static void test_intersect_circle_line() {
        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin(), 1);
            PointContainer.Line b = new PointContainer.Line(new Vector2(0.25, 0.25), new Vector2(0.5, 0.5));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin(), 1);
            PointContainer.Line b = new PointContainer.Line(new Vector2(-2, -2), new Vector2(2, 2));
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Circle a = new PointContainer.Circle(new Vector2.Origin(), 1);
            PointContainer.Line b = new PointContainer.Line(new Vector2(-2, -2), new Vector2(-2, 2));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Circle a = new PointContainer.Circle(
                    new Vector2(10.071979770056398, 118.34813766886532), 0.5
            );

            PointContainer.Line b = new PointContainer.Line(
                    new Vector2(10, 77), new Vector2(10, 67)
            );

            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Circle a = new PointContainer.Circle(
                    new Vector2(2, 2), 1
            );

            PointContainer.Line b = new PointContainer.Line(
                    new Vector2(-2, 2), new Vector2(4, 2)
            );

            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Circle a = new PointContainer.Circle(
                    new Vector2(2, 2), 1
            );

            PointContainer.Line b = new PointContainer.Line(
                    new Vector2(2, 0), new Vector2(2, 4)
            );

            assertTrue(PointContainer.intersect(a, b));
        }
    }

    private static void test_intersect_line_line() {
        {
            PointContainer.Line a = new PointContainer.Line(new Vector2(-1, 0), new Vector2(1, 0));
            PointContainer.Line b = new PointContainer.Line(new Vector2(0, -1), new Vector2(0, 1));
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2.Origin(), new Vector2(4, 4));
            PointContainer.Line b = new PointContainer.Line(new Vector2(2, 2), new Vector2(5, 3));
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2(-2, -2), new Vector2(2, 2));
            PointContainer.Line b = new PointContainer.Line(new Vector2(-2, 0), new Vector2.Origin());
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2(0, 4), new Vector2(4, 4));
            PointContainer.Line b = new PointContainer.Line(new Vector2(4, 0), new Vector2(4, 8));
            assertTrue(PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2.Origin(), new Vector2(6, 6));
            PointContainer.Line b = new PointContainer.Line(new Vector2(2, 2), new Vector2(4, 4));
            assertTrue(PointContainer.intersect(a, b));
        }

        // --- @regression: This bug was caused by not allowing a proper delta in the >= and <= comparision. To fix this bug
        //      I added the geq() and leq() methods to the PointContainer class for comparisions.
        {
            PointContainer.Line a = new PointContainer.Line(new Vector2(59.43349636341895,14.333224990404418), new Vector2(61.318334753821354, 14.093674907821876));
            PointContainer.Line b = new PointContainer.Line(new Vector2(60.0,12.0), new Vector2(60.0,36.0));

            PointContainer.intersect(a, b);
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2.Origin(), new Vector2(6, 6));
            PointContainer.Line b = new PointContainer.Line(new Vector2(0, 2), new Vector2(4, 6));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2.Origin(), new Vector2(-4, 6));
            PointContainer.Line b = new PointContainer.Line(new Vector2(-4, 2), new Vector2(-8, 8));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2.Origin(), new Vector2(0, 2));
            PointContainer.Line b = new PointContainer.Line(new Vector2(4, 4), new Vector2(6, 4));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2(-2, -2), new Vector2(4, 4));
            PointContainer.Line b = new PointContainer.Line(new Vector2(6, 6), new Vector2(10, 10));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2.Origin(), new Vector2(2, 2));
            PointContainer.Line b = new PointContainer.Line(new Vector2(4, 0), new Vector2(1, 4));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2(2, 2), new Vector2(8, 2));
            PointContainer.Line b = new PointContainer.Line(new Vector2(4, 4), new Vector2(6, 4));
            assertTrue(!PointContainer.intersect(a, b));
        }

        {
            PointContainer.Line a = new PointContainer.Line(new Vector2(0, 8), new Vector2(10, 0));
            PointContainer.Line b = new PointContainer.Line(new Vector2(4, 2), new Vector2(4, 4));
            assertTrue(!PointContainer.intersect(a, b));
        }

    }
}
