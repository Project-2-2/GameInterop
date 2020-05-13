package Group6.Agent;

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
import com.sun.webkit.network.Util;

public class DisperseAgent {

    public Action getDisperseAction(Percepts percepts) {

        ObjectPercepts agentPercepts = PerceptsService.getAgentPercepts(percepts);

        double sumOfAgentAngles = agentPercepts
            .getAll()
            .stream()
            .mapToDouble(percept -> percept.getPoint().getClockDirection().getDegrees())
            .reduce(0, Double::sum);

        double meanAgentsAngle = sumOfAgentAngles / agentPercepts.getAll().size();
        double oppositeToAgents = meanAgentsAngle - 180;

        return ActionsFactory.getValidRotate(oppositeToAgents, percepts);

    }

    public boolean shouldDisperse(Percepts percepts) {
        return !PerceptsService
            .getAgentPercepts(percepts)
            .getAll()
            .isEmpty();
    }

}
