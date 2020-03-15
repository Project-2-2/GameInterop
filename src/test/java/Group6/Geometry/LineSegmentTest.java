package Group6.Geometry;

import SimpleUnitTest.SimpleUnitTest;

public class LineSegmentTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nLine Segment Test\n");

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
