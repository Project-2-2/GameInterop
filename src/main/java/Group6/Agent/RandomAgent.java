package Group6.Agent;

import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Distance;
import Interop.Geometry.Angle;
import Interop.Percept.Percepts;

abstract public class RandomAgent {
    protected Action getRandomAction(Percepts percepts, Distance maxMove, Angle maxRotate) {
        if(percepts.wasLastActionExecuted()) {
            return new Move(maxMove); // go as far as possible
        } else {
            // start rotating if can not move any further
            return new Rotate(Angle.fromRadians(maxRotate.getRadians() * (1. - Math.random())));
        }
    }
}
