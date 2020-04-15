package Group9.math.graph;

public class Edge<T> {

    private final Vertex<T> start;
    private final Vertex<T> end;
    private final double cost;

    public Edge(Vertex<T> start, Vertex<T> end, double cost)
    {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    public Vertex<T> getStart()
    {
        return this.start;
    }

    public Vertex<T> getEnd()
    {
        return this.end;
    }

    public double getCost()
    {
        return this.cost;
    }

}
