package Group8.Agent;

import Group8.Controller.Utils.AgentInfo;
import Interop.Geometry.Point;

public abstract class Agent {
    private AgentInfo agentInfo;

    public Agent(AgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    public void setLocation(Point location) {
        agentInfo.setActualPos(location);
    }

    public Point getLocation() {
        return agentInfo.getActualPos();
    }
}
