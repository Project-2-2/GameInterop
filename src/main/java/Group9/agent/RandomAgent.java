package Group9.agent;

import Group9.Game;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;

public class RandomAgent implements Guard {

    public RandomAgent() {}

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if(!percepts.wasLastActionExecuted())
        {
            System.out.println("rotate");
            return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
        }
        else
        {
            System.out.println("move");
            return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()));
        }
    }

}
