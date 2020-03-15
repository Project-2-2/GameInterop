package Group6.Geometry;

import Group6.Agent.Factory.AgentsFactories;
import Group6.GroupTests;
import Group6.WorldState.Scenario;
import Group6.WorldState.ScenarioTest;
import Group6.WorldState.WorldState;
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

            assertTrue(quadrilateral.isInside(new Point(0.5, 0.5)));

        });

        it("allows to get random point inside", () -> {

            Quadrilateral quadrilateral = new Quadrilateral(
                new Point(0,0),
                new Point(1,0),
                new Point(0,1),
                new Point(1,1)
            );

            assertInstanceOf(quadrilateral.getRandomPointInside(), Point.class);
            assertTrue(quadrilateral.isInside(quadrilateral.getRandomPointInside()));

        });

    }

}
