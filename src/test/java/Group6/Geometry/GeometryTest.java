package Group6.Geometry;

import Group6.Agent.Factory.AgentsFactories;
import Group6.GroupTests;
import Group6.WorldState.Scenario;
import Group6.WorldState.ScenarioTest;
import Group6.WorldState.WorldState;
import SimpleUnitTest.SimpleUnitTest;

public class GeometryTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nGeometry Test\n");

        LineSegmentTest.main(args);
        QuadrilateralTest.main(args);

    }

}
