package Group5.Agent;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a (for now) single agent implementation of the exploratory agent described by
 * Maio and Rizzi in "A MULTI-AGENT APPROACH TO ENVIRONMENT EXPLORATION". The paper can be found here:
 * https://www.researchgate.net/publication/2294236_A_Multi-Agent_Approach_To_Environment_Exploration
 */
public class Explorer {

    private String name;
    private Graph g;
    private Vertex currentVertex;
    private Edge from;
    private String mode;
    private int level;

    /**
     * Use this constructor for an agent that has no information about his environment
     *
     * @param name          Name of the agent
     * @param level         This is the exploration level of the agent. See paper for details.
     * @param currentVertex The vertex that the agent finds itself at. For the constructor this corresponds to
     *                      a spawn point.
     */
    public Explorer(String name, int level, Vertex currentVertex) {
        this.name = name;
        this.level = level;
        this.currentVertex = currentVertex;
        this.g = new Graph(new ArrayList<>(), new ArrayList<>());
        this.g.addVertex(currentVertex);
        this.mode = "forward";
    }

    /**
     * Use this constructor for an agent that has prior information about the environment it is exploring.
     *
     * @param name          Name of the agent
     * @param level         This is the exploration level of the agent. See paper for details.
     * @param g             Graph to be explored. It is assumed that this graph is partially known to the agent.
     * @param currentVertex The vertex that the agent finds itself at. For the constructor this corresponds to
     *                      a spawn point.
     */
    public Explorer(String name, int level, Graph g, Vertex currentVertex) {
        this.name = name;
        this.level = level;
        this.currentVertex = currentVertex;
        this.g = g;
        this.g.addVertex(currentVertex);
        this.mode = "forward";
    }

    /**
     * Call this method to make the agent take its next step.
     *
     * @param map The graph representation of the map to be explored by the agent.
     */
    public void perform(Graph map) {
        List<Edge> adjacentEdges = map.getEdges(currentVertex.getId());
        Edge to = null;
        switch (this.mode) {
            case "forward":
                to = this.marchForward(adjacentEdges);
                break;
            case "backward":
                to = this.marchBackward(adjacentEdges);
                break;
        }
        this.g.addEdge(to);
        this.g.addVertex(to.getDestination());
        this.from = to;
        this.currentVertex = to.getDestination();

    }

    /**
     * @param adjacentEdges List of edges adjacent to the current vertex. For now, this is just retrieved from the input
     *                      Graph. Should be computed based on sensory input.
     * @return The next Edge to explore
     */
    private Edge marchForward(List<Edge> adjacentEdges) {
        Edge to = adjacentEdges.get(0);

        // You reach an unknown vertex
        if (!currentVertex.isVisited()) {
            this.currentVertex.setVisited(true);

            // You visit a new vertex, that is not a dead end.
            if (adjacentEdges.size() > 1) {
                this.from.setLabel('X');
                if (to.equals(from)) to = adjacentEdges.get(1);
                to.setLabel('N');
            }
            // You visit a new vertex that is a dead end
            else {
                to = this.from;
                this.mode = "backward";
            }
        }

        // You reach a known vertex
        else {
            this.from.setLabel('N');
            to = from;
            this.mode = "backward";
        }
        return to;
    }

    /**
     * @param adjacentEdges List of edges adjacent to the current vertex. For now, this is just retrieved from the input
     *                      Graph. Should be computed based on sensory input.
     * @return The next Edge to explore
     */
    private Edge marchBackward(List<Edge> adjacentEdges) {
        Edge to = adjacentEdges.get(0);

        // Check if any edge is unlabeled
        for (Edge e : adjacentEdges) {
            if (e.getLabel() == '\u0000') to = e;
        }

        // You are visiting a vertex with some unlabeled edges
        if (to.equals(adjacentEdges.get(0))) {
            for (Edge e : adjacentEdges) {
                if (e.getLabel() == 'X') {
                    to = e;
                    break;
                }
            }
        }

        return to;
    }


}
