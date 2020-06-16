package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Group6.Geometry.Direction;
import Interop.Action.Action;
import Interop.Action.NoAction;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class ToTargetBehaviour implements Behaviour {

    private final int TILL_NEXT_ATTEMPT = 200 + new Random().nextInt(300);

    private boolean isExecuting = false;
    private int tillNextAttempt = new Random().nextInt(70);

    public Action getAction(Percepts percepts) {

        isExecuting = true;

        double targetDirection = ((IntruderPercepts)percepts).getTargetDirection().getDegrees();
        double targetAngle = targetDirection > 180 ? targetDirection - 360 : targetDirection;

        if(Math.abs(targetAngle) > 10) {
            return ActionsFactory.getValidRotate(targetAngle, percepts);
        }

        double meanWallDistance = PerceptsService.getMeanDistance(PerceptsService.getWallPercepts(percepts));
        if(meanWallDistance < 2 * ActionsFactory.getMaxMoveDistance(percepts)) {
            stopExecuting();
            return new NoAction();
        }

        return ActionsFactory.getMaxMove(percepts);

    }

    public boolean shouldExecute(Percepts percepts) {
        return isExecuting || tillNextAttempt == 0;
    }

    public void updateState(Percepts percepts) {
        if(!percepts.wasLastActionExecuted() && isExecuting) {
            stopExecuting();
        }
        if(tillNextAttempt > 0) tillNextAttempt--;
    }

    private void stopExecuting() {
        isExecuting = false;
        tillNextAttempt = 50 + new Random().nextInt(TILL_NEXT_ATTEMPT);
    }

}
