package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Action.Rotate;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercepts;

/**
 * This behaviour will lead agent toward teleport.
 * Soon after teleportation agent will avoid teleports in order to explore the new place.
 * @author Tomasz Darmetko
 */
public class ToTeleportBehaviour implements Behaviour {

    private int tillNextRotation = 0;
    private int tillNextTeleport = 0;

    public Action getAction(Percepts percepts) {
        ObjectPercepts teleportPercepts = PerceptsService.getTeleportPercepts(percepts);
        double towardsTeleport = -1.0 * PerceptsService.getMeanDirection(teleportPercepts);
        // turn away from a teleport if just teleported - provides time for exploration
        if(tillNextTeleport > 0) towardsTeleport = towardsTeleport * -1.0;
        if(tillNextRotation == 0) tillNextRotation = 5; // allows to avoid constant rotating
        return ActionsFactory.getValidRotate(towardsTeleport, percepts);
    }

    public boolean shouldExecute(Percepts percepts) {
        if(percepts.getAreaPercepts().isJustTeleported()) return false;
        ObjectPercepts teleportPercepts = PerceptsService.getTeleportPercepts(percepts);
        if(teleportPercepts.getAll().isEmpty()) return false;
        if(Math.abs(PerceptsService.getMeanDirection(teleportPercepts)) < 3) return false;
        if(tillNextTeleport > 0) return true; // allows to avoid teleports soon after teleporting
        if(tillNextRotation > 0) return false; // allows to avoid constant rotation
        return true;
    }

    public void updateState(Percepts percepts) {
        if(tillNextTeleport > 0) tillNextTeleport--;
        if(tillNextRotation > 0) tillNextRotation--;
        // provides time for exploration of the new place
        if(percepts.getAreaPercepts().isJustTeleported()) tillNextTeleport = 1000;
    }

}
