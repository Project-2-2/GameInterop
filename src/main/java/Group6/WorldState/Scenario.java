/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Group6.WorldState;

import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;

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

    private String mapDoc;
    private final Path filePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    private int gameMode;
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

    public Scenario(String mapFile) {
        // set parameters
        mapDoc = mapFile;

        // initialize variables
        walls = new ArrayList<>(); // create list of walls
        shadedAreas = new ArrayList<>(); // create list of low-visability areas
        teleports = new ArrayList<>(); // create list of teleports e.g. stairs
        doors = new ArrayList<>();
        windows = new ArrayList<>();
        sentryTowers = new ArrayList<>();

        // read scenario
        filePath = Paths.get(mapDoc); // get path
        readMap();
    }

    public void readMap() {
        try (Scanner scanner =  new Scanner(filePath, ENCODING.name())) {
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
                    switch (id) {
                        case "gameMode":
                            gameMode = Integer.parseInt(value);
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
                        case "viewRangeIntruderNomal":
                            viewRangeIntruderNormal = Double.parseDouble(value);
                            break;
                        case "viewRangeIntruderShaded":
                            viewRangeIntruderShaded = Double.parseDouble(value);
                            break;
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
            new Point(Integer.parseInt(items[0]), Integer.parseInt(items[1])),
            new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),
            new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),
            new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7]))
        );
    }

    public String getMapDoc() {
        return mapDoc;
    }

    public int getGameMode() {
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

    public double getCaptureDistance() {
        return captureDistance;
    }

    public int getWinConditionIntruderRounds() {
        return winConditionIntruderRounds;
    }

    public double getMaxRotationAngle() {
        return maxRotationAngle;
    }

    public double getMaxMoveDistanceIntruder() {
        return maxMoveDistanceIntruder;
    }

    public double getMaxSprintDistanceIntruder() {
        return maxSprintDistanceIntruder;
    }

    public double getMaxMoveDistanceGuard() {
        return maxMoveDistanceGuard;
    }

    public int getSprintCooldown() {
        return sprintCooldown;
    }

    public int getPheromoneCooldown() {
        return pheromoneCooldown;
    }

    public double getRadiusPheromone() {
        return radiusPheromone;
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

    public double getViewAngle() {
        return viewAngle;
    }

    public int getViewRays() {
        return viewRays;
    }

    public double getViewRangeIntruderNormal() {
        return viewRangeIntruderNormal;
    }

    public double getViewRangeIntruderShaded() {
        return viewRangeIntruderShaded;
    }

    public double getViewRangeGuardNormal() {
        return viewRangeGuardNormal;
    }

    public double getViewRangeGuardShaded() {
        return viewRangeGuardShaded;
    }

    public double getViewRangeSentryStart() {
        return viewRangeSentryStart;
    }

    public double getViewRangeSentryEnd() {
        return viewRangeSentryEnd;
    }

    public double getYellSoundRadius() {
        return yellSoundRadius;
    }

    public double getMaxMoveSoundRadius() {
        return maxMoveSoundRadius;
    }

    public double getWindowSoundRadius() {
        return windowSoundRadius;
    }

    public double getDoorSoundRadius() {
        return doorSoundRadius;
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

    public ArrayList<Quadrilateral> getWalls() {
        return walls;
    }

    public ArrayList<Quadrilateral> getDoors() {
        return doors;
    }

    public ArrayList<Quadrilateral> getWindows() {
        return windows;
    }

    public ArrayList<Quadrilateral> getSentryTowers() {
        return sentryTowers;
    }

    public ArrayList<Teleport> getTeleports() {
        return teleports;
    }

    public ArrayList<Quadrilateral> getShadedAreas() {
        return shadedAreas;
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
