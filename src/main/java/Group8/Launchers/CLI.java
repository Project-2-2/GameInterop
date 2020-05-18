package Group8.Launchers;

import Group9.Game;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.map.parser.Parser;

public class CLI {
    public static void main(String[] args) {
        // TODO: Create a game object
        // TODO: Create a Thread that takes the game object as an argument
        // TODO: Run the created thread
        Game game = new Game(Parser.parseFile("./src/main/java/Group9/map/maps/TestMapEasy.map"), new DefaultAgentFactory(), false);
        game.run();
        System.out.printf("The winner is: %s\n", game.getWinner());
    }
}
