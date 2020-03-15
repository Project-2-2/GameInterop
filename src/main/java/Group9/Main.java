package Group9;

import Group9.map.GameMap;
import Group9.map.parser.Parser;

public class Main {

    public static void main(String[] args) {
        GameMap gameMap = Parser.parseFile("./src/main/java/Group9/map/maps/test.map");
        Game game = new Game(gameMap, 1);
        game.start();
    }


}
