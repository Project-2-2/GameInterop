package Group5.GameController;


import Group5.UI.DrawableDialogueBox;
import Group5.UI.MapViewer;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPercepts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    private Vision vision;
    private static Hearing hearing;

    private boolean paused;

    // Pause the timer and simulation
    public void pause() {
        this.timer.cancel();
    }

    final private static double FRAMES_PER_SECOND = 5;

    @FXML
    private MapViewer mapViewer;
    @FXML
    private BorderPane gameBorder;

    private static MapInfo mapInfo;

    private Timer timer;

    public GameRunner() {
        mapInfo = new MapInfo();
        timer = new Timer();
    }

    public static void main(String[] args) throws IOException {


        /*
        Point from = new Point(6,6);
        Point to = new Point (100,2);
        GameRunner poep = new GameRunner();
        //Point[] movement = poep.movementShape(from,to,5);
        //movement = new Point[]{new Point(from)};

        Area wall = new Area(5,5,10,5,5,10,10,10);
        Area wall2 = new Area(4,4,8,11,11,8,100,50);


        System.out.println(wall.isHit(6,6));

        System.out.println(wall.isHit(wall2));

         */



        /*
        IntruderController poep = new IntruderController(new Point(1,1),1);
        poep.vision();

         */





        GameRunner runner = new GameRunner();
        runner.initialize();






    }

    @FXML
    public void initialize() throws IOException {
        //File file = DrawableDialogueBox.getFile();
        //mapInfo.readMap(file.getPath());

        File file = DrawableDialogueBox.getFile();
        String src = "src/main/java/Group5/Maps/testmap.txt";
        //mapInfo.readMap(src);
        mapInfo.readMap(file.getPath());
        mapInfo.initialize();

        vision = new Vision();
        hearing = new Hearing(mapInfo);

        // Check if the MapViewer for the UI has been initialized
        if (this.mapViewer != null){
            this.update();
            this.startTimer();
            mapViewer.setFocusTraversable(true);
            mapViewer.requestFocus();
        }
    }

    @FXML
    public void keyHandler(KeyEvent event) {
        if (event.getCode() == KeyCode.P && !paused) {
            paused = true;
            pause();
        }
        if (event.getCode() == KeyCode.R && paused) {
            paused = false;
            this.startTimer();
        }
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
        coolDownTimers();


        //basic movement of an agent
       // mapInfo.intruders.get(0).rotate(Angle.fromDegrees(-180));
       // mapInfo.intruders.get(0).move(new Move(new Distance(1)));
        //rotate(new Rotate(Angle.fromDegrees(90)));
        move(new Move(new Distance(1)));


        //TODO vision percepts is empty
        ObjectPercepts visionPercepts = getVision();
        Set<ObjectPercept> percepts =visionPercepts.getAll();
//        System.out.println(percepts.size());
        if (percepts.size()>0){
            //System.out.println(percepts.iterator().next().toString());
        }



        for (IntruderController intruder : mapInfo.intruders){
            Action nextAction = intruder.explorer.getAction(intruder, visionPercepts);
//            System.out.println(nextAction);
            if (nextAction instanceof Move) {
                mapInfo.intruders.get(0).move((Move) nextAction);
//                System.out.println(((Move) nextAction).getDistance().getValue());
            }
            else {
                Rotate r = (Rotate) nextAction;
                mapInfo.intruders.get(0).rotate(r.getAngle());
            }
//            rotate(new Rotate(Angle.fromDegrees(90)));
//            System.out.println(intruder.getAngle().getDegrees());
            mapViewer.moveIntruder(intruder.position.getX(), intruder.position.getY());
        }


        //System.out.println(mapInfo.intruders.get(0).getPosition().toString());
//        this.mapViewer.moveIntruder(10, 10);
    }



    /**
     * CALL THIS METHOD TO CHECK IF MOVEMENT IS VALID
     * method to move an agent
     * checks if movement is valid
     * returns the postion after movement
     */
    protected static boolean moveValidility(Point from, Point to, Distance distance, Distance maxDistance){
        mapInfo.intruders.get(0).onSentryTower=false;
        ArrayList<Area> walls= mapInfo.walls;

        //for now give the agent a radius of 1
        ArrayList<Point> movment = movementShape(from,to,9);
        for(int i =0; i<walls.size();i++){
            ArrayList<Point> wallVectors = walls.get(i).getAreaVectors();
          //  System.out.println("collision detected");
            if (Sat.hasCollided(movment,wallVectors)||walls.get(i).isHit(to)){
                to = from;
                return false;
            }
        }

        //checks if collision with doors
        ArrayList<Door> doors = mapInfo.doors;
        for(int i =0; i<doors.size();i++){
            ArrayList<Point> doorVectors = doors.get(i).getAreaVectors();
            //  System.out.println("collision detected");
            if ((Sat.hasCollided(movment,doorVectors)||doors.get(i).isHit(to))&&doors.get(i).doorClosed()){
                if(distance.getValue()>maxDistance.getValue()*Door.getSlowDownModifier()){
                    return false;
                }
                doors.get(i).openDoor(hearing);
                to = from;
                return true;
            }
        }

        //checks if colission with windows
        ArrayList<Window> windows = mapInfo.windows;
        for(int i =0; i<windows.size();i++){
            ArrayList<Point> windowVectors = windows.get(i).getAreaVectors();
            //  System.out.println("collision detected");
            if ((Sat.hasCollided(movment,windowVectors)||windows.get(i).isHit(to))&&windows.get(i).windowClosed()){
                if(distance.getValue()>maxDistance.getValue()*Window.getSlowDownModifier()){
                    return false;
                }
                windows.get(i).openWindow(hearing);
                to = from;
                return true;
            }
        }


        //checks if colission with sentries
        ArrayList<SentryTower> sentries = mapInfo.sentryTowers;
        for(int i =0; i<sentries.size();i++){
            ArrayList<Point> sentryVectors = sentries.get(i).getAreaVectors();
            //  System.out.println("collision detected");
            if (Sat.hasCollided(movment,sentryVectors)||sentries.get(i).isHit(to)){
                if(distance.getValue()>maxDistance.getValue()*SentryTower.getSlowDownModifer()){
                    return false;
                }
                if(sentries.get(i).enterTower(to)){
                    mapInfo.intruders.get(0).onSentryTower=true;
                    return true;
                }
                to = from;
            }
        }

        //checks if colission with sentries
        ArrayList<TelePortal> teleports = mapInfo.teleports;
        for(int i =0; i<teleports.size();i++){
            ArrayList<Point> teleportVectors = teleports.get(i).getAreaVectors();
            //  System.out.println("collision detected");
            if (teleports.get(i).isHit(to)){
                to=teleportValidility(to,1);
                if (to.equals(from)){
                    return false;
                }
                mapInfo.intruders.get(0).teleported=true;
                mapInfo.intruders.get(0).setPosition(to);
                return true;
            }
        }




        return true;
    }

    /**
     * creates an area of the movement of the agent, since everything is discrete the total movement has to be checked
     *
     * @param from
     * @param to
     * @param radius of the agent
     * @return
     */
    private static  ArrayList<Point> movementShape(Point from, Point to, double radius){
        Point start = from;
        Point end = to;

        Point direction = Sat.add(start,end);
        Point directionOrthogonal = Sat.orthogonal(direction);
        directionOrthogonal = Sat.normalize(directionOrthogonal);
        directionOrthogonal = Sat.absVector(directionOrthogonal);
        directionOrthogonal = Sat.mul(directionOrthogonal,radius);

        Point point1 = Sat.add(start,directionOrthogonal);
       // System.out.println(point1.getX());
        Point point2 = Sat.add(start,Sat.mul(directionOrthogonal,-1));
       // System.out.println(point2.getX());
        Point point3 = Sat.add(end,directionOrthogonal);
       // System.out.println(point3.getX());
        Point point4 = Sat.add(end,Sat.mul(directionOrthogonal,-1));
       // System.out.println(point4.getY());

        ArrayList<Point> shape =  new ArrayList<>(List.of(point1,point2,point3,point4));

        return shape;

    }

    protected static Point teleportValidility(Point oldLocation, double radius){
        ArrayList<TelePortal> teleports = mapInfo.teleports;
        for (int i =0; i<teleports.size();i++){
            if (teleports.get(i).isHit(oldLocation.getX(), oldLocation.getY(), radius)) {
                Point targetLocation = teleports.get(i).getNewLocation();
                if (checkNoColissionTeleport(targetLocation,radius)){
                    return targetLocation;
                }

            }
        }
        return oldLocation;

    }


    /**
     * extra check if new location doesn't give any colissions
     * @param targetLocation
     * @param radius
     * @return
     */
    private static boolean checkNoColissionTeleport(Point targetLocation, double radius){
        ArrayList<Area> walls =mapInfo.walls;
        for (int i =0; i<walls.size();i++){
            if (walls.get(i).isHit(targetLocation,radius)){
                return false;
            }
        }
        return true;
    }

    /**
     * returns the area which can be seen with the raycast
     * checks only for walls
     * @param rayCast the raycast vector
     * @param location the current location of the agent
     * @return
     */
    protected Area visionCollision(Point rayCast, Point location){
       // ArrayList<>
        return null;

    }

    protected static boolean openDoorValidility(Point from, Point to){
        ArrayList<Door> doors= mapInfo.doors;
        //for now give the agent a radius of 1
        ArrayList<Point> movment = movementShape(from,to,1);

        for(int i =0; i<doors.size();i++){
            ArrayList<Point> doorVectors = doors.get(i).getAreaVectors();
            System.out.println("collision detected");
            if (Sat.hasCollided(movment,doorVectors)||doors.get(i).isHit(to)){
                if (!doors.get(i).doorClosed()){
                    return false;
                }
                doors.get(i).openDoor(hearing);
                return true;
            }
        }
        return false;
    }

    protected static boolean openWindowValidility(Point from, Point to){
        ArrayList<Window> windows= mapInfo.windows;
        //for now give the agent a radius of 1
        ArrayList<Point> movment = movementShape(from,to,1);

        for(int i =0; i<windows.size();i++){
            ArrayList<Point> windowVectors = windows.get(i).getAreaVectors();
            System.out.println("collision detected");
            if (Sat.hasCollided(movment,windowVectors)||windows.get(i).isHit(to)){
                if (!windows.get(i).windowClosed()){
                    return false;
                }
                windows.get(i).openWindow(hearing);
                return true;
            }
        }
        return false;
    }


    protected static boolean enterSentry(Point from, Point to){
        ArrayList<SentryTower> sentryTowers= mapInfo.sentryTowers;
        //for now give the agent a radius of 1
        ArrayList<Point> movment = movementShape(from,to,1);

        for(int i =0; i<sentryTowers.size();i++){
            ArrayList<Point> sentryVectors = sentryTowers.get(i).getAreaVectors();
            System.out.println("collision detected");
            if (Sat.hasCollided(movment,sentryVectors)||sentryTowers.get(i).isHit(to)){
                if (!sentryTowers.get(i).enterTower(to)){
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private void coolDownTimers(){
        coolDownSprint();
        coolDownPheroMone();
    }

    private void coolDownSprint(){
        for (int i =0; i<mapInfo.intruders.size();i++){
            if (mapInfo.intruders.get(i).sprintCooldownTimer){
                if (mapInfo.intruders.get(i).sprintCoolDownCounter<mapInfo.sprintCooldown){
                    mapInfo.intruders.get(i).sprintCoolDownCounter++;
                }else{
                    mapInfo.intruders.get(i).sprintCoolDownCounter=0;
                    mapInfo.intruders.get(i).sprintCooldownTimer=false;
                }
            }
        }
    }

    private void coolDownPheroMone(){

        for (int i =0; i<mapInfo.intruders.size();i++){
            if (mapInfo.intruders.get(i).pheroMoneCooldownTimer){
                if (mapInfo.intruders.get(i).pheroMoneCoolDownCounter<mapInfo.pheromoneCooldown){
                    mapInfo.intruders.get(i).pheroMoneCoolDownCounter++;
                }else{
                    mapInfo.intruders.get(i).pheroMoneCoolDownCounter=0;
                    mapInfo.intruders.get(i).pheroMoneCooldownTimer=false;
                }
            }
        }

        for (int i =0; i<mapInfo.guards.size();i++){
            if (mapInfo.guards.get(i).pheroMoneCooldownTimer){
                if (mapInfo.guards.get(i).pheroMoneCoolDownCounter<mapInfo.pheromoneCooldown){
                    mapInfo.guards.get(i).pheroMoneCoolDownCounter++;
                }else{
                    mapInfo.guards.get(i).pheroMoneCoolDownCounter=0;
                    mapInfo.guards.get(i).pheroMoneCooldownTimer=false;
                }
            }
        }

    }


    /**
     * call this method to do a movement
     *
     * @return true if movement is valid otherwise false
     */
    public boolean move(Move move){
       return mapInfo.intruders.get(0).move(move);


    }

    /**
     * call this method to rotate the agent
     * @param rotate
     */
    public void rotate(Rotate rotate){
        mapInfo.intruders.get(0).rotate(rotate.getAngle());
    }

    public ObjectPercepts getVision(){
        return vision.vision(mapInfo.intruders.get(0));
    }
}

