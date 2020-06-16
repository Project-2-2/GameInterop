package Group6.Agent;

import Interop.Action.Action;
import Interop.Action.Rotate;
import Interop.Geometry.Distance;
import Interop.Geometry.Angle;
import Interop.Percept.Percepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
abstract public class RandomAgent {

    private final boolean switchRotationSide = new Random().nextBoolean();

    protected Action getRandomAction(Percepts percepts) {

        if(percepts.wasLastActionExecuted()) {

            // go as far as possible
            return ActionsFactory.getMaxMove(percepts);

        } else {

            return getRandomRotate(percepts);

        }
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
