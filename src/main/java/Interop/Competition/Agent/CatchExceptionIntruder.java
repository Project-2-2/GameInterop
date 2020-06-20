package Interop.Competition.Agent;

import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;
import Interop.Action.NoAction;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;

/**
 * Decorator that prevents crashing the game by errors in an agent implementation.
 *
 * @author Tomasz Darmetko
 */
public class CatchExceptionIntruder implements Intruder {

    private final Intruder intruder;

    public CatchExceptionIntruder(Intruder intruder) {
        this.intruder = intruder;
    }

    public IntruderAction getAction(IntruderPercepts percepts) {
        try {
            return intruder.getAction(percepts);
        } catch (Exception e) {
            System.err.println(e.toString());
            return new NoAction();
        }
    }

}