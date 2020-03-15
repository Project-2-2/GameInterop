package Group9.agent;

import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Agent.Guard;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;

public class ExplorerAgent implements Guard {
    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        return new Move(new Distance(1));
    }
}
