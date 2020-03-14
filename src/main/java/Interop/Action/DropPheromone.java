package Interop.Action;

import Interop.Percept.Smell.SmellPerceptType;

/**
 * This class represents an intention of dropping a pheromone issued by an agent.
 *
 * After a dropping a pheromone an agent will enter a cool down period.
 */
public final class DropPheromone implements Action, IntruderAction, GuardAction {

    private SmellPerceptType type;

    public DropPheromone(SmellPerceptType type) {
        this.type = type;
    }

    public SmellPerceptType getType() {
        return type;
    }
    
}
