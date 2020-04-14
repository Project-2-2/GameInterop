package Group9.agent.deepspace;

import Interop.Action.GuardAction;
import Interop.Percept.GuardPercepts;

public class StateHandlerInitial implements StateHandler {
    StateType nextState;

    @Override
    public GuardAction execute(GuardPercepts percepts, DeepSpace deepSpace) {

        nextState = StateType.EXPLORE_360;

        return new Inaction();
    }

    @Override
    public StateType getNextState() {
        return nextState;
    }
}
