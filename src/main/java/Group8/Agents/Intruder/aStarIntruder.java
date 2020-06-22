package Group8.Agents.Intruder;

import Group8.PathFinding.AStar;
import Group8.PathFinding.SimplePathfinding;
import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Percept.IntruderPercepts;

/**
 * This agent is implemented using the A* algorithm
 * It specifically uses the occupancy grid as memory map
 */

public class aStarIntruder implements Intruder {
    private static AStar pathfinding;

    @Override
    public IntruderAction getAction(IntruderPercepts percepts){
        if(pathfinding == null){
            pathfinding = new AStar();
        }
        return pathfinding.getMoveIntruder(percepts);
    }
}
