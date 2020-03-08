package Group5.GameController;


import Group5.UI.DrawableDialogueBox;
import Group5.UI.MapViewer;
import Interop.Geometry.Point;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameRunner {

//    protected String mapDoc;
//    protected Scenario scenario;
//
//    GamePlayer p;
//
//    public static void main(String[] args){
//        // the mapscenario should be passed as a parameter
//        String mapD="/Users/slav/Documents/Maastricht University - DKE/Year 2/Project 2-2/Project22/GameControllerSample/testmap.txt";
//        GameRunner game = new GameRunner(mapD);
//        game.p.setup();
//        //game.writeGameFile();
//        game.p.start();
//    }
//
//    public GameRunner(String scn){
//        mapDoc=scn;
//        scenario = new Scenario(mapDoc);
//        p = new GamePlayer(scenario);
//    }
//
//    public void runWholeGame(){
//        this.p.setup();
//        this.p.start();
//        Explorer explorer = new Explorer(gameRunner.getPath());
//        Explorer.runExplorer(explorer);
//    }
//
//    private String getPath(){
//        return this.mapDoc;
//    }

    final private static double FRAMES_PER_SECOND = 5;

    @FXML
    private MapViewer mapViewer;
    @FXML
    private BorderPane gameBorder;

    private MapInfo mapInfo;

    private Timer timer;

    public GameRunner() {
        mapInfo = new MapInfo();
        timer = new Timer();
    }

    public static void main(String[] args) throws IOException {

        Point from = new Point(6,6);
        Point to = new Point (100,2);
        GameRunner poep = new GameRunner();
        //Vector2D[] movement = poep.movementShape(from,to,5);
        //movement = new Vector2D[]{new Vector2D(from)};

        Area wall = new Area(5,5,10,5,5,10,10,10);
        Area wall2 = new Area(4,4,8,11,11,8,100,50);


        System.out.println(wall.isHit(4,4,2));

        System.out.println(wall.isHit(wall2));


      /*
        GameRunner runner = new GameRunner();
        runner.initialize();

       */


    }

    @FXML
    public void initialize() throws IOException {
        //File file = DrawableDialogueBox.getFile();
        //mapInfo.readMap(file.getPath());
        String src = "src/main/java/Group5/Maps/testmap.txt";
        mapInfo.readMap(src);
        mapInfo.spawnAgents();  

       // this.update();
        //this.startTimer();
        /*
        mapViewer.setFocusTraversable(true);
        mapViewer.requestFocus();

         */
    }

    private void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        try {
                            update();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        long frameTimeInMilliseconds = (long) (1000.0 / FRAMES_PER_SECOND);
        this.timer.schedule(timerTask, 100, frameTimeInMilliseconds);
    }

    private void update() throws IOException {
        this.mapViewer.moveIntruder(10, 10, true);
    }



    /**
     * CALL THIS METHOD TO CHECK IF MOVEMENT IS VALID
     * method to move an agent
     * checks if movement is valid
     * returns the postion after movement
     */
    public Point moveValidility(Point from, Point to){
        ArrayList<Area> walls= mapInfo.walls;

        //for now give the agent a radius of 1
        Vector2D[] movment = movementShape(from,to,1);
        for(int i =0; i<walls.size();i++){
            Vector2D[] wallVectors = walls.get(i).getAreaVectors();
            System.out.println("collision detected");
            if (Sat.hasCollided(movment,wallVectors)||walls.get(i).isHit(to)){
                to = from;
                return to;
            }
        }
        return to;
    }

    /**
     * creates an area of the movement of the agent, since everything is discrete the total movement has to be checked
     *
     * @param from
     * @param to
     * @param radius of the agent
     * @return
     */
    public Vector2D[] movementShape(Point from, Point to, double radius){
        Vector2D start = new Vector2D(from);
        Vector2D end = new Vector2D(to);

        Vector2D direction = new Vector2D(start).add(end);
        Vector2D directionOrthogonal = direction.orthogonal();
        directionOrthogonal = directionOrthogonal.normalize();
        directionOrthogonal = directionOrthogonal.absVector();
        directionOrthogonal = directionOrthogonal.mul(radius);

        Vector2D point1 = new Vector2D(start).add(directionOrthogonal);
        System.out.println(point1.getX());
        Vector2D point2 = new Vector2D(start).add(directionOrthogonal.mul(-1));
        System.out.println(point2.getX());
        Vector2D point3 = new Vector2D(end).add(directionOrthogonal);
        System.out.println(point3.getX());
        Vector2D point4 = new Vector2D(end).add(directionOrthogonal.mul(-1));
        System.out.println(point4.getY());

        Vector2D[] shape =  {point1,point2,point3,point4};

        return shape;

    }

}

