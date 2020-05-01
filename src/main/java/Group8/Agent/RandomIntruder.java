package Group8.Agent;

import Group8.Controller.Utils.IntruderInfo;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;

public class RandomIntruder extends Agent implements Intruder {

    public RandomIntruder(IntruderInfo agentInfo) {
        super(agentInfo);
    }

    private int moveCount = 0;
    private final int mCap = 9;

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        if(moveCount >= mCap){
            moveCount = 0;
            return new Rotate(Angle.fromDegrees(45));
        }
        else{
            moveCount++;
            return new Move(new Distance(1));
        }
    }
}
