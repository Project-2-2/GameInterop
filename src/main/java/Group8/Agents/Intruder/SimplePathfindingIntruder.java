package Group8.Agents.Intruder;

import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Percept.IntruderPercepts;


/**
 * This agent will only focus on getting to the target and will not consider anything else
 */
public class SimplePathfindingIntruder implements Intruder {

    private static Group8.PathFinding.SimplePathfindingIntruder pathfinding = new Group8.PathFinding.SimplePathfindingIntruder();


    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        return pathfinding.getMoveIntruder(percepts);
    }
}
