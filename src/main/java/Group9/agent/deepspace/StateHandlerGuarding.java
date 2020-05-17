package Group9.agent.deepspace;

import Interop.Action.GuardAction;
import Interop.Action.NoAction;
import Interop.Percept.GuardPercepts;

public class StateHandlerGuarding implements StateHandler {
    @Override
    public ActionContainer<GuardAction> execute(GuardPercepts percepts, DeepSpace deepSpace) {
        return ActionContainer.of(this, new NoAction(), ActionContainer.Input.create().i("guarding", true));
    }

    @Override
    public StateType getNextState() {
        return StateType.GUARD_TARGET_AREA;
    }

    @Override
    public void resetState() {

    }
}
