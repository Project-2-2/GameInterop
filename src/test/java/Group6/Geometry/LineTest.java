package Group6.Geometry;

import Group6.ExtendedUnitTest;
import Group6.Geometry.Collection.Points;

public class LineTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nLine Test\n");

        it("allows to create horizontal line from two points", () -> {

            Line line = new Line(
                new Point(0, 0),
                new Point(1, 0)
            );

            assertEqual(line.getA(), 0, "A param");
            assertEqual(line.getB(), 1, "B param");
            assertEqual(line.getC(), 0, "C param");

            assertEqual(line.getSlope(), 0, "Slope");
            assertEqual(line.getYIntercept(), 0, "Y Intercept");

            assertFalse(line.isVertical(), "Horizontal line");

        });

        it("allows to create vertical line from two points (0,0) and (0,1)", () -> {

            Line line = new Line(
                new Point(0, 0),
                new Point(0, 1)
            );

            assertEqual(line.getA(), -1, "A param");
            assertEqual(line.getB(),  0, "B param");
            assertEqual(line.getC(),  0, "C param");

            assertTrue(line.isVertical(), "Vertical line");

            assertEqual(line.getX(), 0, "line where x = 0");

        });

        it("allows to create vertical line from two points (0,-1) and (0,+1)", () -> {

            Line line = new Line(
                new Point(0, -1),
                new Point(0, +1)
            );

            assertEqual(line.getA(), -2, "A param");
            assertEqual(line.getB(),  0, "B param");
            assertEqual(line.getC(),  0, "C param");

            assertTrue(line.isVertical(), "Vertical line");

            assertEqual(line.getX(), 0, "line where x = 0");

        });

        it("allows to create simple y = x line from two points", () -> {

            Line line = new Line(
                new Point(0, 0),
                new Point(1, 1)
            );

            assertEqual(line.getA(), -1, "A param");
            assertEqual(line.getB(),  1, "B param");
            assertEqual(line.getC(),  0, "C param");

            assertFalse(line.isVertical(), "Diagonal line");

            assertEqual(line.getSlope(), 1, "slope");
            assertEqual(line.getYIntercept(), 0, "y-intercept");

        });

        it("allows to create simple y = x + 1 line from two points", () -> {

            Line line = new Line(
                new Point(0, 1),
                new Point(1, 2)
            );

            assertEqual(line.getA(), -1, "A param");
            assertEqual(line.getB(),  1, "B param");
            assertEqual(line.getC(), -1, "C param");

            assertFalse(line.isVertical(), "Diagonal line");

            assertEqual(line.getSlope(), 1, "slope");
            assertEqual(line.getYIntercept(), 1, "y-intercept");

        });

    }

}
