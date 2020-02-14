package Agent;

import Action.*;
import Percept.*;

/**
 * The interface of an intruder.
 *
 * You need to implement this interface in order to allow your agent play a role of am intruder.
 * This interface limits your actions to the actions allowed by intruders.
 */
public interface Intruder extends Agent {
    IntruderAction getAction(Percepts percepts);
}
