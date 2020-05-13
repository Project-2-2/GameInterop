package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Group6.Geometry.Direction;
import Interop.Action.Action;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class ToTargetBehaviour {

    private final int TILL_NEXT_ATTEMPT = 500;

    private boolean isExecuting = false;
    private int tillNextAttempt = 50;

    public Action getAction(Percepts percepts) {

        isExecuting = true;

        IntruderPercepts intruderPercepts = (IntruderPercepts)percepts;

        Direction targetDirection = Direction
            .fromInteropDirection(intruderPercepts.getTargetDirection());

        double targetAngle = targetDirection.getDegrees() < 180 ?
            targetDirection.getDegrees() : targetDirection.getDegrees() - 360;

        if(Math.abs(targetAngle) > 10) {
            return ActionsFactory.getValidRotate(targetAngle, percepts);
        }

        return ActionsFactory.getMaxMove(percepts);

    }

    public boolean shouldExecute(Percepts percepts) {
        return isExecuting || tillNextAttempt == 0;
    }

    public void updateState(Percepts percepts) {
        if(!percepts.wasLastActionExecuted() && isExecuting) {
            isExecuting = false;
            tillNextAttempt = new Random().nextInt(TILL_NEXT_ATTEMPT);
        }
        if(tillNextAttempt > 0) tillNextAttempt--;
    }

}
