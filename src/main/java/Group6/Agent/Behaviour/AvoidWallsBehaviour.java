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

    private double wallDistance1 = 0.0;
    private double wallDistance2 = 0.0;

    public Action getAction(Percepts percepts) {

        actions++;
        ObjectPercepts objectPercepts = PerceptsService.getWallPercepts(percepts);
        double wallDirection = PerceptsService.getMeanDirection(objectPercepts);

        if(wallDirection < 10) return ActionsFactory
            .getValidRotate(Math.signum(wallDirection) * 90 + wallDirection, percepts);

        return ActionsFactory.getPartMaxRotate(
            percepts,
            0.3 * Math.signum(wallDirection)
        );

    }

    public boolean shouldExecute(Percepts percepts) {

        if(actions > 5) { actions = 0; tillNextAction = 10; }
        if(tillNextAction > 0) return false;

        return wallDistance1 > 0 && wallDistance1 < wallDistance2;

    }

    public void updateState(Percepts percepts) {

        ObjectPercepts wallPercepts = PerceptsService.getWallPercepts(percepts);
        if(actions > 0 && wallPercepts.getAll().size() == 0) actions--;
        if(tillNextAction > 0) tillNextAction--;

        wallDistance2 = wallDistance1;
        wallDistance1 = PerceptsService.getMeanDistance(wallPercepts);

    }

}
