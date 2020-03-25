package Group6.Agent.Intruder;

import Group6.Agent.RandomAgent;
import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Percept.IntruderPercepts;

/**
 * @author Tomasz Darmetko
 */
public class RandomIntruder extends RandomAgent implements Intruder {
    public IntruderAction getAction(IntruderPercepts percepts) {
        return (IntruderAction) getRandomAction(
            percepts,
            percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder(),
            percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle()
        );
    }
}
