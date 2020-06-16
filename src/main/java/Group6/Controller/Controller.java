package Group6.Controller;

import Group6.WorldState.*;
import Group6.Percept.AgentPerceptsBuilder;
import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Object.GuardState;
import Group6.WorldState.Object.IntruderState;
import Interop.Action.*;

/**
 * @author Tomasz Darmetko
 */
public class Controller {

    private AgentPerceptsBuilder agentPerceptsBuilder;
    private boolean debug = true;

    public Controller(AgentPerceptsBuilder agentPerceptsBuilder) {
        this.agentPerceptsBuilder = agentPerceptsBuilder;
    }

    public Controller(AgentPerceptsBuilder agentPerceptsBuilder, boolean debug) {
        this.agentPerceptsBuilder = agentPerceptsBuilder;
        this.debug = debug;
    }

    public void executeTurn(WorldState worldState) {

        for (IntruderState intruderState: worldState.getIntruderStates()) {
            try {
                IntruderAction action = intruderState.getIntruder().getAction(
                    agentPerceptsBuilder.buildIntruderPercepts(worldState, intruderState)
                );
                executeAction(worldState, intruderState, action);
            } catch (Exception e) {
                intruderState.rejectAction();
                if(debug) throw e;
            }
        }

        for (GuardState guardState: worldState.getGuardStates()) {
            try {
                GuardAction action = guardState.getGuard().getAction(
                    agentPerceptsBuilder.buildGuardPercepts(worldState, guardState)
                );
                executeAction(worldState, guardState, action);
            } catch (Exception e) {
                guardState.rejectAction();
                if(debug) throw e;
            }
        }

        worldState.nextTurn();

    }

    private void executeAction(WorldState worldState, AgentState agentState, Action action) {

        if(action instanceof NoAction) {
            agentState.noAction();
            return;
        }

        if(agentState.hasCooldown()) {
            agentState.rejectAction();
            return;
        }

        if(action instanceof Move) {
            agentState.move(worldState, (Move)action);
            return;
        }

        if(action instanceof Rotate) {
            agentState.rotate(worldState, (Rotate)action);
            return;
        }

        if(action instanceof DropPheromone) {
            agentState.dropPheromone(worldState, (DropPheromone)action);
            return;
        }

        if(action instanceof Yell) {
            ((GuardState)agentState).yell(worldState, (Yell)action);
            return;
        }

        if(action instanceof Sprint && agentState instanceof IntruderState) {
            ((IntruderState)agentState).sprint(worldState, (Sprint)action);
            return;
        }

        throw new UnexpectedAction(action, agentState);

    }

    static class UnexpectedAction extends RuntimeException {
        public UnexpectedAction(Action action, AgentState agentState) {
            super(
                "Unexpected action: " + action.getClass().getName() + "\n" +
                "Issued by: " + agentState.getClass().getName() + "\n" +
                "Has cooldown: " + (agentState.hasCooldown() ? "yes" : "no")
            );
        }
    }

}
