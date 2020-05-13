package Group6.Agent.Intruder;

import Group6.Agent.Behaviour.Behaviour;
import Group6.Agent.BehaviourBasedAgent;
import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Percept.IntruderPercepts;

import java.util.List;

/**
 * @author Tomasz Darmetko
 */
public class BehaviourBasedIntruder extends BehaviourBasedAgent implements Intruder {

    public BehaviourBasedIntruder(List<Behaviour> behaviours) {
        super(behaviours);
    }

    public IntruderAction getAction(IntruderPercepts percepts) {
        return (IntruderAction)getBehaviourBasedAction(percepts);
    }

}
