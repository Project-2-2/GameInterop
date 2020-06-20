package Interop.Competition.Agent;

import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;
import Interop.Action.NoAction;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;

/**
 * Agent that is doing nothing - a placeholder for a true agent.
 *
 * @author Tomasz Darmetko
 */
public class SittingDuckAgent implements Intruder, Guard {
    public IntruderAction getAction(IntruderPercepts percepts) {
        return new NoAction();
    }
    public GuardAction getAction(GuardPercepts percepts) {
        return new NoAction();
    }
}