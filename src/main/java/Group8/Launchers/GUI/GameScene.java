package Group8.Launchers.GUI;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.GameMap;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.*;
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
            drawAgent(guard,Presets.GUARD_COL);
        }
        for(IntruderContainer intruder:
            intruders){
            drawAgent(intruder,Presets.INTRUDER_COL);
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
    // Inspired by group 9 implementation
    private StaticDrawable getStaticDrawable(MapObject mo) {
        if(mo instanceof Wall){
            return new StaticDrawable(Presets.WALL_COL,true);
        }
        else if(mo instanceof TargetArea){
            return new StaticDrawable(Presets.TARGET_COL,false);
        }
        else if(mo instanceof Spawn.Guard){
            return new StaticDrawable(Presets.SPAWN_GUARD_COL,false);
        }
        else if(mo instanceof Spawn.Intruder){
            return new StaticDrawable(Presets.SPAWN_INTRUDER_COL,false);
        }
        else if(mo instanceof ShadedArea){
            return new StaticDrawable(Presets.SHADED_COL,true);
        }
        else if(mo instanceof Door){
            return new StaticDrawable(Presets.DOOR_COL,true);
        }
        else if(mo instanceof Window){
            return new StaticDrawable(Presets.WINDOW_COL,true);
        }
        else if(mo instanceof SentryTower){
            return new StaticDrawable(Presets.SENTRY_COL,true);
        }
        else if(mo instanceof TeleportArea){
            return new StaticDrawable(Presets.TELEPORT_COL,true);
        }

        return new StaticDrawable(Presets.UNKNOWN,true);
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

class Presets{
    public static final Color GUARD_COL = Color.BLUE;
    public static final Color INTRUDER_COL = Color.RED;
    public static final Color WALL_COL = Color.WHITE;
    public static final Color TARGET_COL = Color.BLACK;
    public static final Color SPAWN_INTRUDER_COL = Color.RED;
    public static final Color SPAWN_GUARD_COL = Color.BLUE;
    public static final Color SHADED_COL = Color.DARKGRAY;
    public static final Color DOOR_COL = Color.GREEN;
    public static final Color WINDOW_COL = Color.LIGHTBLUE;
    public static final Color SENTRY_COL = Color.FIREBRICK;
    public static final Color TELEPORT_COL = Color.PURPLE;
    public static final Color UNKNOWN = Color.LIMEGREEN;
}