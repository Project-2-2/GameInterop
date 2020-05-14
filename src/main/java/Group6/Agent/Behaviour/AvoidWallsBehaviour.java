package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class AvoidWallsBehaviour implements Behaviour {

    private int actions = 0;
    private int tillNextAction = 0;

    public Action getAction(Percepts percepts) {

        actions++;
        ObjectPercepts objectPercepts = PerceptsService.getWallPercepts(percepts);

        return ActionsFactory.getPartMaxRotate(
            percepts,
            Math.signum(0.3 * PerceptsService.getMeanDirection(objectPercepts))
        );

    }

    public boolean shouldExecute(Percepts percepts) {

        if(actions > 1) { actions = 0; tillNextAction = 20; }
        if(tillNextAction > 0) return false;

        ObjectPercepts wallPercepts = PerceptsService.getWallPercepts(percepts);
        if(PerceptsService.getMeanDistance(wallPercepts) < 5) return false;

        return wallPercepts.getAll().size() > 2;

    }

    public void updateState(Percepts percepts) {
        if(tillNextAction > 0) tillNextAction--;
    }

}
