package Group8.Launchers;


import Group8.Launchers.GUIRes.GameScene;
import Group9.Game;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.agent.factories.DefaultAgentFactory;
import Group9.gui2.Gui;
import Group9.map.dynamic.DynamicObject;
import Group9.map.parser.Parser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class GUI extends Application{

    public static final boolean USE_OWN_GUI = false;

    private final int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;

    // If we need to pass a specific factory make sure to use -1 for ticks since that will be the fastest
    // DefaultAgentFactory needs to be replaced!
    private final Game game = new Game(Parser.parseFile("./src/main/java/Group9/map/maps/TestMapEasy.map"),new DefaultAgentFactory(), false, 15,null);

    private final GameScene scene = new GameScene(new StackPane(),game.getGameMap());

    private AnimationTimer timer;




    /**
     * Launches the GUI
     * @param args
     */
    public static void main(String[] args) {
        if(USE_OWN_GUI) {
            launch(args);
        }
        else{
            Gui.Gui(args);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("OWN GUI");
        stage.setHeight(DEFAULT_HEIGHT);
        stage.setWidth(DEFAULT_WIDTH);
        stage.setTitle("G8");
        stage.setScene(scene);
        scene.attachWindow(stage);
        scene.rescale();
        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("GUI shutting down...");
        });

        Thread th = new Thread(game);
        th.start();

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
        scene.clearForeground();
        scene.drawEntities(guards, intruders, objects);
    }
}



