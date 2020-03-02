package Group5.Agent;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Vertex> vertexes;
    private final List<Edge> edges;

    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public boolean adjacent(String x, String y) {
        for (Edge e : edges) {
            if (e.getSource().getId().equals(x) && e.getDestination().getId().equals(y))
                return true;
            else if (e.getSource().getId().equals(y) && e.getDestination().getId().equals(x))
                return true;
            else if (e.getSource().getName().equals(x) && e.getDestination().getName().equals(y))
                return true;
            else if (e.getSource().getName().equals(y) && e.getDestination().getName().equals(x))
                return true;
        }

        return false;
    }

    public List<Vertex> getNeighbours(String vertex) {
        List<Vertex> neighbours = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getSource().getId().equals(vertex))
                neighbours.add(e.getDestination());
            else if (e.getDestination().getId().equals(vertex))
                neighbours.add(e.getSource());
            else if (e.getSource().getName().equals(vertex))
                neighbours.add(e.getDestination());
            else if (e.getDestination().getName().equals(vertex))
                neighbours.add(e.getSource());
        }
        return neighbours;
    }

    public List<Edge> getEdges(String vertex) {
        List<Edge> adjacentEdges = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getSource().getId().equals(vertex))
                adjacentEdges.add(e);
            else if (e.getDestination().getId().equals(vertex))
                adjacentEdges.add(e);
            else if (e.getSource().getName().equals(vertex))
                adjacentEdges.add(e);
            else if (e.getDestination().getName().equals(vertex))
                adjacentEdges.add(e);
        }
        return adjacentEdges;
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }

    public void addVertex(Vertex v) {
        this.vertexes.add(v);
    }


}