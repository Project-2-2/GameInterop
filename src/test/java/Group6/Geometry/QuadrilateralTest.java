package Group6.Geometry;

import Group6.ExtendedUnitTest;
import SimpleUnitTest.SimpleUnitTest;

public class QuadrilateralTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nQuadrilateral Test\n");

        it("allows to get bounding box", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0,1),
                new Point(2,1),
                new Point(1,3),
                new Point(4,5)
            );

            assertEqual(quadrilateral.getMinX(), 0., Tolerance.epsilon);
            assertEqual(quadrilateral.getMaxX(), 4., Tolerance.epsilon);
            assertEqual(quadrilateral.getMinY(), 1., Tolerance.epsilon);
            assertEqual(quadrilateral.getMaxY(), 5., Tolerance.epsilon);

        });

        it("allows to check if point is inside", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0,0),
                new Point(1,0),
                new Point(0,1),
                new Point(1,1)
            );

            assertTrue(quadrilateral.hasInside(new Point(0.1, 0.1)));
            assertTrue(quadrilateral.hasInside(new Point(0.5, 0.5)));
            assertTrue(quadrilateral.hasInside(new Point(0.7, 0.7)));
            assertTrue(quadrilateral.hasInside(new Point(0.1, 0.7)));
            assertTrue(quadrilateral.hasInside(new Point(0.7, 0.1)));

            assertFalse(quadrilateral.hasInside(new Point(2, 2)), "Point (2,2) is outside.");

        });

        it("allows to get random point inside", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0,0),
                new Point(1,0),
                new Point(0,1),
                new Point(1,1)
            );

            assertInstanceOf(quadrilateral.getRandomPointInside(), Point.class);
            assertTrue(quadrilateral.hasInside(quadrilateral.getRandomPointInside()));

            Point randomPoint = quadrilateral.getRandomPointInside();
            assertTrue(randomPoint.getX() >= 0);
            assertTrue(randomPoint.getX() <= 1);
            assertTrue(randomPoint.getY() >= 0);
            assertTrue(randomPoint.getY() <= 1);

        });

        it("allows to get center point when quadrilateral has one corner at origin", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0,0),
                new Point(1,0),
                new Point(0,1),
                new Point(1,1)
            );

            assertEqual(quadrilateral.getCenter(), new Point(0.5, 0.5));

        });

        it("allows to get center point when quadrilateral is away from origin", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(1,1),
                new Point(2,1),
                new Point(1,2),
                new Point(2,2)
            );

            assertEqual(quadrilateral.getCenter(), new Point(1.5, 1.5));

        });

    }

}
