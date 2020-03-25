package Group6.Geometry;

import Group6.ExtendedUnitTest;
import SimpleUnitTest.SimpleUnitTest;

/**
 * @author Tomasz Darmetko
 */
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

        it("allows to check if point is inside on simple quadrilateral", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0,0),
                new Point(1,0),
                new Point(1,1),
                new Point(0,1)
            );

            assertTrue(quadrilateral.hasInside(new Point(0.1, 0.1)));
            assertTrue(quadrilateral.hasInside(new Point(0.5, 0.5)));
            assertTrue(quadrilateral.hasInside(new Point(0.7, 0.7)));
            assertTrue(quadrilateral.hasInside(new Point(0.1, 0.7)));
            assertTrue(quadrilateral.hasInside(new Point(0.7, 0.1)));

            assertFalse(quadrilateral.hasInside(new Point(2, 2)), "Point (2,2) is outside.");

        });

        it("allows to check if point is inside on more complex quadrilateral", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0, 0),
                new Point(5, 0),
                new Point(5, 5),
                new Point(0, 5)
            );

            assertTrue(quadrilateral.hasInside(new Point(0, 0)), "Point (0,0) is inside.");
            assertTrue(quadrilateral.hasInside(new Point(1, 4)), "The (1, 4) is inside.");
            assertTrue(quadrilateral.hasInside(new Point(4, 1)), "The (4, 1) is inside.");
            assertTrue(quadrilateral.hasInside(new Point(2, 2)), "The (2, 2) is inside.");
            assertTrue(quadrilateral.hasInside(new Point(4, 4)), "The (2, 4) is inside.");
            assertTrue(quadrilateral.hasInside(quadrilateral.getCenter()), "The center is inside.");

            assertFalse(quadrilateral.hasInside(new Point(6, 6)), "Point (6,6) is outside.");
            assertFalse(quadrilateral.hasInside(new Point(0, 6)), "Point (0,6) is outside.");
            assertFalse(quadrilateral.hasInside(new Point(6, 0)), "Point (6,0) is outside.");

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
