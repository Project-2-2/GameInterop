package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercepts;

/**
 * @author Tomasz Darmetko
 */
public class ToPassageBehaviour implements Behaviour {

    private int tillNextRotation = 0;
    private int tillNextPassage = 0;

    public Action getAction(Percepts percepts) {
        if(percepts.getAreaPercepts().isInDoor()) return ActionsFactory.getValidMove(Math.random() * 10, percepts);
        if(percepts.getAreaPercepts().isInWindow()) return ActionsFactory.getValidMove(Math.random() * 10, percepts);
        ObjectPercepts objectPercepts = PerceptsService.getPassagePercepts(percepts);
        double towards = -1 * PerceptsService.getMeanDirection(objectPercepts);
        if(tillNextPassage > 0) towards = towards * -1.0;
        if(tillNextRotation == 0) tillNextRotation = 3;
        return ActionsFactory.getValidRotate(towards, percepts);
    }

    public boolean shouldExecute(Percepts percepts) {
        if(!percepts.wasLastActionExecuted()) return false;
        if(percepts.getAreaPercepts().isInDoor()) return true;
        if(percepts.getAreaPercepts().isInWindow()) return true;

        ObjectPercepts passagePercepts = PerceptsService.getDoorPercepts(percepts);
        if(!seesPassages(passagePercepts)) return false;
        if(tillNextRotation > 0) return false;
        if(tillNextPassage > 0 && seesPassages(passagePercepts)) return true;
        if(Math.abs(PerceptsService.getMeanDirection(passagePercepts)) < 5) return false;
        return true;
    }

    public void updateState(Percepts percepts) {
        if(tillNextPassage > 0) tillNextPassage--; // allow exploration
        if(tillNextRotation > 0) tillNextRotation--; // prevents loops
        // avoid passages if almost trough the passage
        if(percepts.getAreaPercepts().isInDoor()) tillNextPassage = 500;
        if(percepts.getAreaPercepts().isInWindow()) tillNextPassage = 500;
        if(percepts.getAreaPercepts().isJustTeleported()) tillNextPassage = 0;
    }

    private boolean seesPassages(ObjectPercepts passagePercepts) {
        return passagePercepts.getAll().size() > 0;
    }

}
