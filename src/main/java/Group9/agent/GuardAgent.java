package Group9.agent;

import Interop.Action.GuardAction;
import Interop.Agent.Guard;
import Interop.Geometry.Vector;
import Interop.Percept.GuardPercepts;

public class GuardAgent extends AgentContainer<Guard> implements Guard {

    public GuardAgent(Guard agent, Vector position, Vector direction) {
        super(agent, position, direction);
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        return null;
    }

}
