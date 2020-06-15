package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Action.NoAction;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

/**
 * @author Tomasz Darmetko
 */
public class FollowIntruderBehaviour implements Behaviour {

    public Action getAction(Percepts percepts) {

        ObjectPercepts intruderPercepts = PerceptsService.getIntruderPercepts(percepts);

        double meanDistance = PerceptsService.getMeanDistance(intruderPercepts);
        double towards = -1 * PerceptsService.getMeanDirection(intruderPercepts);
        if(Math.abs(towards) < Math.random() * 2) return ActionsFactory.getValidMove(
            PerceptsService.getMeanDistance(intruderPercepts), percepts
        );

//        if(meanDistance < PerceptsService.getCaptureDistance(percepts)) {
//            return ActionsFactory.getValidRotate(towards, percepts);
//        }

        double maxApproachAngle = 1 + Math.random() * 3;
        if(meanDistance > 0.5 * percepts.getVision().getFieldOfView().getRange().getValue()) {
            maxApproachAngle *= 2;
        }

        if(meanDistance < ActionsFactory.getMaxMoveDistance(percepts)) {
            maxApproachAngle = Math.random();
        }

        if(Math.abs(towards) < maxApproachAngle) {
            return ActionsFactory.getValidMove(meanDistance - 0.5, percepts);
        }

        return ActionsFactory.getValidRotate(towards, percepts);

    }

    public boolean shouldExecute(Percepts percepts) {
        return PerceptsService
            .getIntruderPercepts(percepts)
            .getAll()
            .size() > 0;
    }

    public void updateState(Percepts percepts) {
        // no op
    }

}
