package Group8.Controller;

import Group8.Controller.Utils.Scenario;
import Interop.Geometry.Point;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class launches a graphical user interface which shows the simulation of the project
 */
public class LauncherGUI extends Application {
    private static final int FPS = 60;
    public static final int DRAW_CONSTANT = 5;
    private Point mapDimension;
    private Group root;
    private Scenario scenario;
    private static final int frameTimeMillis = 6000/FPS;

    @Override
    public void start(Stage stage) throws Exception {
        scenario = G8Launcher.Scenario;
        //Retrieves the scene dimensions
        mapDimension = scenario.getMapDimension();

        //Creates the content pane containing all the elements
        root = scenario.getContentPane();


        //Creates the scene for the GUI
        Scene scene = new Scene(root, 1200, 800);

        //scene.setOnMouseClicked(event -> System.out.printf("X: %f & Y: %f\n",event.getX(),event.getY()));
        stage.setScene(scene);
        stage.show();

        prepareAnimation();

    }

    public void prepareAnimation(){
        Timeline timer = new Timeline(
                new KeyFrame(Duration.millis(frameTimeMillis), e -> {
                    Controller.gameStep();
                }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public Group getRoot(){
        return this.root;
    }
}