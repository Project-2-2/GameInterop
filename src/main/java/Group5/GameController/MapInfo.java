package Group5.GameController;

import Interop.Geometry.Point;
import Interop.Percept.Scenario.SlowDownModifiers;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class MapInfo {



    private final static Charset ENCODING = StandardCharsets.UTF_8;


    protected String name;
    protected String gameFile;
    protected int mapHeight;
    protected int mapWidth;
    protected double scaling;
    protected int numIntruders;
    protected int numGuards;
    protected Area spawnAreaIntruders;
    protected Area spawnAreaGuards;
    protected Area targetArea;
    protected ArrayList<Area> walls;
    protected ArrayList<TelePortal> teleports;
    protected ArrayList<ShadedArea> shaded;
    protected ArrayList<Door> doors;
    protected ArrayList<Window> windows;
    protected ArrayList<SentryTower> sentryTowers;
    protected double maxMoveDistanceIntruder;
    protected double baseSpeedGuard;
    protected double maxSprintDistanceIntruder;
    //slowdown Modifier interop class
    public SlowDownModifiers slowDownModifiers;
    protected int gameMode;
    //TODO
    protected double captureDistance;
    //TODO
    protected int winConditionIntruderRounds;
    protected double maxRotationAngleDegrees;
    //TODO
    protected int sprintCooldown;
    //TODO
    protected int pheromoneCooldown;
    //TODO
    protected double radiusPheromone;

    protected double slowDownModifierWindow;
    protected double slowDownDoor;
    protected double slowDownSentryTower;
    protected double viewAngleDegrees;

    //TODO ALL VIEW RANGES BUT CAN ONLY BE DONE AFTER VISION IS IMPLEMENTED
    protected int viewRays;

    protected double viewRangeIntruderNormal;
    protected double viewRangeIntruderShaded;
    protected double viewRangeGuardNormal;
    protected double viewRangeGuardShaded;

    //TODO BUT CAN ONLY BE DONE IS SOUND IS IMPLEMENTED
    protected double[] viewRangeSentry;
    protected double yellSoundRadius;
    protected double maxMoveSoundRadius;
    protected double windowSoundRadius;
    protected double doorSoundRadius;


    protected ArrayList<GuardController> guards;
    protected ArrayList<IntruderController> intruders;

    public void readMap(String filePath){
        try (Scanner scanner =  new Scanner(Paths.get(filePath), ENCODING.name())){
            while (scanner.hasNextLine()){
                System.out.println("biem");
                parseLine(scanner.nextLine());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /*

     */
    protected void parseLine(String line){
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
                String[] items=value.split(" ");
                Area tmp;
                switch(id) {
                    case "name":
                        name = value;
                        break;
                    case "gameFile":
                        gameFile = value;
                        break;
                    case "gameMode":
                        gameMode = Integer.parseInt(value); // 0 is exploration, 1 evasion pursuit game
                        break;
                    case "scaling":
                        scaling = Double.parseDouble(value);
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
                    case "captureDistance":
                        captureDistance = Integer.parseInt(value);
                        break;
                    case "winConditionIntruderRounds":
                        winConditionIntruderRounds = Integer.parseInt(value);
                        break;
                    case "maxRotationAngle":
                        maxRotationAngleDegrees = Double.parseDouble(value);
                        break;
                    case "maxMoveDistanceIntruder":
                        maxMoveDistanceIntruder = Double.parseDouble(value);
                        break;
                    case "maxSprintDistanceIntruder":
                        maxSprintDistanceIntruder = Double.parseDouble(value);
                        break;
                    case "maxMoveDistanceGuard":
                        baseSpeedGuard = Double.parseDouble(value);
                        break;
                    case "sprintCooldown":
                        sprintCooldown = Integer.parseInt(value);
                        break;
                    case "pheromoneCooldown":
                        pheromoneCooldown = Integer.parseInt(value);
                        break;
                    case "radiusPheromone":
                        radiusPheromone = Integer.parseInt(value);
                        break;
                    case "slowDownModifierWindow":
                        slowDownModifierWindow = Double.parseDouble(value);
                        break;
                    case "slowDownModifierDoor":
                        slowDownDoor = Double.parseDouble(value);
                        break;
                    case "slowDownModifierSentryTower":
                        slowDownSentryTower = Double.parseDouble(value);
                        break;
                    case "viewAngle":
                        viewAngleDegrees = Double.parseDouble(value);
                        break;
                    case "viewRays":
                        viewRays= Integer.parseInt(value);
                        break;
                    case "viewRangeIntruderNormal":
                        viewRangeIntruderNormal= Double.parseDouble(value);
                        break;
                    case "viewRangeIntruderShaded":
                        viewRangeIntruderShaded= Double.parseDouble(value);
                        break;
                    case "viewRangeGuardShaded":
                        viewRangeGuardShaded= Double.parseDouble(value);
                        break;
                    case "viewRangeGuardNormal":
                        viewRangeGuardNormal= Double.parseDouble(value);
                        break;
                    case "viewRangeSentry":
                        viewRangeSentry= new double[]{Double.parseDouble(items[0]), Double.parseDouble(items[1])};
                        break;
                    case "yellSoundRadius":
                        yellSoundRadius= Double.parseDouble(value);
                        break;
                    case "maxMoveSoundRadius":
                        maxMoveSoundRadius= Double.parseDouble(value);
                        break;
                    case "windowSoundRadius":
                        windowSoundRadius= Double.parseDouble(value);
                        break;
                    case "doorSoundRadius":
                        doorSoundRadius= Double.parseDouble(value);
                        break;
                    case "targetArea":
                        targetArea = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        break;
                    case "spawnAreaIntruders":
                        spawnAreaIntruders = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        break;
                    case "spawnAreaGuards":
                        spawnAreaGuards = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        break;
                    case "wall":
                        tmp = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        walls.add(tmp);
                        break;
                    case "shaded":
                        ShadedArea tmpShaded = new ShadedArea(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        shaded.add(tmpShaded);
                        break;
                    case "teleport":
                        TelePortal teletmp = new TelePortal(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]),Integer.parseInt(items[8]),Integer.parseInt(items[9]));
                        teleports.add(teletmp);
                        break;
                    case "door":
                        Door door = new Door(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        doors.add(door);
                        break;
                    case "window":
                        Window window = new Window(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        windows.add(window);
                        break;
                    case "sentry":
                        SentryTower sentry = new SentryTower(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]),Integer.parseInt(items[8]),Integer.parseInt(items[9]),Integer.parseInt(items[10]),Integer.parseInt(items[11]),Integer.parseInt(items[12]),Integer.parseInt(items[13]),Integer.parseInt(items[14]),Integer.parseInt(items[15]));
                        sentryTowers.add(sentry);
                        break;
                }
            }
        }
    }

    protected void initialize(){
        spawnAgents();
        setSlowDownModifiers();

    }

    private void setSlowDownModifiers(){
        slowDownModifiers = new SlowDownModifiers(slowDownModifierWindow,slowDownDoor,slowDownSentryTower);
        Door.setSlowDownModifier(slowDownDoor);
        SentryTower.setSlowDownModifer(slowDownSentryTower);
        Window.setSlowDownModifier(slowDownModifierWindow);
    }

    protected void spawnAgents(){
        spawnGuards();
        spawnIntruders();
    }

    private void spawnGuards(){
        guards = new ArrayList<>(numGuards);
        for (int i =0; i<numGuards;i++){

            int randomX = ThreadLocalRandom.current().nextInt(spawnAreaGuards.leftBoundary, spawnAreaGuards.rightBoundary + 1);
            int randomY = ThreadLocalRandom.current().nextInt(spawnAreaGuards.topBoundary, spawnAreaGuards.bottomBoundary + 1);
            Point guardPosition = new Point(randomX,randomY);

            GuardController guard = new GuardController(guardPosition,4,maxRotationAngleDegrees,baseSpeedGuard);
            guards.add(guard);
        }
    }

    private void spawnIntruders(){
        intruders = new ArrayList<>(numIntruders);
        for (int i =0; i<numIntruders;i++){

            int randomX = ThreadLocalRandom.current().nextInt(spawnAreaIntruders.leftBoundary, spawnAreaIntruders.rightBoundary + 1);
            int randomY = ThreadLocalRandom.current().nextInt(spawnAreaIntruders.topBoundary, spawnAreaIntruders.bottomBoundary + 1);
            //System.out.println(randomX + " " + randomY);
            Point intruderPosition = new Point(randomX,randomY);

            IntruderController intruder = new IntruderController(intruderPosition,4,maxMoveDistanceIntruder,maxSprintDistanceIntruder,maxRotationAngleDegrees);
            intruders.add(intruder);
        }
    }

}
