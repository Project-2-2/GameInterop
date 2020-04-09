package Group9.agent.container;

import Group9.math.Vector2;
import Interop.Agent.Guard;
import Interop.Percept.Vision.FieldOfView;

public class ExplorerContainer extends AgentContainer<Guard> {

    public ExplorerContainer(Guard agent, Vector2 position, Vector2 direction, FieldOfView normalFOV) {
        super(agent, position, direction, normalFOV);
    }

}
