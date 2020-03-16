package Group9.agent;

import Group9.Game;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Percept.GuardPercepts;

public class ExplorerAgent implements Guard {
    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if(percepts.wasLastActionExecuted())
        {
            System.out.println("move");
            return new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
        }
        else
        {
            System.out.println("rotate");
            return new Rotate(Angle.fromRadians(
                percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()
            ));
        }
    }
}
