package Group6.Geometry;

import Group6.Percept.Vision.RaysTest;
import SimpleUnitTest.SimpleUnitTest;

/**
 * @author Tomasz Darmetko
 */
public class GeometryTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nGeometry Test\n");

        VectorTest.main(args);
        DirectionTest.main(args);
        LineTest.main(args);
        LineSegmentTest.main(args);
        CircleTest.main(args);
        QuadrilateralTest.main(args);

    }

}
