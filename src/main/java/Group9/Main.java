package Group9;

import Group9.map.GameMap;
import Group9.map.parser.Parser;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;

public class Main {

    public static void main(String[] args) {

        Graph<String> graph = new Graph<>();
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Vertex<String> d = new Vertex<>("d");
        Vertex<String> e = new Vertex<>("e");
        Vertex<String> f = new Vertex<>("f");

        graph.add(a, b, c, d, e);

        graph.addEdge(a, b, 13);
        graph.addEdge(b, c, 1);
        graph.addEdge(a, c, 12);
        graph.addEdge(c, d, 3);

        System.out.println(graph.shortedPath(a, d));

        //GameMap gameMap = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        //gameMap.toString();

        //\left(-60,-40\right),\left(-60,-39\right),\left(60,-39\right),\left(60,-40\right)

        /*System.out.println(PointContainer.intersect(
            new PointContainer.Quadrilateral(
                new Vector2(-60, -40), new Vector2(-60, -39), new Vector2(60, -39), new Vector2(60, -40)
            ),
            //\left(-55,-39.5\right),\left(-53,-39.5\right),\left(-55,-37.5\right),\left(-53,-37.5\right)
            new PointContainer.Quadrilateral(
                new Vector2(-55, -39.5), new Vector2(-55, -37.5), new Vector2(-53, -37.5), new Vector2(-53, -39.5)
            )

        ));
            /*new PointContainer.Quadrilateral(
                    new Vector2(-4, -28), new Vector2(0, -28), new Vector2(0, -29), new Vector2(-4, -29)
            )*/
        //TODO limit tree depth
        //TODO check whether or not the order of the points matter

    }


}
