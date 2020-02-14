package Agent;

import Action.*;
import Percept.*;

/**
 * The interface of a guard.
 *
 * You need to implement this interface in order to allow your agent play a role of a guard.
 * This interface limits your actions to the actions allowed by guards.
 */
public interface Guard extends Agent {
    GuardAction getAction(Percepts percepts);
}
