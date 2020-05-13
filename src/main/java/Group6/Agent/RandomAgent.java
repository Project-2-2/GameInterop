package Group6.Agent;

import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Distance;
import Interop.Geometry.Angle;
import Interop.Percept.Percepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
abstract public class RandomAgent {
    enum Side { left, right }
    private Side rotationSide = Side.right;
    protected Action getRandomAction(Percepts percepts, Distance maxMove, Angle maxRotate) {
        if(percepts.wasLastActionExecuted()) {
            rotationSide = new Random().nextBoolean() ? Side.left : Side.right;
            return new Move(new Distance(maxMove.getValue() * (1. - Math.random()))); // go as far as possible
        } else {
            double modifier;
            switch (rotationSide) {
                case left: modifier = -1.0; break;
                case right: modifier = 1.0; break;
                default:
                    throw new RuntimeException("Wrong rotation side... " + rotationSide);
            }
            // start rotating if can not move any further
            return new Rotate(Angle.fromRadians(maxRotate.getRadians() * (1. - Math.random()) * modifier));
        }
    }
}
