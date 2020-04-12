package Group9.gui2;

import Group9.Game;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.map.parser.Parser;
import javafx.animation.AnimationTimer;

import java.io.File;
import java.util.ArrayList;

public class MainController implements Runnable {

    private Gui gui;
    private Game game;
    private AnimationTimer animator;
    public MainController(Gui gui, File mapFile){
        this.gui = gui;
        game = new Game(Parser.parseFile(mapFile.getAbsolutePath()), new DefaultAgentFactory(), false, 15);
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void run() {
             animator = new AnimationTimer(){
                @Override
                public void handle(long now){
                    game.query((lock) -> {
                        gui.drawMovables(new ArrayList<>(game.getGuards()), new ArrayList<>(game.getIntruders()),
                                new ArrayList<>(game.getGameMap().getDynamicObjects()));
                    }, true);

                }};
            animator.start();
    }
    public void kill(){
        game.getRunningLoop().set(false);
        if(animator!=null){
            animator.stop();
        }
    }
    public void updateGameSpeed(int gameSpeed){
        game.getTicks().set(gameSpeed);
    }


}