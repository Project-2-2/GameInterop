package Group6.GUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
public class GUI extends Application{

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("surveillance system");
        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        GUIController guiController = new GUIController();
        new AnimationTimer()
        {

            public void handle(long currentNanoTime)
            {
                //loop
            }
        }.start();


        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
