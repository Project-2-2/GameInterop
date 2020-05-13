package Group6.Agent.Guard;

import Group6.Agent.Behaviour.Behaviour;
import Group6.Agent.BehaviourBasedAgent;
import Interop.Action.GuardAction;
import Interop.Agent.Guard;
import Interop.Percept.GuardPercepts;

import java.util.List;

/**
 * @author Tomasz Darmetko
 */
public class BehaviourBasedGuard extends BehaviourBasedAgent implements Guard {

    public BehaviourBasedGuard(List<Behaviour> behaviours) {
        super(behaviours);
    }

    public GuardAction getAction(GuardPercepts percepts) {
        return (GuardAction)getBehaviourBasedAction(percepts);
    }

}
