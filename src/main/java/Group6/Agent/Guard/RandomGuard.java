package Group6.Agent.Guard;

import Group6.Agent.RandomAgent;
import Interop.Action.GuardAction;
import Interop.Agent.Guard;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;

public class RandomGuard extends RandomAgent implements Guard {
    public GuardAction getAction(GuardPercepts percepts) {
        return (GuardAction)getRandomAction(
            percepts,
            percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard(),
            percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle()
        );
    }
}
