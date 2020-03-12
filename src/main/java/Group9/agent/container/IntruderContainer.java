package Group9.agent.container;

import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Geometry.Vector;
import Interop.Percept.IntruderPercepts;

public class IntruderContainer extends AgentContainer<Intruder> {

    private int sprintCooldown = -1;

    public IntruderContainer(Intruder agent, Vector position, Vector direction) {
        super(agent, position, direction);
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
