package Group9.agent.deepspace;

import Interop.Action.GuardAction;
import Interop.Percept.GuardPercepts;

public class StateHandlerInitial implements StateHandler {
    StateType nextState;

    @Override
    public ActionContainer<GuardAction> execute(GuardPercepts percepts, DeepSpace deepSpace) {

        nextState = StateType.EXPLORE_360;

        return ActionContainer.of(this, new Inaction());
    }

    @Override
    public StateType getNextState() {
        return nextState;
    }
}
