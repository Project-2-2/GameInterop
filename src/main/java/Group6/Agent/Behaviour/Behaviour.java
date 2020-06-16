package Group6.Agent.Behaviour;

import Interop.Action.Action;
import Interop.Percept.Percepts;

/**
 * Allows to implement a behaviour.
 *
 * Behaviour can maintain a state.
 * Behaviour can decide when to execute.
 * Behaviour can decide action should be taken.
 *
 * @author Tomasz Darmetko
 */
public interface Behaviour {
    Action getAction(Percepts percepts);
    boolean shouldExecute(Percepts percepts);
    void updateState(Percepts percepts);
}