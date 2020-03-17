package Group6.Controller;

import Group6.Agent.Factory.AgentsFactories;
import Group6.ExtendedUnitTest;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.GroupTests;
import Group6.Percept.*;
import Group6.WorldState.Scenario;
import Group6.WorldState.WorldState;

public class ControllerTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nController Test\n");

        it("allows to execute a turn", () -> {

            Controller controller = new Controller(
                new AgentPerceptsBuilder(
                    new VisionPerceptsBuilder(),
                    new SoundPerceptsBuilder(),
                    new SmellPreceptsBuilder(),
                    new AreaPerceptsBuilder(),
                    new ScenarioPerceptsBuilder()
                )
            );

            WorldState worldState = new WorldState(
                new Scenario(GroupTests.resources + "/scenario.txt"),
                new AgentsFactories(),
                "random",
                "random"
            );

            assertEqual(worldState.getTurn(), 0);

            controller.executeTurn(worldState);

            assertEqual(worldState.getTurn(), 1);


        });

    }

}
