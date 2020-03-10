package Group9.agent;

import Group9.math.graph.Graph;
import Interop.Action.GuardAction;
import Interop.Agent.Guard;
import Interop.Geometry.Vector;
import Interop.Percept.GuardPercepts;

public class ExplorerAgent extends AgentContainer<Guard> implements Guard {

    private final Graph<DataContainer> graph = new Graph<>();

    public ExplorerAgent(Guard guard, Vector position, Vector direction) {
        super(guard, position, direction);
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        return null;
    }


    private class DataContainer
    {
        // TODO this is supposed to contain what the agent sees at a certain point so it can be stored in the graph
        //  - it would be great if we could come up with some kind of hash function to match new data containers or subsets
        //      of them, so that we can figure out whether we have seen this space before which is kinda important because of
        //          teleports
    }

}
