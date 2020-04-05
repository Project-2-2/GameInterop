package Group5.Agent;

import Group5.GameController.AgentController;
import Group5.GameController.Area;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public static double minimumDistance = 7.1913; //distance two rays
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;
    private AgentController agent;

    public Graph(AgentController agent) {
        this.agent = agent;
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }

    public void addVertex(Vertex v) {
        if (checkNewEdges(v)) {
            this.vertices.add(v);
        }

    }

    /**
     * Every time you add a new vertex to the graph, this method checks if it's a view distance from other objects
     * @param newVertex vertex that was added to the graph
     */
    public Boolean checkNewEdges(Vertex newVertex) {
        ArrayList<Vertex> vertexInRange = new ArrayList<>();
        ArrayList<Double> distances = new ArrayList<>();

        boolean partOfAnotherObject = false;

        for (Vertex v: vertices) {
            double distance = getDistance(v, newVertex);

            if (distance < minimumDistance &&
                    v.getObject().getType() == newVertex.getObject().getType()) {
                partOfAnotherObject = true;
                combineTwoVertex(v, newVertex, v.getObject().getType());

            }else if (distance <= agent.getViewRange().getValue()) {
                distances.add(distance);
                vertexInRange.add(v);
            }
        }

        if (!partOfAnotherObject) {
            for (int i = 0; i < vertexInRange.size(); i++) {
                addEdge(new Edge(vertexInRange.get(i), newVertex, distances.get(i)));
            }
        }

        return partOfAnotherObject;
    }

    /**
     * If the two objectPercept are recognize as being part of the same object we combine them to get Areas
     * @param v already saved vertex
     * @param toCombine vertex containing the objectPercept to combine to v
     */
    public void combineTwoVertex(Vertex v, Vertex toCombine, ObjectPerceptType type) {
        if (v.isAreaVertex()) {
            v.getArea().addPoint(toCombine.getObject().getPoint());
        }else {
            Point point1 = v.getObject().getPoint();
            Point point2 = toCombine.getObject().getPoint();
            vertices.add(new AreaVertex(true, new Area(point1.getX(), point1.getY(), point2.getX(), point2.getY(), type)));
            removeVertex(v);
        }


    }

    public static double getDistance(Vertex v1, Vertex v2) {

        return Math.sqrt(Math.pow(v1.getObject().getPoint().getX() - v2.getObject().getPoint().getX(), 2) +
                Math.pow(v1.getObject().getPoint().getY() - v2.getObject().getPoint().getY(), 2));
    }

    public void removeVertex(Vertex v) {
        ArrayList<Edge> newEdges = new ArrayList<>();
        for (Edge edge: this.edges) {
            if (!edge.contains(v)) {
               newEdges.add(edge);
            }
        }
        this.edges = newEdges;
        this.vertices.remove(v);
    }




    /*
    public boolean adjacent(String x, String y) {
        for (Edge e : edges) {
            if (e.getVerticies().get(0).getId().equals(x) && e.getVerticies().get(1).getId().equals(y))
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

    public List<Edge> getEdgesDirected(String vertex) {
        List<Edge> adjacentEdges = new ArrayList<>();
        for (Edge e : edges) {
            if (e.getSource().getId().equals(vertex))
                adjacentEdges.add(e);
            else if (e.getSource().getName().equals(vertex))
                adjacentEdges.add(e);
        }
        return adjacentEdges;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Graph other = (Graph) obj;

        for(Vertex v: other.getVertexes()){
            if(!this.vertexes.contains(v)) return false;
        }

        for(Edge e: other.getEdges()){
            if(!this.edges.contains(e)) return false;
        }

        return true;

    }

     */


}