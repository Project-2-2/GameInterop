package Group6.Geometry;

import SimpleUnitTest.SimpleUnitTest;

public class QuadrilateralTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nQuadrilateral Test\n");

        it("allows to check if point is inside", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0,0),
                new Point(1,0),
                new Point(0,1),
                new Point(1,1)
            );

            assertTrue(quadrilateral.hasInside(new Point(0.5, 0.5)));

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

        });

    }

}
