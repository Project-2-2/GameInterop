package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Interop.Action.Action;
import Interop.Percept.Percepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class ExploreBehaviour implements Behaviour {

    private final boolean switchRotationSide = new Random().nextBoolean();

    public Action getAction(Percepts percepts) {
        if(percepts.wasLastActionExecuted()) {

            // go as far as possible
            return ActionsFactory.getMaxMove(percepts);

        } else {

            return getRandomRotate(percepts);

        }
    }

    public boolean shouldExecute(Percepts percepts) {
        return true;
    }

    public void updateState(Percepts percepts) {
        // NO OP
    }

    private Action getRandomRotate(Percepts percepts) {

        double modifier = (1. - Math.random()); // make it random
        modifier = modifier * (switchRotationSide ? -1.0 : 1.0);

        // start rotating if can not move any further
        return ActionsFactory.getValidRotate(
            ActionsFactory.getMaxRotationDegrees(percepts) * modifier,
            percepts
        );

    }

}
