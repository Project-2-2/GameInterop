package Group6.Agent;


import Group6.Agent.Intruder.WalksAroundWallIntruder;

import Group6.Examples.Percepts.IntruderPerceptsBuilder;
import Group6.ExtendedUnitTest;
import Interop.Action.Action;
import Interop.Action.Move;

import Interop.Percept.IntruderPercepts;


public class walkWallTest extends ExtendedUnitTest {

    public static void main(String [] args){

        System.out.println("\n\nwalk wall test Guard Test\n");

        WalksAroundWallIntruder walkIntruder = new WalksAroundWallIntruder();


        it("walk wall ", () -> {

            IntruderPercepts percepts= IntruderPerceptsBuilder.getSimple(true);
            Action action=walkIntruder.getAction(percepts);

            assertInstanceOf(action, Move.class);
            assertTrue(((Move)action).getDistance().getValue() > 0);
        });



    }
}
