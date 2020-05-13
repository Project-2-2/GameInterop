package Group8.Launchers.GUI;


import Group9.Game;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.dynamic.DynamicObject;
import Group9.map.parser.Parser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class GUI extends Application{
    //If we need to pass a specific factory make sure to use -1 for ticks since that will be the fastest
    private final Game game = new Game(Parser.parseFile("./src/main/java/Group9/map/maps/test_2.map"),false);

    private final GameScene scene = new GameScene(new StackPane());

    private AnimationTimer timer;


    /**
     * Launches the GUI
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(600);
        stage.setWidth(800);
        stage.setTitle("G8");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("GUI shutting down...");
        });

        // The following code was heavily inspired by the GUI implementation of the common controller
        timer = new AnimationTimer(){
            @Override
            public void handle(long l) {
                game.query((lock) -> {
                    drawMovables(new ArrayList<>(game.getGuards()), new ArrayList<>(game.getIntruders()),
                            new ArrayList<>(game.getGameMap().getDynamicObjects()));
                },true);
            }
        };
        timer.start();
    }


    private void drawMovables(List<GuardContainer> guards, List<IntruderContainer> intruders, List<DynamicObject<?>> objects){
        //Implement this somehow....
        scene.drawRect();
    }
}

