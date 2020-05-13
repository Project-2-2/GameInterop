package Group8.Launchers.GUI;


import Group9.Game;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.dynamic.DynamicObject;
import Group9.map.parser.Parser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class GUI extends Application{
    private final int WIDTH = 800, HEIGHT = 600;

    //If we need to pass a specific factory make sure to use -1 for ticks since that will be the fastest
    private final Game game = new Game(Parser.parseFile("./src/main/java/Group9/map/maps/test_2.map"),false);

    private final GameScene scene = new GameScene(new StackPane(),WIDTH,HEIGHT);

    private AnimationTimer timer;

    private MovableObject obj = new MovableObject(0,0,50,50);
    private MovableObject obj2 = new MovableObject(WIDTH,0,10,10);

    /**
     * Launches the GUI
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(HEIGHT);
        stage.setWidth(WIDTH);
        stage.setTitle("G8");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
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
//        scene.clearForeground();
//        scene.drawMovableObject(obj);
//        scene.drawMovableObject(obj2);
//        obj.move(1,1);
//        obj2.move(-1,1);
        scene.drawEntities(guards, intruders, objects);

    }
}

class MovableObject{
    private double x;
    private double y;
    private int width,height;

    public MovableObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void move(double x, double y){
        this.x += x;
        this.y += y;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

