package Group9.agent.container;

import Interop.Agent.Intruder;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;

public class IntruderContainer extends AgentContainer<Intruder> {

    private boolean captured = false;
    private int zoneCounter = 0;

    public IntruderContainer(Intruder agent, Point position, Point direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }

    public int getZoneCounter()
    {
        return this.zoneCounter;
    }

    public boolean isCaptured()
    {
        return this.captured;
    }

    public void setZoneCounter(int zoneCounter)
    {
        this.zoneCounter = zoneCounter;
    }

    public void setCaptured(boolean captured)
    {
        this.captured = captured;
    }

}
