package Group9.math.graph;

import java.util.*;

public class Graph<T> {

    private final List<Vertex<T>> vertices = new ArrayList<>();
    private final Map<Long, Edge<T>> edges = new HashMap<>();
    private final Map<Integer, List<Edge<T>>> neighbours = new HashMap<>();

    public Graph() {}

    public void add(Vertex<T> ...vertex)
    {
        this.vertices.addAll(Arrays.asList(vertex));
    }

    public void addEdge(Vertex<T> source, Vertex<T> target, double cost)
    {
        Edge<T> edge = new Edge<>(source, target, cost);
        //---
        long hash = 0L;
        hash |= source.hashCode();
        hash <<= 32;
        hash |= target.hashCode();

        this.edges.put(hash, edge);

        //---
        if(!this.neighbours.containsKey(source.hashCode()))
        {
            this.neighbours.put(source.hashCode(), new ArrayList<>());
        }

        this.neighbours.get(source.hashCode()).add(edge);
    }

    public List<Edge<T>> getNeighbours(Vertex<T> source)
    {
        return this.neighbours.getOrDefault(source.hashCode(), new ArrayList<>());
    }


    public List<Vertex<T>> shortestPath(Vertex<T> source, Vertex<T> target)
    {


        Map<Vertex<T>, Double> dist = new HashMap<>();
        Map<Vertex<T>, Vertex<T>> prev = new HashMap<>();
        Queue<Vertex<T>> Q = new PriorityQueue<>(Comparator.comparingDouble(o -> dist.getOrDefault(o, Double.MAX_VALUE)));
        Q.addAll(this.vertices);

        dist.put(source, 0D);

        while (!Q.isEmpty())
        {

            Vertex<T> u = Q.poll();

            final double currentDistance = dist.getOrDefault(u, Double.MAX_VALUE);

            for (Edge<T> v : getNeighbours(u))
            {
                if(Q.contains(v.getEnd()))
                {
                    double alt = currentDistance + v.getCost();

                    if(alt < dist.getOrDefault(v.getEnd(), Double.MAX_VALUE))
                    {
                        dist.put(v.getEnd(), alt);
                        prev.put(v.getEnd(), u);
                    }
                }
            }
        }

        List<Vertex<T>> path = new LinkedList<>();
        Vertex<T> u = target;
        while (u != null) {
            path.add(0, u);
            u = prev.getOrDefault(u, null);
        }

        return path;

    }

}
