package Group6.Geometry;

import Group6.ExtendedUnitTest;
import SimpleUnitTest.SimpleUnitTest;

/**
 * @author Tomasz Darmetko
 */
public class LineSegmentTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nLine Segment Test\n");

        it("allows to check if line segment includes point", () -> {

            LineSegment lineSegment = new LineSegment(
                new Point(0, 0),
                new Point(1, 1)
            );

            assertTrue(lineSegment.includes(new Point(0,0)));
            assertTrue(lineSegment.includes(new Point(0.1,0.1)));
            assertTrue(lineSegment.includes(new Point(0.5,0.5)));
            assertTrue(lineSegment.includes(new Point(0.9,0.9)));
            assertTrue(lineSegment.includes(new Point(1,1)));

            assertFalse(lineSegment.includes(new Point(-1,-1)), "Point (-1,-1) is not included.");
            assertFalse(lineSegment.includes(new Point(2,-2)), "Point (2,2) is not included.");
            assertFalse(lineSegment.includes(new Point(0,1)), "Point (0,1) is not included.");
            assertFalse(lineSegment.includes(new Point(1,0)), "Point (1,0) is not included.");

        });

        it("allows to get intersection point of two line segments", () -> {

            LineSegment lineSegmentA = new LineSegment(
                new Point(0, 0),
                new Point(2, 2)
            );

            LineSegment lineSegmentB = new LineSegment(
                new Point(0, 2),
                new Point(2, 0)
            );

            assertTrue(lineSegmentA.isIntersecting(lineSegmentB));

            Point intersection = lineSegmentA.getIntersectionPointWith(lineSegmentB);

            assertEqual(intersection, new Point(1, 1));

        });

        it("allows to check if cross like two line segments intersect", () -> {

            LineSegment lineSegmentA = new LineSegment(
                new Point(0, 0),
                new Point(1, 1)
            );

            LineSegment lineSegmentB = new LineSegment(
                new Point(0, 1),
                new Point(1, 0)
            );

            assertTrue(lineSegmentA.isIntersecting(lineSegmentB));

        });

        it("allows to check if two orthogonal line segments intersect", () -> {

            LineSegment lineSegmentA = new LineSegment(
                new Point(0, 0),
                new Point(3, 0)
            );

            LineSegment lineSegmentB = new LineSegment(
                new Point(1, 1),
                new Point(1, 2)
            );

            assertEqual(lineSegmentA.getIntersectionPointWith(lineSegmentB), new Point(1,0));

            assertFalse(lineSegmentA.isIntersecting(lineSegmentB), "Two separate orthogonal line segments.");

        });

        it("allows to check if two arbitrary line segments intersect", () -> {

            LineSegment lineSegmentA = new LineSegment(
                new Point(1, 4),
                new Point(6, 6)
            );

            LineSegment lineSegmentB = new LineSegment(
                new Point(5, 0),
                new Point(5, 5)
            );

            assertFalse(lineSegmentA.isIntersecting(lineSegmentB), "No intersection.");

        });

        it("allows to check if two parallel line segments intersect", () -> {

            LineSegment lineSegmentA = new LineSegment(
                new Point(0, 0),
                new Point(0, 1)
            );

            LineSegment lineSegmentB = new LineSegment(
                new Point(1, 0),
                new Point(1, 1)
            );

            assertTrue(!lineSegmentA.isIntersecting(lineSegmentB));

        });

    }

}
