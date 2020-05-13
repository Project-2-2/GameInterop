package Group9;

import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import SimpleUnitTest.SimpleUnitTest;

import java.util.List;
import java.util.function.Function;

public class GraphTest extends SimpleUnitTest {

    public static void main(String[] args) {

        it("Graph::new", GraphTest::test_new);
        it("Graph::add (vertices)", GraphTest::test_add_vertices);
        it("Graph::add (edges)", GraphTest::test_add_edges);
        it("Graph::shortestPath", GraphTest::test_shortestPath);

    }

    private static void test_new() {
        Graph<String> graph = new Graph<>();

        assertEqual(graph.getVertices().size(), 0, "graph shouldn't have any initial vertices");
    }

    private static void test_add_vertices() {
        Graph<String> graph = new Graph<>();

        Vertex<String> _a = new Vertex<>("a");
        Vertex<String> _b = new Vertex<>("b");
        Vertex<String> _c = new Vertex<>("c");
        Vertex<String> _d = new Vertex<>("d");
        Vertex<String> _e = new Vertex<>("e");
        Vertex<String> _f = new Vertex<>("f");
        Vertex<String> _g = new Vertex<>("g");

        assertTrue(graph.add(_a, _b, _c, _d, _e, _f, _g), "Returns true if it was successful");

        assertTrue(graph.getVertices().contains(_a), "Node 'a' should exist");
        assertTrue(graph.getVertices().contains(_b), "Node 'b' should exist");
        assertTrue(graph.getVertices().contains(_c), "Node 'c' should exist");
        assertTrue(graph.getVertices().contains(_d), "Node 'd' should exist");
        assertTrue(graph.getVertices().contains(_e), "Node 'e' should exist");
        assertTrue(graph.getVertices().contains(_f), "Node 'f' should exist");
        assertTrue(graph.getVertices().contains(_g), "Node 'g' should exist");
        assertEqual(graph.getVertices().size(), 7, "graph should have 7 vertices now");
    }

    private static void test_shortestPath() {
        Graph<String> graph = new Graph<>();


        Vertex<String> _a = new Vertex<>("a");
        Vertex<String> _b = new Vertex<>("b");
        Vertex<String> _c = new Vertex<>("c");
        Vertex<String> _d = new Vertex<>("d");
        Vertex<String> _e = new Vertex<>("e");
        Vertex<String> _f = new Vertex<>("f");
        Vertex<String> _g = new Vertex<>("g"); //Note: This vertex has on purpose no edges.

        graph.add(_a, _b, _c, _d, _e, _f, _g);


        //--- Creates this graph https://upload.wikimedia.org/wikipedia/commons/5/57/Dijkstra_Animation.gif
        graph.add(_a, _b, 7, true);
        graph.add(_a, _c, 9, true);
        graph.add(_a, _f, 14, true);
        graph.add(_b, _c, 10, true);
        graph.add(_b, _d, 15, true);
        graph.add(_c, _d, 11, true);
        graph.add(_c, _f, 2, true);
        graph.add(_d, _e, 6, true);
        graph.add(_e, _f, 9, true);

        {
            List<Vertex<String>> path = graph.shortestPath(_a, _e);
            assertEqual(path.size(), 4, "Path length should be 4 (a -> c -> f -> e)");
            assertTrue(path.get(0) == _a);
            assertTrue(path.get(1) == _c);
            assertTrue(path.get(2) == _f);
            assertTrue(path.get(3) == _e);
        }

        {
            List<Vertex<String>> path = graph.shortestPath(_a, _a);
            assertTrue(path == null, "There is no path between a vertex and itself.");
        }

        {
            List<Vertex<String>> path = graph.shortestPath(_a, _g);
            assertTrue(path == null, "_g is not connected in any way to the graph thus no path" +
                    " is possible.");
        }
    }

    private static void test_add_edges() {
        Graph<String> graph = new Graph<>();

        Vertex<String> _a = new Vertex<>("a");
        Vertex<String> _b = new Vertex<>("b");
        Vertex<String> _c = new Vertex<>("c");
        Vertex<String> _d = new Vertex<>("d");
        Vertex<String> _e = new Vertex<>("e");
        Vertex<String> _f = new Vertex<>("f");
        Vertex<String> _g = new Vertex<>("g");

        graph.add(_a, _b, _c, _d, _e, _f, _g);

        assertTrue(graph.add(_a, _b, 7, true));
        assertTrue(graph.add(_a, _c, 9, true));
        assertTrue(graph.add(_a, _f, 14, true));
        assertTrue(graph.add(_b, _c, 10, true));
        assertTrue(graph.add(_b, _d, 15, true));
        assertTrue(graph.add(_c, _d, 11, true));
        assertTrue(graph.add(_c, _f, 2, true));
        assertTrue(graph.add(_d, _e, 6, true));
        assertTrue(graph.add(_e, _f, 9, true));

        Function<Vertex<String>[], Void> testEdge = vertices -> {
            assertTrue(graph.has(vertices[0], vertices[1]), String.format("has(%s,%s) returns true if successful",
                    vertices[0].getContent(), vertices[1].getContent()));
            assertTrue(graph.has(vertices[1], vertices[0]), String.format("has(%s,%s) returns true if successful",
                    vertices[1].getContent(), vertices[0].getContent()));
            assertTrue(graph.get(vertices[0], vertices[1]) != null, String.format("get(%s,%s) should not be null",
                    vertices[0].getContent(), vertices[1].getContent()));
            assertTrue(graph.get(vertices[1], vertices[0]) != null, String.format("get(%s,%s) should not be null",
                    vertices[1].getContent(), vertices[0].getContent()));
            assertEqual(graph.get(vertices[0], vertices[1]).getCost(), graph.get(vertices[1], vertices[0]).getCost(),
                    0, String.format("cost should be the same for %s <-> %s", vertices[0].getContent(), vertices[1].getContent()));
            return null;
        };

        testEdge.apply(new Vertex[]{_a, _b});
        testEdge.apply(new Vertex[]{_a, _c});
        testEdge.apply(new Vertex[]{_a, _f});
        testEdge.apply(new Vertex[]{_b, _c});
        testEdge.apply(new Vertex[]{_b, _d});
        testEdge.apply(new Vertex[]{_c, _d});
        testEdge.apply(new Vertex[]{_c, _f});
        testEdge.apply(new Vertex[]{_d, _e});
        testEdge.apply(new Vertex[]{_e, _f});

        assertTrue(!(graph.has(_a, _g) || graph.has(_b, _g) || graph.has(_c, _g) || graph.has(_d, _g) || graph.has(_e, _g) || graph.has(_f, _g)),
                "No vertex has an edge to 'g'");
    }
}
