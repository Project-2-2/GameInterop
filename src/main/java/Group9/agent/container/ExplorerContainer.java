package Group9.agent.container;

import Interop.Agent.Guard;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.FieldOfView;

public class ExplorerContainer extends AgentContainer<Guard> {

    public ExplorerContainer(Guard agent, Vector position, Vector direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }

}
