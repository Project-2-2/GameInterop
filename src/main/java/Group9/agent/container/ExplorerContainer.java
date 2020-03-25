package Group9.agent.container;

import Interop.Agent.Guard;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;

public class ExplorerContainer extends AgentContainer<Guard> {

    public ExplorerContainer(Guard agent, Point position, Point direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }

}
