package Group6.WorldState;

import Group6.GroupTests;
import SimpleUnitTest.SimpleUnitTest;

import java.nio.file.Paths;

public class ScenarioTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nScenario Test\n");

        it("allows to load scenario from file", () -> {
            new Scenario(GroupTests.resources + "/scenario.txt");
        });

    }

}
