package Group11;


import Group9.Game;
import Group9.map.parser.Parser;

public class Main {

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            Game game = new Game(Parser.parseFile("./src/main/java/Group11/MapFiles/map2.map"), new CoolAgentFactory(), false);
            game.run();
            System.out.printf("The winner is: %s\n", game.getWinner());
        }
    }


}