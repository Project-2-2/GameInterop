package Group6.WorldState;

import Group6.Agent.Factory.AgentsFactories;
import Group6.GroupTests;
import SimpleUnitTest.SimpleUnitTest;

public class WorldStateTest extends SimpleUnitTest {

    public static void main(String[] args) {

        AgentStateTest.main(args);
        ScenarioTest.main(args);

        System.out.println("\n\nWorld State Test\n");

        it("allows to create world state from a scenario", () -> {
            Scenario scenario = new Scenario(GroupTests.resources + "/scenario.txt");
            new WorldState(
                scenario,
                new AgentsFactories(),
                "random",
                "random"
            );
        });

    }

}
