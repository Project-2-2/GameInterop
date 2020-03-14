package Group6.Agent.Intruder;

import Group6.Agent.RandomAgent;
import Group6.Examples.Percepts.IntruderPerceptsBuilder;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Percept.IntruderPercepts;
import SimpleUnitTest.SimpleUnitTest;

public class RandomIntruderTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nRandom Intruder Test\n");

        RandomIntruder agent = new RandomIntruder();

        it("moves as far as possible", () -> {

            IntruderPercepts percepts = IntruderPerceptsBuilder.getSimple(true);
            Action action = agent.getAction(percepts);

            assertInstanceOf(action, Move.class);
            assertTrue(((Move)action).getDistance().getValue() > 0);

        });

        it("rotates if it couldn't move", () -> {

            IntruderPercepts percepts = IntruderPerceptsBuilder.getSimple(false);
            Action action = agent.getAction(percepts);

            assertInstanceOf(action, Rotate.class);
            assertTrue(((Rotate)action).getAngle().getRadians() > 0);
            
        });

    }

}
