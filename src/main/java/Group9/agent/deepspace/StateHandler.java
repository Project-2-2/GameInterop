package Group9.agent.deepspace;

import Interop.Action.GuardAction;
import Interop.Percept.GuardPercepts;

public interface StateHandler {

    public ActionContainer<GuardAction> execute(GuardPercepts percepts, DeepSpace deepSpace);

    public StateType getNextState();

    public void resetState();

}
