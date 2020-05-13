package Group8.Launchers.GUI;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameScene extends Scene {

    private final int WIDTH,HEIGHT;

    private Canvas background;
    private Canvas foreground;

    private GraphicsContext gcBackground;
    private GraphicsContext gcForeground;
    private StackPane parent;

    public GameScene(StackPane parent, int width, int height) {
        super(parent);
        WIDTH = width;
        HEIGHT = height;
        init(parent); // Initializes the needed variables
        constructScene(); // Necessary setup for the scene

    }

    private void constructScene(){
        parent.getChildren().add(background);
        parent.getChildren().add(foreground);

        drawBackgroundLayer();
    }


    private void init(StackPane parent){
        background = new Canvas(WIDTH,HEIGHT);
        foreground = new Canvas(WIDTH,HEIGHT);
        gcBackground = background.getGraphicsContext2D();
        gcForeground = foreground.getGraphicsContext2D();
        this.parent = parent;
    }

    public void drawRect(){
        gcBackground.setFill(Color.BLUE);
        gcBackground.fillRect(0,0,WIDTH,HEIGHT);
    }

    public void drawMovableObject(MovableObject o){
        System.out.println("Clearing foreground");
        gcForeground.clearRect(0,0,WIDTH,HEIGHT);
        System.out.println("Draw foreground");
        gcForeground.setFill(Color.RED);
        gcForeground.fillRect(o.getX(),o.getY(),o.getWidth(),o.getHeight());
    }

    private void drawBackgroundLayer(){
        System.out.println("Draw background");
        gcBackground.clearRect(0,0,WIDTH,HEIGHT); // Clear the screen
        drawRect();
    }


}
