package Group1.AgentsGroup01.Intruders;

import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Percept.IntruderPercepts;

import java.util.Random;

public class SimpleIntruder implements Intruder {

    public SimpleIntruder() {}

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {

        int choice = (int) (Math.random() * 10);

        // turn around, if last action was not executed. You are probably stuck at a wall.
        if (!percepts.wasLastActionExecuted()) {
            // System.out.println(percepts.getVision().getObjects());

            return randomRotate(percepts);
        }

        if (choice < 7) {
            Move move = new Move(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
            return move;
        }

        if (choice < 9) {
            return randomRotate(percepts);

        }
        return new NoAction();
    }

    private Rotate randomRotate(IntruderPercepts percepts){
        int maxRotationAngleDegrees = (int) Math.round(percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees());
        int turningDegrees = new Random().nextInt(maxRotationAngleDegrees) - maxRotationAngleDegrees/2;
        Angle angle = Angle.fromDegrees(turningDegrees);
        return new Rotate(angle);
    }

}
