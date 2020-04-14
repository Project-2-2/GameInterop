package Group9.agent.deepspace;

import Interop.Action.GuardAction;
import Interop.Percept.GuardPercepts;

public interface StateHandler {

    public GuardAction execute(GuardPercepts percepts, DeepSpace deepSpace);

    public StateType getNextState();

}
