package Group9;

import Group9.map.GameMap;
import Group9.map.parser.Parser;
import Group9.math.Line;
import Group9.math.Vector2;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import Group9.tree.PointContainer;

public class Main {

    public static void main(String[] args) {
        GameMap gameMap = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        Game game = new Game(gameMap, 3);
        game.start();
    }


}
