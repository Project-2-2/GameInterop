package Group8.Launchers.GUI;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.math.Vector2;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

public class GameScene extends Scene {

    private final int WIDTH,HEIGHT;
    private final int AGENT_RAD = 5;
    private final Color GUARD_COL = Color.BLANCHEDALMOND;
    private final Color INTRUDER_COL = Color.DARKSLATEGRAY;

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
        gcBackground.setFill(Color.GREY);
        gcBackground.fillRect(0,0,WIDTH,HEIGHT);
    }

    public void drawMovableObject(MovableObject o){
        gcForeground.setFill(Color.RED);
        gcForeground.fillRect(o.getX(),o.getY(),o.getWidth(),o.getHeight());
    }

    public void drawEntities(List<GuardContainer> guards, List<IntruderContainer> intruders, List<DynamicObject<?>> objects){
        for (DynamicObject<?> obj :
                objects) {
            if (obj instanceof Sound) {
                // Draw sound
            }
            else if(obj instanceof Pheromone){
                // Draw pheromone
            }
            else{
                // Draw remaining
            }
        }
        for (GuardContainer guard:
             guards) {
            drawAgent(guard,GUARD_COL);
        }
        for(IntruderContainer intruder:
            intruders){
            drawAgent(intruder,INTRUDER_COL);
        }
    }

    private void drawAgent(AgentContainer<?> agent, Color color){
        gcForeground.setFill(color);
        Vector2 position = agent.getPosition();
        gcForeground.fillOval(position.getX(),position.getY(),AGENT_RAD,AGENT_RAD);
    }

    private void drawBackgroundLayer(){
        clearBackground();
        drawRect();
    }

    public void clearForeground(){
        gcForeground.clearRect(0,0,WIDTH,HEIGHT); // Clear the canvas
    }
    public void clearBackground(){
        gcBackground.clearRect(0,0,WIDTH,HEIGHT); // Clear the canvas
    }


}
