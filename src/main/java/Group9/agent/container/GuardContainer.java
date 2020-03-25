package Group9.agent.container;

import Interop.Agent.Guard;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;

public class GuardContainer extends AgentContainer<Guard> {

    public GuardContainer(Guard agent, Point position, Point direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }

}
