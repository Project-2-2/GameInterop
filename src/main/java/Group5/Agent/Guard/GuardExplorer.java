package Group5.Agent.Guard;

import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Agent.Guard;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;

public class GuardExplorer implements Guard {


    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        return explore(percepts);
    }

    public GuardAction explore(GuardPercepts percepts){
        return new Move(new Distance(1));
    }
}
