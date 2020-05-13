package Group6.Agent.Intruder;

import Group6.Geometry.Direction;
import Group6.Utils;
import Group9.agent.RandomIntruderAgent;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Percept.IntruderPercepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class StraightToTargetIntruder implements Intruder {

    int continueExplorationFor = 0;

    public IntruderAction getAction(IntruderPercepts percepts) {
        if(!percepts.wasLastActionExecuted()) continueExplorationFor = new Random().nextInt(200);
        if(continueExplorationFor > 0) {
            continueExplorationFor--;
            return new RandomIntruder().getAction(percepts);
        }
        return getMoveToTargetAction(percepts);
    }

    private IntruderAction getMoveToTargetAction(IntruderPercepts percepts) {
        Direction targetDirection = Direction.fromInteropDirection(percepts.getTargetDirection());
        double targetAngle = targetDirection.getDegrees() < 180 ?
            targetDirection.getDegrees() : targetDirection.getDegrees() - 360;
        if(Math.abs(targetAngle) > 10) {
            double maxAngle = percepts
                .getScenarioIntruderPercepts()
                .getScenarioPercepts()
                .getMaxRotationAngle()
                .getDegrees();

            double rotation = Math.signum(targetAngle) * Math.min(Math.abs(targetAngle), maxAngle);
            return new Rotate(Angle.fromDegrees(rotation));
        }
        return new Move(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
    }
}
