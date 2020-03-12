package Group9.agent;

import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Geometry.Vector;
import Interop.Percept.IntruderPercepts;

public class IntruderAgent extends AgentContainer<Intruder> implements Intruder {

    private int sprintCooldown = -1;

    public IntruderAgent(Intruder intruder, Vector position, Vector direction) {
        super(intruder, position, direction);
    }

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        return null;
    }

    public int getSprintCooldown()
    {
        return sprintCooldown;
    }

    public void setSprintCooldown(int sprintCooldown)
    {
        this.sprintCooldown = sprintCooldown;
    }


}
