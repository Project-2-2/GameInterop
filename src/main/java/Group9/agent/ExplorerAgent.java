package Group9.agent;

import Interop.Action.GuardAction;
import Interop.Action.NoAction;
import Interop.Agent.Guard;
import Interop.Percept.GuardPercepts;

public class ExplorerAgent implements Guard {
    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        return new NoAction();
    }
}
