package Group8.Agents.Intruder;

import Group8.PathFinding.SimplePathfinding;
import Interop.Action.IntruderAction;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Percept.IntruderPercepts;


/**
 * This agent will only focus on getting to the target and will not consider anything else
 */
public class SimplePathfindingIntruder implements Intruder {

    private static SimplePathfinding pathfinding;


    @Override
    public IntruderAction getAction(IntruderPercepts percepts){
        if(pathfinding == null){
            pathfinding = new SimplePathfinding(percepts);
        }
        return pathfinding.getMoveIntruder(percepts);
    }
}
