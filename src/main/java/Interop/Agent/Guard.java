package Interop.Agent;

import Interop.Action.*;
import Interop.Percept.*;

/**
 * The interface of a guard.
 *
 * You need to implement this interface in order to allow your agent play a role of a guard.
 * This interface limits your actions to the actions allowed by guards.
 */
public interface Guard {

    /**
     * In order to decide an action the implementing agent receives percepts.
     *
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return The action that the agent decides on taking.
     */
    GuardAction getAction(GuardPercepts percepts);

}
