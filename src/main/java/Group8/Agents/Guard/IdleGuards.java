package Group8.Agents.Guard;

import Interop.Action.GuardAction;
import Interop.Action.NoAction;
import Interop.Agent.Guard;
import Interop.Percept.GuardPercepts;

public class IdleGuards implements Guard {
    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        return new NoAction();
    }
}
