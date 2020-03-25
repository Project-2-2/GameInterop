package Group6.WorldState;

import Group6.Agent.Intruder.RandomIntruder;
import Group6.ExtendedUnitTest;
import Group6.Geometry.Direction;
import Group6.Geometry.Point;
import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Object.IntruderState;

/**
 * @author Tomasz Darmetko
 */
public class AgentStateTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nAgent State Test\n");

        it("allows to get direction to a (1, 0) point as perceived by agent at (0, 0), with 0 heading", () -> {

            AgentState agentState = new IntruderState(
                new RandomIntruder(),
                new Point(0,0),
                Direction.fromRadians(0)
            );

            assertEqual(
                agentState.getPerceivedDirectionTo(new Point(1, 0)).getDegrees(),
                90,
                "" // TODO: 90 or 270?
            );

        });

        it("allows to get direction to a (1, 0) point as perceived by agent at (0, 0), with 90 heading", () -> {

            AgentState agentState = new IntruderState(
                new RandomIntruder(),
                new Point(0,0),
                Direction.fromDegrees(90)
            );

            assertEqual(
                agentState.getPerceivedDirectionTo(new Point(1, 0)).getDegrees(),
                0,
                ""
            );

        });

        it("allows to get direction to a (1, 0) point as perceived by agent at (1, 2), with 0 heading", () -> {

            AgentState agentState = new IntruderState(
                new RandomIntruder(),
                new Point(1,2),
                Direction.fromRadians(0)
            );

            assertEqual(
                agentState.getPerceivedDirectionTo(new Point(1, 0)).getDegrees(),
                180,
                "" // TODO: 0 or 180?
            );

        });

    }

}
