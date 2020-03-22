package Group9.agent.container;

import Interop.Agent.Guard;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.FieldOfView;

public class GuardContainer extends AgentContainer<Guard> {

    public GuardContainer(Guard agent, Vector position, Vector direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }

}
