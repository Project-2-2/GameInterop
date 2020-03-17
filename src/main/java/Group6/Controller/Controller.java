package Group6.Controller;

import Group6.Geometry.Angle;
import Group6.Geometry.Distance;
import Group6.WorldState.AgentState;
import Group6.WorldState.GuardState;
import Group6.WorldState.IntruderState;
import Group6.WorldState.WorldState;
import Group6.Percept.AgentPerceptsBuilder;
import Interop.Action.*;

public class Controller {

    private AgentPerceptsBuilder agentPerceptsBuilder;
    private boolean debug = true;

    public Controller(AgentPerceptsBuilder agentPerceptsBuilder, boolean debug) {
        this.agentPerceptsBuilder = agentPerceptsBuilder;
        this.debug = debug;
    }

    public void nextTurn(WorldState worldState) {

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

    }

    public void executeAction(WorldState worldState, AgentState agentState, Action action) {

        if(action instanceof Move) {
            agentState.move(new Distance(((Move)action).getDistance()));
        }

        if(action instanceof Rotate) {
            agentState.rotate(Angle.fromInteropAngle(((Rotate) action).getAngle()));
        }

    }

}
