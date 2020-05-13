package Group6.WorldState;

import Group6.Agent.Factory.AgentsFactories;
import Group6.ExtendedUnitTest;
import Group6.GroupTests;
import SimpleUnitTest.SimpleUnitTest;

/**
 * @author Tomasz Darmetko
 */
public class WorldStateTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        AgentStateTest.main(args);
        ScenarioTest.main(args);

        System.out.println("\n\nWorld State Test\n");

        it("allows to create world state from a scenario", () -> {

            Scenario scenario = new Scenario(GroupTests.resources + "/scenario.txt");

            WorldState worldState = new WorldState(
                scenario,
                new AgentsFactories(),
                "random",
                "random"
            );

            assertEqual(worldState.getGuardStates().size(), 3);
            assertEqual(worldState.getIntruderStates().size(), 2);

            assertTrue(scenario.getSpawnAreaGuards().hasInside(
                worldState.getGuardStates().get(1).getLocation()
            ));

            assertTrue(scenario.getSpawnAreaIntruders().hasInside(
                worldState.getIntruderStates().get(1).getLocation()
            ));

        });

    }

}
