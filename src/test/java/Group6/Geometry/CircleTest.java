package Group6.Geometry;

import Group6.ExtendedUnitTest;
import Group6.Geometry.Collection.Points;

public class CircleTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nCircle Test\n");


        it("allows to get vertical line segment and circle single intersect point", () -> {

            Circle circle = new Circle(new Point(0, 0), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(0, 0),
                new Point(0, 2)
            );

            assertEqual(
                circle.getIntersections(lineSegment),
                new Points(new Point(0, 1))
            );

        });

        it("allows to get vertical line segment and circle 2 intersect points", () -> {

            Circle circle = new Circle(new Point(0, 0), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(0, -2),
                new Point(0, +2)
            );

            assertEqual(
                circle.getIntersections(lineSegment),
                new Points(new Point(0, -1), new Point(0, 1))
            );

        });

        it("allows to get horizontal line segment and circle single intersect point", () -> {

            Circle circle = new Circle(new Point(0, 0), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(0, 0),
                new Point(2, 0)
            );

            assertEqual(
                circle.getIntersections(lineSegment),
                new Points(new Point(1, 0))
            );

        });

        it("allows to get horizontal line segment and circle 2 intersect points", () -> {

            Circle circle = new Circle(new Point(0, 0), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(-2, 0),
                new Point(+2, 0)
            );

            assertEqual(
                circle.getIntersections(lineSegment),
                new Points(new Point(-1, 0), new Point(1, 0))
            );

        });

        it("allows to get single intersect point of a tangent vertical line segment", () -> {

            Circle circle = new Circle(new Point(0, 0), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(1, -1),
                new Point(1, +1)
            );

            assertEqual(
                circle.getIntersections(lineSegment),
                new Points(new Point(1, 0))
            );

        });

        it("allows to get single intersect point of a tangent horizontal line segment", () -> {

            Circle circle = new Circle(new Point(0, 0), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(-1, 1),
                new Point(+1, 1)
            );

            assertEqual(
                circle.getIntersections(lineSegment),
                new Points(new Point(0, 1))
            );

        });

        it("allows to get two intersect points of a diagonal line segment when circle is shifted", () -> {

            Circle circle = new Circle(new Point(1, 1), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(0, 3),
                new Point(3, 0)
            );

            assertEqual(
                circle.getIntersections(lineSegment),
                new Points(new Point(1, 2), new Point(2, 1))
            );

        });

        it("returns no points when line segment does not intersect circle", () -> {

            Circle circle = new Circle(new Point(0, 0), 1);

            LineSegment lineSegment = new LineSegment(
                new Point(0, 0.9),
                new Point(0.9, 0)
            );

            assertEqual(circle.getIntersections(lineSegment), new Points());

        });

    }

}
