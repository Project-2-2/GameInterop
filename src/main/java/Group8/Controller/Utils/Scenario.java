package Group8.Controller.Utils;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Group8.Controller.LauncherGUI;
import Interop.Geometry.Point;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author joel
 */
public class Scenario {

    private String mapDoc;
    private final Path filePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private int gameMode;
    private int mapHeight;
    private int mapWidth;
    private int numIntruders;
    private int numGuards;
    private float captureDistance;
    private int winConditionIntruderRounds;
    private float maxRotationAngle;
    private float maxMoveDistanceIntruder;
    private float maxSprintDistanceIntruder;
    private float maxMoveDistanceGuard;
    private int sprintCooldown;
    private int pheromoneCooldown;
    private float radiusPheromone;
    private float slowDownModifierWindow;
    private float slowDownModifierDoor;
    private float slowDownModifierSentryTower;
    private float viewAngle;
    private int viewRays;
    private float viewRangeIntruderNormal;
    private float viewRangeIntruderShaded;
    private float viewRangeGuardNormal;
    private float viewRangeGuardShaded;
    private float[] viewRangeSentry;
    private float yellSoundRadius;
    private float maxMoveSoundRadius;
    private float windowSoundRadius;
    private float doorSoundRadius;
    private int scaling = 2;

    private Area spawnAreaIntruders;
    private Area spawnAreaGuards;
    private Area targetArea;
    private static ArrayList<Area> walls;
    private static ArrayList<TelePortal> teleports;
    private ArrayList<Area> shaded;

    private ArrayList<IntruderInfo> intruders;
    private ArrayList<GuardInfo> guards;
    private double[][] intruderLocations;
    private double[][] guardLocations;
    private final double AGENT_RADIUS = 0.5;

    private Group contentPane;

    public Scenario(String mapFile){
        // set parameters
        mapDoc=mapFile;
        // initialize variables
        walls = new ArrayList<>(); // create list of walls
        shaded = new ArrayList<>(); // create list of low-visability areas
        teleports = new ArrayList<>(); // create list of teleports e.g. stairs
        // read scenario
        filePath = Paths.get(mapDoc); // get path
        System.out.println(filePath);
        readMap();
        intruders = new ArrayList<>();
        guards = new ArrayList<>();
        contentPane = createContentPane();
    }

    public void readMap(){
        try (Scanner scanner =  new Scanner(filePath, ENCODING.name())){
            while (scanner.hasNextLine()){
                parseLine(scanner.nextLine());
            }
        }
        catch(Exception e)
        {
        }
    }

    /*

     */
    private void parseLine(String line){
        //use a second Scanner to parse the content of each line
        try(Scanner scanner = new Scanner(line)){
            scanner.useDelimiter("=");
            if (scanner.hasNext()){
                // read id value pair
                String id = scanner.next();
                String value = scanner.next();
                // trim excess spaces
                value=value.trim();
                id=id.trim();
                // in case multiple parameters
                String[] items=value.split(",");
                Area tmp;
                switch(id)
                {
                    case "scaling":
                        scaling = Integer.parseInt(value);
                        break;
                    case "gameMode":
                        gameMode = Integer.parseInt(value);// 0 is exploration, 1 evasion pursuit game
                        break;
                    case "height":
                        mapHeight = Integer.parseInt(value);
                        break;
                    case "width":
                        mapWidth = Integer.parseInt(value);
                        break;
                    case "numGuards":
                        numGuards = Integer.parseInt(value);
                        break;
                    case "numIntruders":
                        numIntruders = Integer.parseInt(value);
                        break;
                    case "targetArea":
                        targetArea = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        break;
                    case "spawnAreaIntruders":
                        spawnAreaIntruders = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]));
                        break;
                    case "spawnAreaGuards":
                        spawnAreaGuards = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]));
                        break;
                    case "wall":
                        tmp = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]));
                        walls.add(tmp);
                        break;
                    case "teleport":
                        TelePortal teletmp = new TelePortal(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Double.parseDouble(items[6]));
                        teleports.add(teletmp);
                        break;
                    case "shaded":
                        tmp = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]));
                        shaded.add(tmp);
                        break;
                    case "captureDistance":
                        captureDistance = Float.parseFloat(value);
                        break;
                    case "winConditionIntruderRounds":
                        winConditionIntruderRounds = Integer.parseInt(value);
                        break;
                    case "maxRotationAngle":
                        maxRotationAngle = Float.parseFloat(value);
                        break;
                    case "maxMoveDistanceIntruder":
                        maxMoveDistanceIntruder = Float.parseFloat(value);
                        break;
                    case "maxSprintDistanceIntruder":
                        maxSprintDistanceIntruder = Float.parseFloat(value);
                        break;
                    case "maxMoveDistanceGuard":
                        maxMoveDistanceGuard = Float.parseFloat(value);
                        break;
                    case "sprintCooldown":
                        sprintCooldown = Integer.parseInt(value);
                        break;
                    case "pheromoneCooldown":
                        pheromoneCooldown = Integer.parseInt(value);
                        break;
                    case "radiusPheromone":
                        radiusPheromone = Float.parseFloat(value);
                        break;
                    case "slowDownModifierDoor":
                        slowDownModifierDoor = Float.parseFloat(value);
                        break;
                    case "slowDownModifierWindow":
                        slowDownModifierWindow = Float.parseFloat(value);
                        break;
                    case "slowDownModifierSentryTower":
                        slowDownModifierSentryTower = Float.parseFloat(value);
                        break;
                    case "viewAngle":
                        viewAngle = Float.parseFloat(value);
                        break;
                    case "viewRays":
                        viewRays = Integer.parseInt(value);
                        break;
                    case "viewRangeIntruderNormal":
                        viewRangeIntruderNormal = Float.parseFloat(value);
                        break;
                    case "viewRangeIntruderShaded":
                        viewRangeIntruderShaded = Float.parseFloat(value);
                        break;
                    case "viewRangeGuardNormal":
                        viewRangeGuardNormal = Float.parseFloat(value);
                        break;
                    case "viewRangeGuardShaded":
                        viewRangeGuardShaded = Float.parseFloat(value);
                        break;
                    case "viewRangeSentry":
                        viewRangeSentry = new float[]{Float.parseFloat(items[0]), Float.parseFloat(items[1])};
                        break;
                    case "yellSoundRadius":
                        yellSoundRadius = Float.parseFloat(value);
                        break;
                    case "maxMoveSoundRadius":
                        maxMoveSoundRadius = Float.parseFloat(value);
                        break;
                    case "windowSoundRadius":
                        windowSoundRadius = Float.parseFloat(value);
                        break;
                    case "doorSoundRadius":
                        doorSoundRadius = Float.parseFloat(value);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public boolean inWall(double x, double y){
        boolean tmp = false;
        for(int j=0;j<walls.size();j++){
            if(walls.get(j).isHit(x,y)){
                tmp=true;
            }
        }
        return(tmp);
    }

    public double[][] spawnGuards(){
        double[][] tmp = new double[numGuards][4];
        double dx=spawnAreaGuards.rightBoundary-spawnAreaGuards.leftBoundary;
        double dy=spawnAreaGuards.bottomBoundary-spawnAreaGuards.topBoundary;
        for(int i=0; i<numGuards; i++){
            tmp[i][0]=spawnAreaGuards.leftBoundary+Math.random()*dx;
            tmp[i][1]=spawnAreaGuards.bottomBoundary+Math.random()*dy;
            //tmp[i][2]=Math.random()*2*Math.PI;
        }
        return tmp;
    }

    public double[][] spawnIntruders(){
        double[][] tmp = new double[numIntruders][4];
        double dx=spawnAreaIntruders.rightBoundary-spawnAreaIntruders.leftBoundary;
        double dy=spawnAreaIntruders.topBoundary-spawnAreaIntruders.bottomBoundary;
        for(int i=0; i<numIntruders; i++){
            tmp[i][0]=spawnAreaIntruders.leftBoundary+Math.random()*dx;
            tmp[i][1]=spawnAreaIntruders.bottomBoundary+Math.random()*dy;
            //tmp[i][2]=Math.random()*2*Math.PI;
        }
        return tmp;
    }
    public double getGuardRange(){return viewRangeGuardNormal;}

    public Point getMapDimension(){
        return new Point(this.mapWidth, this.mapHeight);
    }

    public int getNumGuards(){
        return numGuards;
    }

    public int getNumIntruders(){
        return numIntruders;
    }

    public String getMapDoc(){
        return mapDoc;
    }

    public int getScaling(){
        return scaling;
    }

    public Area getSpawnAreaIntruders() { return spawnAreaIntruders; }

    public Area getSpawnAreaGuards() { return spawnAreaGuards; }

    public static ArrayList<TelePortal> getTeleports() { return teleports; }

    public static ArrayList<Area> getWalls() { return walls; }

    public ArrayList<Area> getShaded(){
        return shaded;
    }

    public ArrayList<TelePortal> getTeleportals(){
        return teleports;
    }

    public Area getTargetArea(){
        return targetArea;
    }

    public Group getContentPane(){
        return contentPane;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public float getCaptureDistance() {
        return captureDistance;
    }

    public int getWinConditionIntruderRounds() {
        return winConditionIntruderRounds;
    }

    public float getMaxRotationAngle() {
        return maxRotationAngle;
    }

    public float getMaxMoveDistanceIntruder() {
        return maxMoveDistanceIntruder;
    }

    public float getMaxSprintDistanceIntruder() {
        return maxSprintDistanceIntruder;
    }

    public float getMaxMoveDistanceGuard() {
        return maxMoveDistanceGuard;
    }

    public int getSprintCooldown() {
        return sprintCooldown;
    }

    public int getPheromoneCooldown() {
        return pheromoneCooldown;
    }

    public float getRadiusPheromone() {
        return radiusPheromone;
    }

    public float getSlowDownModifierWindow() {
        return slowDownModifierWindow;
    }

    public float getSlowDownModifierDoor() {
        return slowDownModifierDoor;
    }

    public float getSlowDownModifierSentryTower() {
        return slowDownModifierSentryTower;
    }

    public float getViewAngle() {
        return viewAngle;
    }

    public int getViewRays() {
        return viewRays;
    }

    public float getViewRangeIntruderNormal() {
        return viewRangeIntruderNormal;
    }

    public float getViewRangeIntruderShaded() {
        return viewRangeIntruderShaded;
    }

    public float getViewRangeGuardNormal() {
        return viewRangeGuardNormal;
    }

    public float getViewRangeGuardShaded() {
        return viewRangeGuardShaded;
    }

    public float[] getViewRangeSentry() {
        return viewRangeSentry;
    }

    public float getYellSoundRadius() {
        return yellSoundRadius;
    }

    public float getMaxMoveSoundRadius() {
        return maxMoveSoundRadius;
    }

    public float getWindowSoundRadius() {
        return windowSoundRadius;
    }

    public float getDoorSoundRadius() {
        return doorSoundRadius;
    }


    /** This function creates objects for rendering 'eg. rectangle for areas' and returns a pane which
     * contains all of them
     * @return a pane containing all the elements necessary for the GUI
     */
    public Group createContentPane(){
        Group root = new Group();

        //------------------------------------------------------------------------------------------------------------
        // TARGET AREAS
        Rectangle targetAreaRec = new Rectangle(getTargetArea().leftBoundary* LauncherGUI.DRAW_CONSTANT, getTargetArea().topBoundary* LauncherGUI.DRAW_CONSTANT, (getTargetArea().rightBoundary - getTargetArea().leftBoundary)* LauncherGUI.DRAW_CONSTANT, (getTargetArea().bottomBoundary-getTargetArea().topBoundary)* LauncherGUI.DRAW_CONSTANT);
        Rectangle spawnAreaIntruders = new Rectangle(getSpawnAreaIntruders().leftBoundary* LauncherGUI.DRAW_CONSTANT, getSpawnAreaIntruders().topBoundary* LauncherGUI.DRAW_CONSTANT, (getSpawnAreaIntruders().rightBoundary - getSpawnAreaIntruders().leftBoundary)* LauncherGUI.DRAW_CONSTANT, (getSpawnAreaIntruders().bottomBoundary-getSpawnAreaIntruders().topBoundary)* LauncherGUI.DRAW_CONSTANT);
        Rectangle spawnAreaGuards = new Rectangle(getSpawnAreaGuards().leftBoundary* LauncherGUI.DRAW_CONSTANT, getSpawnAreaGuards().topBoundary* LauncherGUI.DRAW_CONSTANT, (getSpawnAreaGuards().rightBoundary - getSpawnAreaGuards().leftBoundary)* LauncherGUI.DRAW_CONSTANT, (getSpawnAreaGuards().bottomBoundary-getSpawnAreaGuards().topBoundary)* LauncherGUI.DRAW_CONSTANT);

        targetAreaRec.setOnMouseEntered(e-> System.out.println("Target Area"));
        spawnAreaIntruders.setOnMouseEntered(e-> System.out.println("Spawn Area Intruders"));
        spawnAreaGuards.setOnMouseEntered(e-> System.out.println("Spawn Area Guards"));

        targetAreaRec.setFill(Color.GREEN);  spawnAreaIntruders.setFill(Color.RED);  spawnAreaGuards.setFill(Color.BLUE);
        root.getChildren().addAll(targetAreaRec, spawnAreaGuards, spawnAreaIntruders);

        //------------------------------------------------------------------------------------------------------------
        //WALLS
        for(Area a : getWalls()){
            Rectangle wall = new Rectangle(a.leftBoundary* LauncherGUI.DRAW_CONSTANT, a.topBoundary* LauncherGUI.DRAW_CONSTANT, (a.rightBoundary - a.leftBoundary)* LauncherGUI.DRAW_CONSTANT, (a.bottomBoundary-a.topBoundary)* LauncherGUI.DRAW_CONSTANT);
            wall.setOnMouseEntered(event -> System.out.println("Wall"));
            root.getChildren().add(wall);
        }

        //------------------------------------------------------------------------------------------------------------
        // TELEPORT SHADED AND TEXTURE
        for(TelePortal tp : getTeleportals()){
            Rectangle teleportRec = new Rectangle(tp.leftBoundary* LauncherGUI.DRAW_CONSTANT, tp.topBoundary* LauncherGUI.DRAW_CONSTANT, (tp.rightBoundary - tp.leftBoundary)* LauncherGUI.DRAW_CONSTANT, (tp.bottomBoundary-tp.topBoundary)* LauncherGUI.DRAW_CONSTANT);
            teleportRec.setOnMouseEntered(e-> System.out.println("Teleport Area"));
            root.getChildren().add(teleportRec);
        }
        for(Area shd : getShaded()){
            Rectangle shadedRectangle = new Rectangle(shd.leftBoundary* LauncherGUI.DRAW_CONSTANT, shd.topBoundary* LauncherGUI.DRAW_CONSTANT, (shd.rightBoundary - shd.leftBoundary)* LauncherGUI.DRAW_CONSTANT, (shd.bottomBoundary-shd.topBoundary)* LauncherGUI.DRAW_CONSTANT);
            shadedRectangle.setOnMouseEntered(e-> System.out.println("Shaded Area"));
            shadedRectangle.setFill(Color.LIGHTGREY);
            root.getChildren().add(shadedRectangle);
        }
        //------------------------------------------------------------------------------------------------------------
        intruderLocations = spawnIntruders();
        guardLocations = spawnGuards();
        for (int i = 0; i < intruderLocations.length; i++) {
            IntruderInfo AI = new IntruderInfo(intruderLocations[i][0],intruderLocations[i][1],AGENT_RADIUS);
            AI.getC().setOnMouseEntered(event -> System.out.println("Intruder"));
            intruders.add(AI);
            root.getChildren().add(AI.getC());
        }
        for (int i = 0; i < guardLocations.length; i++) {
            GuardInfo AI = new GuardInfo(guardLocations[i][0],guardLocations[i][1],AGENT_RADIUS);
            AI.getC().setFill(Color.rgb(160,10,10));
            AI.getC().setOnMouseEntered(event -> System.out.println("Guard"));
            guards.add(AI);
            root.getChildren().add(AI.getC());
        }

        return root;
    }


    public ArrayList<IntruderInfo> getIntruders() {
        return intruders;
    }

    public ArrayList<GuardInfo> getGuards() {
        return guards;
    }

    public double[][] getIntruderLocations() {
        return intruderLocations;
    }

    public double[][] getGuardLocations() {
        return guardLocations;
    }
}
