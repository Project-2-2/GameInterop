package Agent;

import Action.*;
import Percept.*;

/**
 * The common interface for agents.
 */
public interface Agent {

    /**
     * In order to decide an action the implementing agent receives percepts.
     *
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return The action that the agent decides on taking.
     */
    Action getAction(Percepts percepts);

}
