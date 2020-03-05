package Group5.Agent;

public class Edge implements Comparable<Edge> {
    private final String id;
    private final Vertex source;
    private final Vertex destination;
    private final int weight;
    private Edge mirror;

    public Edge(String id, Vertex source, Vertex destination, int weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge o) {
        if (this.weight < o.getWeight())
            return -1;
        else
            return 1;
    }

    public String getId() {
        return id;
    }

    public Vertex getDestination() {
        return destination;
    }

    public Vertex getSource() {
        return source;
    }

    public int getWeight() {
        return weight;
    }

    public Edge getMirror() {
        return mirror;
    }

    public void setMirror(Edge mirror) {
        this.mirror = mirror;
        mirror.setMirrorNoRecursion(this);
    }

    private void setMirrorNoRecursion(Edge mirror){
        this.mirror = mirror;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }


}