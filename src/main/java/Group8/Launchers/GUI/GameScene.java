package Group8.Launchers.GUI;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.GameMap;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.MapObject;
import Group9.map.objects.Wall;
import Group9.math.Vector2;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class GameScene extends Scene {

    private int width, height;
    private final int AGENT_RAD = 5;
    private final Color GUARD_COL = Color.BLUE;
    private final Color INTRUDER_COL = Color.RED;
    private final int SCALE = 4;


    private Canvas background;
    private Canvas foreground;

    private GraphicsContext gcBackground;
    private GraphicsContext gcForeground;
    private StackPane parent;
    private Stage window;

    private GameMap map;
    private List<MapObject> mapObjects;


    public GameScene(StackPane parent, GameMap map) {
        super(parent);
        width = map.getGameSettings().getWidth();
        height = map.getGameSettings().getHeight();
        init(parent,map); // Initializes the needed variables
        constructScene(); // Necessary setup for the scene
    }

    public void attachWindow(Stage stage){
        this.window = stage;
    }

    private void constructScene(){
        parent.getChildren().add(background);
        parent.getChildren().add(foreground);

        drawBackgroundLayer();
    }


    private void init(StackPane parent, GameMap map){
        background = new Canvas(width, height);
        foreground = new Canvas(width, height);
        gcBackground = background.getGraphicsContext2D();
        gcForeground = foreground.getGraphicsContext2D();
        this.parent = parent;
        this.map = map;
        this.mapObjects = map.getObjects();
    }

    public void drawRect(){
        gcBackground.setFill(Color.GREY);
        gcBackground.fillRect(0,0, width, height);
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
        Vector2 position = agent.getPosition().mul(SCALE);
        gcForeground.fillOval(position.getX(),position.getY(),AGENT_RAD,AGENT_RAD);
    }

    private void drawBackgroundLayer(){
        clearBackground();
        drawRect();

        // Draw static components
        for (MapObject mo :
                mapObjects) {
            StaticDrawable staticDrawable = getStaticDrawable(mo);
            Vector2[] verts = mo.getArea().getAsPolygon().getPoints();
            double[] scaledX = new double[verts.length];
            double[] scaledY = new double[verts.length];

            for (int i = 0; i < verts.length; i++) {
                scaledX[i] = verts[i].getX() * SCALE;
                scaledY[i] = verts[i].getY() * SCALE;
            }

            gcBackground.setFill(staticDrawable.getColor());
            if(staticDrawable.isFill()){
                gcBackground.fillPolygon(scaledX,scaledY,4);
            }
            else{
                gcBackground.setLineWidth(3);
                gcBackground.strokePolygon(scaledX,scaledY,4);
            }




        }
    }

    private StaticDrawable getStaticDrawable(MapObject mo) {
        if(mo instanceof Wall){
            return new StaticDrawable(Color.WHITE,true);
        }
        return new StaticDrawable(Color.PINK,false);
    }

    public void clearForeground(){
        gcForeground.clearRect(0,0, width, height); // Clear the canvas
    }

    public void clearBackground(){
        gcBackground.clearRect(0,0, width, height); // Clear the canvas
    }

    public void rescale(){
        if(window == null){
            System.out.println("Cant rescale, window is not attached!");
            return;
        }

        // Clear the screen
        clearForeground();
        clearBackground();

        // Scale variables
        width = width * SCALE;
        height = height * SCALE;

        window.setWidth(width);
        window.setHeight(height);

        parent.setPrefWidth(width);
        parent.setPrefHeight(height);

        background.setWidth(width);
        background.setHeight(height);
        foreground.setWidth(width);
        foreground.setHeight(height);

        // Redraw background / Static components
        drawBackgroundLayer();

    }

}
