package Group9.agent.container;

import Group9.math.graph.Graph;
import Interop.Action.GuardAction;
import Interop.Agent.Guard;
import Interop.Geometry.Vector;
import Interop.Percept.GuardPercepts;

public class ExplorerContainer extends AgentContainer<Guard> {

    private final Graph<AgentContainer.DataContainer> graph = new Graph<>();

    public ExplorerContainer(Guard agent, Vector position, Vector direction) {
        super(agent, position, direction);
    }

}
