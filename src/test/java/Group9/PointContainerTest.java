package Group9;

import Group9.math.Vector2;
import Group9.tree.PointContainer;
import SimpleUnitTest.SimpleUnitTest;

public class PointContainerTest extends SimpleUnitTest {

    public static void main(String[] args) {

        it("PointContainer::Polygon::new", PointContainerTest::test_polygon_new);
        it("PointContainer::Polygon::getArea", PointContainerTest::test_polygon_getArea);
        it("PointContainer::Polygon::translate", PointContainerTest::test_polygon_translate);
        it("PointContainer::Circle::new", PointContainerTest::test_circle_new);
        it("PointContainer::Circle::translate", PointContainerTest::test_translate_new);
        it("PointContainer::Line::new", PointContainerTest::test_line_new);
        it("PointContainer::Line::translate", PointContainerTest::test_line_translate);
        it("PointContainer::Line::getNormal", () -> {});

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

    private static void test_translate_new() {
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
}
