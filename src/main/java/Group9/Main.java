package Group9;

import Group9.map.GameMap;
import Group9.map.parser.Parser;
import Group9.math.Vector2;

public class Main {

    public static void main(String[] args) {
        GameMap gameMap = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        Game game = new Game(gameMap, 3);
        game.start();
    }


}
