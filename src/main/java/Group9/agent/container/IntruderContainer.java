package Group9.agent.container;

import Group9.gui.IntruderGui;
import Interop.Agent.Intruder;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.FieldOfView;

public class IntruderContainer extends AgentContainer<Intruder> {

    private boolean captured = false;
    private int zoneCounter = 0;

    public IntruderContainer(Intruder agent, Vector position, Vector direction, FieldOfView normalFOV) {
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
    public IntruderGui getGui(FieldOfView FOV)
    {
        IntruderGui gui = new IntruderGui(this.getShape().getCenter().getX(), this.getShape().getCenter().getY(), this.getShape().getRadius(), this.getDirection(), FOV.getRange().getValue());
        return gui;
    }

}
