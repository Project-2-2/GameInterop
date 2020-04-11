package Group9.gui2;

import Group11.Control.GameController;
import Group11.Model.Map;
import Group11.Model.MapTranslator;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

import java.io.File;

public class MainController implements Runnable {
    private Gui gui;
    public boolean run = true;
    private Map map;
    private GameController gameController;
    public MainController(Gui gui){
        this.gui = gui;
        File file = new File(System.getProperty("user.dir")+"/src/main/java/Group11/MapFiles/map2.txt");
        map = MapTranslator.translate(file);
        gameController = new GameController(map);
    }
    @Override
    public void run() {
            AnimationTimer animator = new AnimationTimer(){
                @Override
                public void handle(long now){
                    Platform.runLater((Runnable) () -> {
                        gameController.runRound();
                        gui.drawMovables(gameController.getMovables());
                    });
                }};
            animator.start();
            //TODO: Simulate Game
    }


}