package Group6.Agent;

import Group6.Agent.Behaviour.Behaviour;
import Interop.Action.Action;
import Interop.Action.NoAction;
import Interop.Percept.Percepts;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Darmetko
 */
public abstract class BehaviourBasedAgent {

    private final List<Behaviour> behaviours;

    public BehaviourBasedAgent(List<Behaviour> behaviours) {
        this.behaviours = behaviours;
    }

    protected Action getBehaviourBasedAction(Percepts percepts) {
        updateBehaviours(percepts);
        for (Behaviour behaviour: behaviours) {
            if(behaviour.shouldExecute(percepts)) return behaviour.getAction(percepts);
        }
        return new NoAction();
    }

    private void updateBehaviours(Percepts percepts) {
        for(Behaviour behaviour: behaviours) {
            behaviour.updateState(percepts);
        }
    }

}
