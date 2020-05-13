package Group6.Agent.Intruder;

import Group6.Agent.ActionsFactory;
import Group6.Agent.DisperseAgent;
import Group6.Agent.RandomAgent;
import Group6.Geometry.Direction;
import Group6.Utils;
import Group9.agent.RandomIntruderAgent;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class StraightToTargetIntruder implements Intruder {

    double disperse = 0;
    int continueExplorationFor = 0;

    private final RandomIntruder randomAgent = new RandomIntruder();

    public IntruderAction getAction(IntruderPercepts percepts) {

        if(disperse < 3 && new DisperseAgent().shouldDisperse(percepts)) {
            disperse++;
            return (IntruderAction)new DisperseAgent().getDisperseAction(percepts);
        } else {
            disperse = Math.max(disperse - 0.1, 0);
        }

        if(!percepts.wasLastActionExecuted()) continueExplorationFor = new Random().nextInt(400);

        if(continueExplorationFor > 0) {
            continueExplorationFor--;
            return randomAgent.getAction(percepts);
        }

        return getMoveToTargetAction(percepts);
    }

    private IntruderAction getMoveToTargetAction(IntruderPercepts percepts) {
        Direction targetDirection = Direction
            .fromInteropDirection(percepts.getTargetDirection());
        double targetAngle = targetDirection.getDegrees() < 180 ?
            targetDirection.getDegrees() : targetDirection.getDegrees() - 360;
        if(Math.abs(targetAngle) > 10) {
            return ActionsFactory.getValidRotate(targetAngle, percepts);
        }
        return ActionsFactory.getMaxMove(percepts);
    }
}
