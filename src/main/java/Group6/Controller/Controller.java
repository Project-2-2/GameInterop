package Group6.Controller;

import Group6.Geometry.Angle;
import Group6.Geometry.Distance;
import Group6.WorldState.*;
import Group6.Percept.AgentPerceptsBuilder;
import Interop.Action.*;

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
            return;
        }

        if(agentState.hasCooldown()) {
            agentState.rejectAction();
            return;
        }

        if(action instanceof Move) {
            agentState.move(new Distance(((Move)action).getDistance()));
            return;
        }

        if(action instanceof Rotate) {
            agentState.rotate(Angle.fromInteropAngle(((Rotate) action).getAngle()));
            return;
        }

        if(action instanceof DropPheromone) {
            worldState.addPheromone(
                Pheromone.createByAgent(worldState, agentState, (DropPheromone)action)
            );
            return;
        }

        if(action instanceof Yell) {
            worldState.addSound(Sound.createYell(worldState.getScenario(), agentState));
            return;
        }

        if(action instanceof Sprint) {
            if(agentState instanceof IntruderState) {
                ((IntruderState)agentState).sprint(
                    new Distance(((Sprint)action).getDistance()),
                    worldState.getScenario().getSprintCooldown()
                );
            } else {
                agentState.rejectAction();
            }
            return;
        }

        throw new UnexpectedAction(action);

    }

    static class UnexpectedAction extends RuntimeException {
        public UnexpectedAction(Action action) {
            super(
                "Unexpected action: " + action.getClass().getName()
            );
        }
    }

}
