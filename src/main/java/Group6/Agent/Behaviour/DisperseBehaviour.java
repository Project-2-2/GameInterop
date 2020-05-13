package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Group6.GUI.Agent;
import Interop.Action.Action;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Utils.Utils;


/**
 * @author Tomasz Darmetko
 */
public class DisperseBehaviour implements Behaviour {

    private int tillNextDisperse = 0;

    public Action getAction(Percepts percepts) {

        tillNextDisperse = 5;
        ObjectPercepts agentPercepts = PerceptsService.getAgentPercepts(percepts);
        double oppositeToAgents = PerceptsService.getMeanClockDirection(agentPercepts) - 180;
        return ActionsFactory.getValidRotate(oppositeToAgents, percepts);

    }

    public boolean shouldExecute(Percepts percepts) {
        if(tillNextDisperse > 0) return false;
        return !PerceptsService
            .getAgentPercepts(percepts)
            .getAll()
            .isEmpty();
    }

    public void updateState(Percepts percepts) {
        if(tillNextDisperse > 0) tillNextDisperse--;
    }

}
