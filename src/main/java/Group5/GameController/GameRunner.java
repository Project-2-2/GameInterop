package Group5.GameController;


import Group5.UI.DrawableDialogueBox;
import Group5.UI.MapViewer;
import Interop.Action.Action;
import Interop.Action.DropPheromone;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameRunner {


    private Vision vision;
    private static Hearing hearing;

    private boolean paused;

    // Pause the timer and simulation
    public void pause() {
        this.timer.cancel();
    }

    final private static double FRAMES_PER_SECOND = 20;

    @FXML
    private MapViewer mapViewer;
    @FXML
    private BorderPane gameBorder;

    private static MapInfo mapInfo;

    private Timer timer;

    private int checkIfWon;

    private boolean gameEnded;


    protected static PheromoneStorage pheromoneStorage;

    public GameRunner() {
        mapInfo = new MapInfo();
        timer = new Timer();
        checkIfWon = 0;
        gameEnded = false;
        pheromoneStorage = new PheromoneStorage();
    }

    public static void main(String[] args) throws IOException {

        GameRunner runner = new GameRunner();
        runner.initialize();
    }

    @FXML
    public void initialize() throws IOException {
        //File file = DrawableDialogueBox.getFile();
        //mapInfo.readMap(file.getPath());

        File file = DrawableDialogueBox.getFile();
        String src = "src/main/java/Group5/Maps/testmap";
        //mapInfo.readMap(src);
        //tries to open the map without gui, otherwise open without gui
        try{
            mapInfo.readMap(file.getPath());
        }catch (NullPointerException e){
            JFXPanel panel =new JFXPanel();
            mapInfo.readMap(src);
        }







        mapInfo.initialize();

        vision = new Vision();
        hearing = new Hearing(mapInfo);


        this.update();
        this.startTimer();

        if (mapViewer!=null){
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
        if (gameEnded){
            System.out.println("Game ENDED");
            return;
        }
        coolDownTimers();


        ObjectPercepts visionPercepts = getVision();
        Set<ObjectPercept> percepts =visionPercepts.getAll();
//       System.out.println(percepts.size());
        if (percepts.size()>0){
            //System.out.println(percepts.iterator().next().toString());
        }



        for (IntruderController intruder : mapInfo.intruders){
            if (!intruder.isCaptured) {
                Action nextAction = intruder.explorer.getAction(intruder, visionPercepts);
//            System.out.println(nextAction);
                if (nextAction instanceof Move) {
                    intruder.move((Move) nextAction);
//                System.out.println(((Move) nextAction).getDistance().getValue());
                } else {
                    Rotate r = (Rotate) nextAction;
                    intruder.rotate(r.getAngle());
                }
//            rotate(new Rotate(Angle.fromDegrees(90)));
//            System.out.println(intruder.getAngle().getDegrees());
                if (mapViewer != null) {
                    mapViewer.moveIntruder(intruder.position.getX(), intruder.position.getY());
                    mapViewer.drawAgentVisionField(intruder.position.getX(), intruder.position.getY(),
                            intruder.getAngle().getRadians(), intruder.getViewRange().getValue());
                }
            }
        }


        if (mapViewer!=null) {
            for (Door door : mapInfo.doors) {
                if (!door.doorClosed()) {
                    mapViewer.doorOpening(door.x1, door.y1, door.x2, door.y2, door.x3, door.y3, door.x4, door.y4);
                }
            }
            for (Window window : mapInfo.windows) {
                if (!window.windowClosed()) {
                    mapViewer.windowOpening(window.x1, window.y1, window.x2, window.y2, window.x3, window.y3, window.x4, window.y4);
                }
            }
        }


        //use a different agent for this
        for (GuardController guard : mapInfo.guards){
            Action nextAction = guard.explorer.getAction(guard, visionPercepts);
//            System.out.println(nextAction);
            if (nextAction instanceof Move) {
                guard.move((Move) nextAction);
//                System.out.println(((Move) nextAction).getDistance().getValue());
            }
            else {
                Rotate r = (Rotate) nextAction;
                guard.rotate(r.getAngle());
            }
//            rotate(new Rotate(Angle.fromDegrees(90)));
//            System.out.println(intruder.getAngle().getDegrees());
            if(mapViewer!=null) {
                mapViewer.moveIntruder(guard.position.getX(), guard.position.getY());
                mapViewer.drawAgentVisionField(guard.position.getX(), guard.position.getY(),
                        guard.getAngle().getRadians(), guard.getViewRange().getValue());
            }
        }

        if (mapViewer!=null) {
            for (Door door : mapInfo.doors) {
                if (!door.doorClosed()) {
                    mapViewer.doorOpening(door.x1, door.y1, door.x2, door.y2, door.x3, door.y3, door.x4, door.y4);
                }
            }
            for (Window window : mapInfo.windows) {
                if (!window.windowClosed()) {
                    mapViewer.windowOpening(window.x1, window.y1, window.x2, window.y2, window.x3, window.y3, window.x4, window.y4);
                }
            }
        }

        pheromoneStorage.updatePheromones();

        checkIfIntruderCaught();
        checkGameEnded();

    }


    private void checkGameEnded(){
        if (mapInfo.gameMode==0){
            boolean won = true;
            for (int i =0; i<mapInfo.intruders.size();i++){
                if (!mapInfo.targetArea.isHit(mapInfo.intruders.get(i).getPosition())){
                    won = false;
                    break;
                }
            }
            if (won){
                checkIfWon++;
            }else{
                checkIfWon=0;
            }
            if (checkIfWon==mapInfo.winConditionIntruderRounds){
                System.out.println("INTRUDERS WON");
                gameEnded = true;
            }
        }
        if (mapInfo.gameMode==1){
            boolean won = false;
            for (int i =0; i<mapInfo.intruders.size();i++){
                if (mapInfo.targetArea.isHit(mapInfo.intruders.get(i).getPosition())){
                    won = true;
                    break;
                }
            }
            if (won){
                checkIfWon++;
            }else{
                checkIfWon=0;
            }
            if (checkIfWon==mapInfo.winConditionIntruderRounds){
                System.out.println("INTRUDERS WON");
                gameEnded = true;
            }
        }
    }

    private void checkIfIntruderCaught(){

        for (int i =0; i<mapInfo.guards.size();i++){
            ObjectPercepts visionPercepts =  vision.vision(mapInfo.guards.get(i));
            Set<ObjectPercept> percepts =visionPercepts.getAll();
            for(int j = 0; j<percepts.size();j++){
                if (percepts.iterator().next().getType().equals(ObjectPerceptType.Intruder)){
                    for (int k =0; k<mapInfo.intruders.size();k++){
                        if (Sat.distance(mapInfo.guards.get(i).getPosition(),mapInfo.intruders.get(k).getPosition())<=mapInfo.captureDistance){
                            if (mapInfo.gameMode==1){
                                System.out.println("GUARDS WON");
                                gameEnded=true;
                            }
                            if (mapInfo.gameMode==0){
                                mapInfo.intruders.get(k).isCaptured=true;
                            }
                        }
                    }
                }
            }
        }
        if (mapInfo.gameMode==0){
            boolean allCaptured = true;
            for (int i =0; i<mapInfo.intruders.size();i++){
                if (!mapInfo.intruders.get(i).isCaptured){
                    allCaptured=false;
                    break;
                }
            }
            if (allCaptured){
                gameEnded=true;
            }
        }

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
            //System.out.println("collision detected");
            if (Sat.hasCollided(movment,sentryVectors)||sentryTowers.get(i).isHit(to)){
                if (!sentryTowers.get(i).enterTower(to)){
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    protected static boolean enterShadedAreay(Point from, Point to){
        ArrayList<ShadedArea> shadedAreas= mapInfo.shaded;
        //for now give the agent a radius of 1
        ArrayList<Point> movment = movementShape(from,to,1);

        for(int i =0; i<shadedAreas.size();i++){
            ArrayList<Point> shadedVectors = shadedAreas.get(i).getAreaVectors();
            if (Sat.hasCollided(movment,shadedVectors)||shadedAreas.get(i).isHit(to)){
                //System.out.println("collision detected biem");
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

    //TODO smell has to be implemented
    public void dropPheromone(DropPheromone dropPheromone){
        SmellPercept smell = new SmellPercept(dropPheromone.getType(),new Distance(mapInfo.radiusPheromone));
        mapInfo.intruders.get(0).dropPheromone(smell);

    }

    protected static void addPheromoneIntruders(SmellPercept smell, Point position){
        pheromoneStorage.addPheromone(new Pheromone(smell.getType(),position,5,mapInfo.radiusPheromone),false);

    }

    protected static void addPheromoneGuards(SmellPercept smell, Point position){
        pheromoneStorage.addPheromone(new Pheromone(smell.getType(),position,5,mapInfo.radiusPheromone),true);

    }

    public static Direction getTargetDirection(){
        Point targetArea = new Point(mapInfo.targetArea.x1,mapInfo.targetArea.y1);
        Direction targetAngle = targetArea.getClockDirection();

        Point intruderLocation = mapInfo.intruders.get(0).getPosition();
        Direction intruderAngle = intruderLocation.getClockDirection();

        Direction targetDirection = Direction.fromRadians(targetAngle.getRadians()-intruderAngle.getRadians());
        return targetDirection;
    }



}

