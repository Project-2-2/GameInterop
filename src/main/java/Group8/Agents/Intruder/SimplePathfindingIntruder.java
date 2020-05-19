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

    private SimplePathfinding pathfinding;
    private static int intruderCount = 0;
    private static int currentIntruder = 1;
    private boolean init;
    private static boolean doneInit = false;


    @Override
    public IntruderAction getAction(IntruderPercepts percepts){
        init = false;
        if(pathfinding == null){
            pathfinding = new SimplePathfinding(percepts);
            intruderCount++;
            init = true;
        }


        nextIntruder();
        return pathfinding.getMoveIntruder(percepts);
    }

    private void nextIntruder(){
        if(init){
            System.out.println(String.format("Currently active intruder: %d",currentIntruder++));
        }
        else {
            if(!doneInit){
                currentIntruder = 1;
                System.out.println(String.format("Currently active intruder: %d",currentIntruder));
                doneInit = true;
            }
            else {
                if (currentIntruder == intruderCount) {
                    currentIntruder = 1;
                } else {
                    currentIntruder++;
                }
                System.out.println(String.format("Currently active intruder: %d",currentIntruder));
            }
        }
    }
}
