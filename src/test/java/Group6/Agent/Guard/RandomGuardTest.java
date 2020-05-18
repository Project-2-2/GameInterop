package Group6.Agent.Guard;

import Group6.Agent.Guard.RandomGuard;
import Group6.Examples.Percepts.GuardPerceptsBuilder;
import Group6.Examples.Percepts.IntruderPerceptsBuilder;
import Group6.ExtendedUnitTest;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import SimpleUnitTest.SimpleUnitTest;

/**
 * @author Tomasz Darmetko
 */
public class RandomGuardTest extends ExtendedUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nRandom Guard Test\n");

        RandomGuard agent = new RandomGuard();

        it("moves as far as possible", () -> {

            GuardPercepts percepts = GuardPerceptsBuilder.getSimple(true);
            Action action = agent.getAction(percepts);

            assertInstanceOf(action, Move.class);
            assertTrue(((Move)action).getDistance().getValue() > 0);

        });

        it("rotates if it couldn't move", () -> {

            GuardPercepts percepts = GuardPerceptsBuilder.getSimple(false);
            Action action = agent.getAction(percepts);

            assertInstanceOf(action, Rotate.class);

        });

    }

}
