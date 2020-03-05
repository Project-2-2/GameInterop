package Group6.Controller;

import Group6.WorldState.GuardState;
import Group6.WorldState.IntruderState;
import Group6.WorldState.WorldState;
import Group6.Percept.AgentPerceptsBuilder;
import Interop.Action.Action;
import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;

public class Controller {

    private AgentPerceptsBuilder agentPerceptsBuilder;

    public void nextTurn(WorldState worldState) {
        for (IntruderState intruderState : worldState.getIntruderStates()) {
            IntruderAction action = intruderState.getIntruder().getAction(
                agentPerceptsBuilder.buildIntruderPercepts(worldState, intruderState)
            );
            executeAction(action);
        }
        for (GuardState guardState : worldState.getGuardStates()) {
            GuardAction action = guardState.getGuard().getAction(
                agentPerceptsBuilder.buildGuardPercepts(worldState, guardState)
            );
            executeAction(action);
        }
    }

    public void executeAction(Action action) {
        // TODO: implement
    }

}
