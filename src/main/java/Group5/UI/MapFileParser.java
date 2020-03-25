package Group5.UI;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapFileParser {

    private static DrawableIntruderAgent explorer;
    private static DrawableIntruderAgent intruder;
    private static DrawableGuardAgent guard;
    private static int gameMode;
    private static String gameFile;
    private static int mapHeight;
    private static int mapWidth;
    private static int numIntruders;
    private static int numGuards;
    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private static List<Shape> drawableObjects = new ArrayList<>();
    private static List<Circle> drawableAgents = new ArrayList<>();

    public static void readMapFile(File file) {
        try (Scanner scanner = new Scanner(file, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                readLine(scanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse lines and create a Shape objects based on the file input.
     * Create variables to be stored and used by the Controller, based in the file input.
     * @param line line to be parsed from the input txt file
     */
    public static void readLine(String line) {
        try (Scanner scanner = new Scanner(line)) {
            scanner.useDelimiter("=");
            if (scanner.hasNext()) {
                // read id value pair
                String id = scanner.next();
                String value = scanner.next();
                // trim excess spaces
                value = value.trim();
                id = id.trim();
                // in case multiple parameters
                String[] items = value.split(" ");
                switch (id) {
                    case "gameMode":
                        gameMode = Integer.parseInt(value); // 0 is exploration, 1 evasion pursuit game
                        if (gameMode == 0) {
                            explorer = new DrawableIntruderAgent(0, 0, 7.5, Color.CYAN);
                            drawableAgents.add(explorer);
                            DrawableObject visionFieldExplorer = new DrawableObject(0,0,0,0,0,0);
                            visionFieldExplorer.setFill(Color.YELLOW);
                            drawableObjects.add(visionFieldExplorer);
                        }
                        break;
                    case "height":
                        mapHeight = Integer.parseInt(value);
                        break;
                    case "width":
                        mapWidth = Integer.parseInt(value);
                        break;
                    case "gameFile":
                        gameFile = value;
                        break;
                    case "numGuards":
                        numGuards = Integer.parseInt(value);
                        if (gameMode != 0) {
                            for (int i = 0; i < numGuards; i++) {
                                guard = new DrawableGuardAgent(3, 5, 3, Color.BLUE);
                                drawableAgents.add(guard);
                            }
                        }
                        break;
                    case "numIntruders":
                        numIntruders = Integer.parseInt(value);
                        if (gameMode != 0) {
                            for (int i = 0; i < numIntruders; i++) {
                                intruder = new DrawableIntruderAgent(1, 1, 3, Color.PURPLE);
                                drawableAgents.add(intruder);
                            }
                        }
                        break;
                    case "targetArea":
                        DrawableObject targetArea = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        targetArea.setFill(Color.CYAN);
                        drawableObjects.add(targetArea);
                        break;
                    case "spawnAreaIntruders":
                        DrawableObject spawnAreaIntruders = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        spawnAreaIntruders.setFill(Color.RED);
                        drawableObjects.add(spawnAreaIntruders);
                        break;
                    case "spawnAreaGuards":
                        DrawableObject spawnAreaGuards = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        spawnAreaGuards.setFill(Color.BLUE);
                        drawableObjects.add(spawnAreaGuards);
                        break;
                    case "wall":
                        DrawableObject wall = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        wall.setFill(Color.SANDYBROWN);
                        drawableObjects.add(wall);
                        break;
                    case "shaded":
                        DrawableObject shaded = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        shaded.setFill(Color.DARKGRAY);
                        drawableObjects.add(shaded);
                        break;
                    case "teleport":
                        DrawableObject teleport = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        teleport.setFill(Color.GREEN);
                        drawableObjects.add(teleport);
                        break;
                    case "door":
                        DrawableObject door = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        door.setFill(Color.BROWN);
                        drawableObjects.add(door);
                        break;
                    case "window":
                        DrawableObject window = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        window.setFill(Color.SNOW);
                        drawableObjects.add(window);
                        break;
                    case "sentry":
                        DrawableObject sentry = new DrawableObject(Integer.parseInt(items[0]), Integer.parseInt(items[1]), Integer.parseInt(items[2]),
                                Integer.parseInt(items[3]), Integer.parseInt(items[4]), Integer.parseInt(items[5]),
                                Integer.parseInt(items[6]), Integer.parseInt(items[7]));
                        sentry.setFill(Color.BLACK);
                        drawableObjects.add(sentry);
                        break;
                }
            }
        }
    }

    public static List<Shape> getDrawableObjects() {
        return drawableObjects;
    }

    public static List<Circle> getDrawableAgents() {
        return drawableAgents;
    }

    public static int getGameMode() {
        return gameMode;
    }

    public static String getGameFile() {
        return gameFile;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getNumIntruders() {
        return numIntruders;
    }

    public static int getNumGuards() {
        return numGuards;
    }
}

