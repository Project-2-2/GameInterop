package Group9.agent.container;

import Interop.Agent.Intruder;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.FieldOfView;

public class IntruderContainer extends AgentContainer<Intruder> {

    public IntruderContainer(Intruder agent, Vector position, Vector direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }

}
