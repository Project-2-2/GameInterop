package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Action.Rotate;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercepts;

/**
 * @author Tomasz Darmetko
 */
public class ToTeleportBehaviour implements Behaviour {

    private int tillNextRotation = 0;
    private int tillNextTeleport = 0;

    public Action getAction(Percepts percepts) {
        ObjectPercepts teleportPercepts = PerceptsService.getTeleportPercepts(percepts);
        double towardsTeleport = PerceptsService.getMeanClockDirection(teleportPercepts) - 180;
        if(tillNextTeleport > 0) towardsTeleport = towardsTeleport * -1.0;
        if(tillNextRotation == 0) tillNextRotation = 5;
        return ActionsFactory.getValidRotate(towardsTeleport, percepts);
    }

    public boolean shouldExecute(Percepts percepts) {
        if(percepts.getAreaPercepts().isJustTeleported()) return false;
        if(PerceptsService.getTeleportPercepts(percepts).getAll().isEmpty()) return false;
        if(tillNextTeleport > 0) return true;
        if(tillNextRotation > 0) return false;
        return true;
    }

    public void updateState(Percepts percepts) {
        if(tillNextTeleport > 0) tillNextTeleport--;
        if(tillNextRotation > 0) tillNextRotation--;
        if(percepts.getAreaPercepts().isJustTeleported()) tillNextTeleport = 1000;
    }

}
