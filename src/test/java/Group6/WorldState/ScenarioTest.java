package Group6.WorldState;

import Group6.ExtendedUnitTest;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;
import Group6.GroupTests;
import SimpleUnitTest.SimpleUnitTest;

import java.nio.file.Paths;

/**
 * @author Tomasz Darmetko
 */
public class ScenarioTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nScenario Test\n");

        it("allows to load scenario from file", () -> {
            new Scenario(GroupTests.resources + "/scenario.txt");
        });

        it("correctly reads areas", () -> {

            Scenario scenario = new Scenario(GroupTests.resources + "/scenario.txt");

            assertFalse(scenario.getShadedAreas().getAll().isEmpty(), "Reads at lest one shaded area.");
            assertFalse(scenario.getWalls().getAll().isEmpty(), "Reads at lest one wall area.");
            assertFalse(scenario.getWindows().getAll().isEmpty(), "Reads at lest one window area.");
            assertFalse(scenario.getDoors().getAll().isEmpty(), "Reads at lest one door area.");
            assertFalse(scenario.getSentryTowers().getAll().isEmpty(), "Reads at lest one sentry tower area.");

            assertFalse(new Point(0, 0).isInside(scenario.getShadedAreas()), "Point (0,0) is outside shaded areas.");

            Quadrilateral shadedArea = scenario.getShadedAreas().getAll().iterator().next();

            assertEqual(shadedArea.getPointA(), new Point(20, 20));
            assertEqual(shadedArea.getPointB(), new Point(40, 20));
            assertEqual(shadedArea.getPointC(), new Point(40, 40));
            assertEqual(shadedArea.getPointD(), new Point(20, 40));

        });


    }

}
