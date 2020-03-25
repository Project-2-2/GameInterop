package Group9;

import Group9.map.GameMap;
import Group9.map.parser.Parser;
import Group9.math.Vector2;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import Group9.tree.PointContainer;


public class Main {

    public static void main(String[] args) {

        /*Graph<String> graph = new Graph<>();
        Vertex<String> a = new Vertex<>("a");
        Vertex<String> b = new Vertex<>("b");
        Vertex<String> c = new Vertex<>("c");
        Vertex<String> d = new Vertex<>("d");
        Vertex<String> e = new Vertex<>("e");
        Vertex<String> __ = new Vertex<>("__");

        graph.add(a, b, c, d, e, __);
        graph.addEdge(a, b, 10, true);
        graph.addEdge(a, d, 30, true);
        graph.addEdge(b, c, 20.5, true);
        graph.addEdge(c, d, 5, true);
        graph.addEdge(d, e, 5, true);

        graph.shortestPath(a, __);*/

        // How to read in a map from a file
        //GameMap map = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");

        GameMap gameMap = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");

        Game game = new Game(gameMap, 1);
        game.run();
        //game.start();

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
