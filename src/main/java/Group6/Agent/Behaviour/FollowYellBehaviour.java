package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Group6.Geometry.Direction;
import Interop.Action.Action;
import Interop.Action.NoAction;
import Interop.Percept.Percepts;
import Interop.Utils.Utils;

/**
 * @author Tomasz Darmetko
 */
public class FollowYellBehaviour implements Behaviour {
    public Action getAction(Percepts percepts) {

        double yellDirection = PerceptsService.getMeanDirection(
            PerceptsService.getYellPercepts(percepts)
        );

        double targetAngle = yellDirection > 180 ? yellDirection - 360 : yellDirection;

        return ActionsFactory.getValidRotate(targetAngle, percepts);

    }

    public boolean shouldExecute(Percepts percepts) {
        return PerceptsService.getYellPercepts(percepts).getAll().size() > 0;
    }

    public void updateState(Percepts percepts) {
        // NO OP
    }

}
