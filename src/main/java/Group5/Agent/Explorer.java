package Group5.Agent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class is executes a simple Breadth First Search to explore the graph representation of the map to explore.
 * The idea of the graph being explored on multiple levels is taken from Maio and Rizzi's "A MULTI-AGENT APPROACH TO
 * ENVIRONMENT EXPLORATION". The paper can be found here:
 * https://www.researchgate.net/publication/2294236_A_Multi-Agent_Approach_To_Environment_Exploration
 */
public class Explorer {

    private Queue<Vertex> queue;
    private Graph g;
    private int level;

    /**
     * Use this constructor for an agent that has no information about his environment
     *
     * @param level This is the exploration level of the agent. See paper for details.
     */
    public Explorer(int level) {
        this.level = level;
        this.g = new Graph(new ArrayList<>(), new ArrayList<>());
        this.queue = new LinkedList<>();
    }

    /**
     * Use this constructor for an agent that has prior information about the environment it is exploring.
     *
     * @param level         This is the exploration level of the agent. See paper for details.
     * @param g             Graph to be explored. It is assumed that this graph is partially known to the agent.
     */
    public Explorer(int level, Graph g, Queue<Vertex> queue, Vertex currentVertex) {
        this.level = level;
        this.queue = queue;
        this.g = g;
        this.g.addVertex(currentVertex);
    }

    public Graph getG() {
        return g;
    }

    public void BFSWrapper(Graph map, Vertex start){
        start.setVisited(true);
        this.queue.add(start);

        while(!queue.isEmpty()){
            Vertex v = queue.remove();
            List<Edge> adjacentEdges = map.getEdgesDirected(v.getId());
            BFS(v, adjacentEdges);
        }

    }

    public void BFS(Vertex v, List<Edge> adjacentEdges){
        if(!g.getVertexes().contains(v)) g.addVertex(v);
        for (Edge e: adjacentEdges){
            if(!g.getEdges().contains(e))g.addEdge(e);
            Vertex w = e.getDestination();
            if(!w.isVisited()) {
                w.setVisited(true);
                queue.add(w);
            }
        }
    }


}
