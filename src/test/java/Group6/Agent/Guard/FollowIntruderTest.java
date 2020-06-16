package Group6.Agent.Guard;

import Group6.Examples.Percepts.GuardPerceptsBuilder;
import Group6.ExtendedUnitTest;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Percept.GuardPercepts;

public class FollowIntruderTest extends ExtendedUnitTest {

    public static void main(String [] args){

        System.out.println("\n\nFollowIntruder Guard Test\n");

        FollowIntruder followIntruder=new FollowIntruder();

        xit("Follow intruder", () -> {

            GuardPercepts percepts= GuardPerceptsBuilder.getSimple(true);
            Action action=followIntruder.getAction(percepts);

            assertInstanceOf(action, Move.class);
            assertTrue(((Move)action).getDistance().getValue() > 0);
        });
        xit("rotates if it couldn't move", () -> {

            GuardPercepts percepts = GuardPerceptsBuilder.getSimple(false);
            Action action = followIntruder.getAction(percepts);

            assertInstanceOf(action, Rotate.class);
            assertTrue(((Rotate)action).getAngle().getRadians() > 0);

        });


    }
}
