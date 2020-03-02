package Group5;

public class Edge implements Comparable<Edge> {
    private final String id;
    private final Vertex source;
    private final Vertex destination;
    private final int weight;
    private char label;

    public Edge(String id, Vertex source, Vertex destination, int weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge o) {
        if(this.weight < o.getWeight())
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

    public void setLabel(char label) {
        assert(label == 'N' || label == 'X');
        this.label = label;
    }

    public char getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return source + " " + destination;
    }


}