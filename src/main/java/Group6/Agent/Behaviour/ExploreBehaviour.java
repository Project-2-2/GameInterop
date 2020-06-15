package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class ExploreBehaviour implements Behaviour {

    private boolean switchRotationSide = new Random().nextBoolean();
    private boolean isNextToWall = false;
    private double wallDistance = Double.POSITIVE_INFINITY;

    public Action getAction(Percepts percepts) {
        if(percepts.wasLastActionExecuted() && !isNextToWall) {

            // go as far as possible
            return ActionsFactory.getValidMove(
                Math.min(ActionsFactory.getMaxMoveDistance(percepts), wallDistance - 0.5),
                percepts
            );

        } else {

            return getRandomRotate(percepts);

        }
    }

    public boolean shouldExecute(Percepts percepts) {
        return true;
    }

    public void updateState(Percepts percepts) {

        ObjectPercepts wallPercepts = PerceptsService.getWallPercepts(percepts);

        if(wallPercepts.getAll().size() > 0) {
            wallDistance = PerceptsService.getMeanDistance(wallPercepts);
        } else {
            wallDistance = Double.POSITIVE_INFINITY;
        }

        isNextToWall = wallDistance < 2;

        if(wallDistance > 8 && Math.random() > 0.95) switchRotationSide = !switchRotationSide;

    }

    private Action getRandomRotate(Percepts percepts) {

        double modifier = (1.5 - Math.random()); // make it random
        modifier = modifier * (switchRotationSide ? -1.0 : 1.0);

        // start rotating if can not move any further
        return ActionsFactory.getPartMaxRotate(percepts, modifier);

    }

}
