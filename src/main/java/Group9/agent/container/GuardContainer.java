package Group9.agent.container;

import Group9.gui.GuardGui;
import Interop.Agent.Guard;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.FieldOfView;

public class GuardContainer extends AgentContainer<Guard> {

    public GuardContainer(Guard agent, Vector position, Vector direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }
    public GuardGui getGui(FieldOfView FOV)
    {
        GuardGui gui = new GuardGui(this.getShape().getCenter().getX(), this.getShape().getCenter().getY(), this.getShape().getRadius(), this.getDirection(), FOV.getRange().getValue());
        return gui;
    }

}
