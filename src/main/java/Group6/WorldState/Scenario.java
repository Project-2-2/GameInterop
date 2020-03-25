/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Group6.WorldState;

import Group6.Geometry.Angle;
import Group6.Geometry.Collection.Quadrilaterals;
import Group6.Geometry.Distance;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;
import Group6.WorldState.Object.Teleport;
import Group6.WorldState.Object.WorldStateObjects;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Vision.ObjectPerceptType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * @author joel
 * @author Tomasz Darmetko
 */
public class Scenario {

    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private GameMode gameMode;
    private int height;
    private int width;
    private int numGuards;
    private int numIntruders;
    private double captureDistance;
    private int winConditionIntruderRounds;
    private double maxRotationAngle;
    private double maxMoveDistanceIntruder;
    private double maxSprintDistanceIntruder;
    private double maxMoveDistanceGuard;
    private int sprintCooldown;
    private int pheromoneCooldown;
    private int pheromoneExpireRounds;
    private double radiusPheromone;
    private double slowDownModifierWindow;
    private double slowDownModifierDoor;
    private double slowDownModifierSentryTower;
    private double viewAngle;
    private int viewRays;
    private double viewRangeIntruderNormal;
    private double viewRangeIntruderShaded;
    private double viewRangeGuardNormal;
    private double viewRangeGuardShaded;
    private double viewRangeSentryStart;
    private double viewRangeSentryEnd;
    private double yellSoundRadius;
    private double maxMoveSoundRadius;
    private double windowSoundRadius;
    private double doorSoundRadius;

    private Quadrilateral spawnAreaIntruders;
    private Quadrilateral spawnAreaGuards;
    private Quadrilateral targetArea;
    private ArrayList<Quadrilateral> walls;
    private ArrayList<Quadrilateral> doors;
    private ArrayList<Quadrilateral> windows;
    private ArrayList<Quadrilateral> sentryTowers;
    private ArrayList<Teleport> teleports;
    private ArrayList<Quadrilateral> shadedAreas;

    private WorldStateObjects worldStateObjects;

    public Scenario(String mapFile) {
        
        // initialize variables
        walls = new ArrayList<>(); // create list of walls
        shadedAreas = new ArrayList<>(); // create list of low-visability areas
        teleports = new ArrayList<>(); // create list of teleports e.g. stairs
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        sentryTowers = new ArrayList<>();

        readMap(Paths.get(mapFile));

        worldStateObjects = new WorldStateObjects(
            new WorldStateObjects(getWalls(), ObjectPerceptType.Wall),
            new WorldStateObjects(getDoors(), ObjectPerceptType.Door),
            new WorldStateObjects(getWindows(), ObjectPerceptType.Window),
            new WorldStateObjects(getSentryTowers(), ObjectPerceptType.SentryTower),
            getTeleports().toObjects(),
            new WorldStateObjects(getShadedAreas(), ObjectPerceptType.ShadedArea)
        );

    }

    public void readMap(Path filePath) {
        try (Scanner scanner = new Scanner(filePath, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                parseLine(scanner.nextLine());
            }
        } catch (ScenarioException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseLine(String line) {
        //use a second Scanner to parse the content of each line
        try(Scanner scanner = new Scanner(line)) {

            scanner.useDelimiter("=");

            if (scanner.hasNext()) {

                // read id value pair
                String id = scanner.next();
                String value = scanner.next();
                // trim excess spaces
                value = value.trim();
                id = id.trim();
                // in case multiple parameters
                String[] items = value.split(",");
                Quadrilateral tmp;

                try {
                    if(id.trim().startsWith("//")) return;
                    switch (id) {
                        case "gameMode":
                            switch (Integer.parseInt(value)) {
                                case 0:
                                    gameMode = GameMode.CaptureAllIntruders;
                                    break;
                                case 1:
                                    gameMode = GameMode.CaptureOneIntruder;
                                    break;
                                default:
                                    throw new RuntimeException("Wrong game mode: " + value + " !");
                            }
                            break;
                        case "height":
                            height = Integer.parseInt(value);
                            break;
                        case "width":
                            width = Integer.parseInt(value);
                            break;
                        case "numGuards":
                            numGuards = Integer.parseInt(value);
                            break;
                        case "numIntruders":
                            numIntruders = Integer.parseInt(value);
                            break;
                        case "captureDistance":
                            captureDistance = Double.parseDouble(value);
                            break;
                        case "winConditionIntruderRounds":
                            winConditionIntruderRounds = Integer.parseInt(value);
                            break;
                        case "maxRotationAngle":
                            maxRotationAngle = Double.parseDouble(value);
                            break;
                        case "maxMoveDistanceIntruder":
                            maxMoveDistanceIntruder = Double.parseDouble(value);
                            break;
                        case "maxSprintDistanceIntruder":
                            maxSprintDistanceIntruder = Double.parseDouble(value);
                            break;
                        case "maxMoveDistanceGuard":
                            maxMoveDistanceGuard = Double.parseDouble(value);
                            break;
                        case "sprintCooldown":
                            sprintCooldown = Integer.parseInt(value);
                            break;
                        case "pheromoneCooldown":
                            pheromoneCooldown = Integer.parseInt(value);
                            break;
                        case "pheromoneExpireRounds":
                            pheromoneExpireRounds = Integer.parseInt(value);
                            break;
                        case "radiusPheromone":
                            radiusPheromone = Double.parseDouble(value);
                            break;
                        case "slowDownModifierWindow":
                            slowDownModifierWindow = Double.parseDouble(value);
                            break;
                        case "slowDownModifierDoor":
                            slowDownModifierDoor = Double.parseDouble(value);
                            break;
                        case "slowDownModifierSentryTower":
                            slowDownModifierSentryTower = Double.parseDouble(value);
                            break;
                        case "viewAngle":
                            viewAngle = Double.parseDouble(value);
                            break;
                        case "viewRays":
                            viewRays = Integer.parseInt(value);
                            break;
                        case "viewRangeIntruderNormal":
                        case "viewRangeIntruderNomal":
                            viewRangeIntruderNormal = Double.parseDouble(value);
                            break;
                        case "viewRangeIntruderShaded":
                            viewRangeIntruderShaded = Double.parseDouble(value);
                            break;
                        case "viewRangeGuardNormal":
                        case "viewRangeGuardNomal":
                            viewRangeGuardNormal = Double.parseDouble(value);
                            break;
                        case "viewRangeGuardShaded":
                            viewRangeGuardShaded = Double.parseDouble(value);
                            break;
                        case "viewRangeSentry":
                            viewRangeSentryStart = Double.parseDouble(items[0]);
                            viewRangeSentryEnd = Double.parseDouble(items[1]);
                            break;
                        case "yellSoundRadius":
                            yellSoundRadius = Double.parseDouble(value);
                            break;
                        case "maxMoveSoundRadius":
                            maxMoveSoundRadius = Double.parseDouble(value);
                            break;
                        case "windowSoundRadius":
                            windowSoundRadius = Double.parseDouble(value);
                            break;
                        case "doorSoundRadius":
                            doorSoundRadius = Double.parseDouble(value);
                            break;
                        case "targetArea":
                            targetArea = readArea(items);
                            break;
                        case "spawnAreaIntruders":
                            spawnAreaIntruders = readArea(items);
                            break;
                        case "spawnAreaGuards":
                            spawnAreaGuards = readArea(items);
                            break;
                        case "wall":
                            tmp = readArea(items);
                            walls.add(tmp);
                            break;
                        case "door":
                            tmp = readArea(items);
                            doors.add(tmp);
                            break;
                        case "window":
                            tmp = readArea(items);
                            windows.add(tmp);
                            break;
                        case "sentry":
                            tmp = readArea(items);
                            sentryTowers.add(tmp);
                            break;
                        case "shaded":
                            tmp = readArea(items);
                            shadedAreas.add(tmp);
                            break;
                        case "teleport":
                            Quadrilateral thisSide = readArea(Arrays.copyOfRange(items, 0, 8));
                            Quadrilateral thatSide = readArea(Arrays.copyOfRange(items, 8, 16));
                            teleports.add(new Teleport(thisSide, thatSide));
                            break;
                        default:
                            throw new ScenarioException("Unknown key: " + id);
                    }
                } catch (ScenarioException e) {
                    throw e;
                } catch (Exception e) {
                    throw new ScenarioException(
                        "An exception occurred while reading key: " + id + "\n" +
                        "Read value: " + value,
                        e
                    );
                }
            }
        } catch (ScenarioException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(
                "An exception occurred while extracting a value from a scenario file.\n" +
                "The line with error: \n" +
                line,
                e
            );
        }
    }

    private Quadrilateral readArea(String[] items) {
        return new Quadrilateral(
            new Point(Double.parseDouble(items[0]), Double.parseDouble(items[1])),
            new Point(Double.parseDouble(items[2]), Double.parseDouble(items[3])),
            new Point(Double.parseDouble(items[4]), Double.parseDouble(items[5])),
            new Point(Double.parseDouble(items[6]), Double.parseDouble(items[7]))
        );
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNumGuards() {
        return numGuards;
    }

    public int getNumIntruders() {
        return numIntruders;
    }

    public Distance getCaptureDistance() {
        return new Distance(captureDistance);
    }

    public int getWinConditionIntruderRounds() {
        return winConditionIntruderRounds;
    }

    public Angle getMaxRotationAngle() {
        return Angle.fromDegrees(maxRotationAngle);
    }

    public Distance getMaxMoveDistanceIntruder() {
        return new Distance(maxMoveDistanceIntruder);
    }

    public Distance getMaxSprintDistanceIntruder() {
        return new Distance(maxSprintDistanceIntruder);
    }

    public Distance getMaxMoveDistanceGuard() {
        return new Distance(maxMoveDistanceGuard);
    }

    public int getSprintCooldown() {
        return sprintCooldown;
    }

    public int getPheromoneCooldown() {
        return pheromoneCooldown;
    }

    public int getPheromoneExpireRounds() {
        return pheromoneExpireRounds;
    }

    public Distance getRadiusPheromone() {
        return new Distance(radiusPheromone);
    }

    public double getSlowDownModifierWindow() {
        return slowDownModifierWindow;
    }

    public double getSlowDownModifierDoor() {
        return slowDownModifierDoor;
    }

    public double getSlowDownModifierSentryTower() {
        return slowDownModifierSentryTower;
    }

    public Angle getViewAngle() {
        return Angle.fromDegrees(viewAngle);
    }

    public int getViewRays() {
        return viewRays;
    }

    public Distance getViewRangeIntruderNormal() {
        return new Distance(viewRangeIntruderNormal);
    }

    public Distance getViewRangeIntruderShaded() {
        return new Distance(viewRangeIntruderShaded);
    }

    public Distance getViewRangeGuardNormal() {
        return new Distance(viewRangeGuardNormal);
    }

    public Distance getViewRangeGuardShaded() {
        return new Distance(viewRangeGuardShaded);
    }

    public Distance getViewRangeSentryStart() {
        return new Distance(viewRangeSentryStart);
    }

    public Distance getViewRangeSentryEnd() {
        return new Distance(viewRangeSentryEnd);
    }

    public Distance getYellSoundRadius() {
        return new Distance(yellSoundRadius);
    }

    public Distance getMaxMoveSoundRadius() {
        return new Distance(maxMoveSoundRadius);
    }

    public Distance getWindowSoundRadius() {
        return new Distance(windowSoundRadius);
    }

    public Distance getDoorSoundRadius() {
        return new Distance(doorSoundRadius);
    }

    public Quadrilateral getSpawnAreaIntruders() {
        return spawnAreaIntruders;
    }

    public Quadrilateral getSpawnAreaGuards() {
        return spawnAreaGuards;
    }

    public Quadrilateral getTargetArea() {
        return targetArea;
    }

    public Quadrilaterals getWalls() {
        return new Quadrilaterals(walls);
    }

    //get all walls in an array list
    public ArrayList<Quadrilateral> getWallsAL(){return walls;}

    public Quadrilaterals getDoors() {
        return new Quadrilaterals(doors);
    }

    public Quadrilaterals getWindows() {
        return new Quadrilaterals(windows);
    }

    public Quadrilaterals getSentryTowers() {
        return new Quadrilaterals(sentryTowers);
    }

    public Teleports getTeleports() {
        return new Teleports(teleports);
    }

    public Quadrilaterals getShadedAreas() {
        return new Quadrilaterals(shadedAreas);
    }

    public WorldStateObjects getObjects() {
        return worldStateObjects;
    }

    static private class ScenarioException extends RuntimeException {
        public ScenarioException() {
        }
        public ScenarioException(String s) {
            super(s);
        }
        public ScenarioException(String s, Exception e) {
            super(s, e);
        }
    }

}
