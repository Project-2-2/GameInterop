package Group9.math.graph;

import java.util.*;

public class Graph<T> {

    private final List<Vertex<T>> vertices = new ArrayList<>();
    private final Map<Long, Edge<T>> edges = new HashMap<>();
    private final Map<Integer, List<Edge<T>>> neighbours = new HashMap<>();

    public Graph() {}

    public List<Vertex<T>> getVertices() {
        return vertices;
    }

    public boolean has(Vertex<T> vertex)
    {
        return this.vertices.contains(vertex);
    }

    public boolean has(Vertex<T> source, Vertex<T> target)
    {
        return this.edges.containsKey(calculateEdgeHash(source, target));
    }

    public Edge<T> get(Vertex<T> source, Vertex<T> target)
    {
        return this.edges.getOrDefault(calculateEdgeHash(source, target), null);
    }

    @SafeVarargs
    public final boolean add(Vertex<T>... vertices)
    {
        for(Vertex<T> v : vertices)
        {
            if(has(v))
            {
                return false;
            }
        }
        this.vertices.addAll(Arrays.asList(vertices));
        return true;
    }

    public boolean add(Vertex<T> source, Vertex<T> target, double cost, boolean undirected)
    {
        Edge<T> edge = new Edge<>(source, target, cost);

        if(has(source, target))
        {
            return false;
        }

        this.edges.put(calculateEdgeHash(source, target), edge);

        //---
        if(!this.neighbours.containsKey(source.hashCode()))
        {
            this.neighbours.put(source.hashCode(), new ArrayList<>());
        }

        this.neighbours.get(source.hashCode()).add(edge);

        if(undirected)
        {
            return add(target, source, cost, false);
        }

        return true;
    }

    public List<Edge<T>> getNeighbours(Vertex<T> source)
    {
        return this.neighbours.getOrDefault(source.hashCode(), new ArrayList<>());
    }


    public List<Vertex<T>> shortestPath(Vertex<T> source, Vertex<T> target)
    {


        Map<Vertex<T>, Double> dist = new HashMap<>();
        Map<Vertex<T>, Vertex<T>> prev = new HashMap<>();
        dist.put(source, 0D);
        Queue<Vertex<T>> Q = new LinkedList<>(this.vertices);

        while (!Q.isEmpty())
        {

            Vertex<T> u = Q.stream().min(Comparator.comparingDouble(o -> dist.getOrDefault(o, Double.MAX_VALUE))).get();
            Q.remove(u);
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

        //--- if |path| = 1 that means that only the target has been added and thus no path exists
        if(path.size() == 1)
        {
            return null;
        }

        return path;

    }

    private final static long calculateEdgeHash(Vertex<?> source, Vertex<?> target)
    {
        //---
        long hash = 0L;
        hash |= source.hashCode();
        hash <<= 32;
        hash |= target.hashCode();
        return hash;
    }

}
