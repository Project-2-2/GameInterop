package Group8.Launchers.GUI;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameScene extends Scene {

    private Canvas canvas;
    private GraphicsContext gc;
    private StackPane parent;

    public GameScene(StackPane parent) {
        super(parent);
        init(parent); // Initializes the needed variables
        constructScene(); // Necessary setup for the scene

    }

    private void constructScene(){
        parent.getChildren().add(canvas);
    }

    private void init(StackPane parent){
        canvas = new Canvas(800,600);
        gc = canvas.getGraphicsContext2D();
        this.parent = parent;
    }

    public void drawRect(){
        gc.setFill(Color.BLUE);
        gc.fillRect(100,100,50,50);
    }


}
