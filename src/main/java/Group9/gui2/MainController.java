package Group9.gui2;

import Group9.Game;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.map.parser.Parser;
import javafx.animation.AnimationTimer;

import java.io.File;
import java.util.ArrayList;

public class MainController implements Runnable {

    private Gui gui;
    public boolean run = true;
    private Game game;
    public MainController(Gui gui){
        this.gui = gui;
        File file = new File("./src/main/java/Group9/map/maps/test_2.map");
        game = new Game(Parser.parseFile(file.getAbsolutePath()), new DefaultAgentFactory(), false, 5);

        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
            AnimationTimer animator = new AnimationTimer(){
                @Override
                public void handle(long now){
                    game.query((lock) -> {
                        gui.drawMovables(new ArrayList<>(game.getGuards()), new ArrayList<>(game.getIntruders()),
                                new ArrayList<>(game.getGameMap().getDynamicObjects()));
                    }, true);

                }};
            animator.start();
    }


}