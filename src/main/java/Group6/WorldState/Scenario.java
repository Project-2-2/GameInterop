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
import java.util.Scanner;
import java.util.ArrayList;

/**
 * @author joel
 */
public class Scenario {

    protected double baseSpeedIntruder;
    protected double sprintSpeedIntruder;
    protected double baseSpeedGuard;

    protected String mapDoc;
    protected int gameMode;
    private final Path filePath;
    private final static Charset ENCODING = StandardCharsets.UTF_8;

    protected String name;
    protected String gameFile;
    protected int mapHeight;
    protected int mapWidth;
    protected double scaling;
    protected int numIntruders;
    protected int numGuards;
    protected Quadrilateral spawnAreaIntruders;
    protected Quadrilateral spawnAreaGuards;
    protected Quadrilateral targetArea;
    protected ArrayList<Quadrilateral> walls;
    protected ArrayList<Quadrilateral> teleports;
    protected ArrayList<Quadrilateral> shaded;

    public Scenario(String mapFile) {
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
                Quadrilateral tmp;
                switch(id)
                {
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
                    case "baseSpeedIntruder":
                        baseSpeedIntruder = Double.parseDouble(value);
                        break;
                    case "sprintSpeedIntruder":
                        sprintSpeedIntruder = Double.parseDouble(value);
                        break;
                    case "baseSpeedGuard":
                        baseSpeedGuard = Double.parseDouble(value);
                        break;
                    case "targetArea":
                        targetArea = new Quadrilateral(
                            new Point(Integer.parseInt(items[0]), Integer.parseInt(items[1])),
                            new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),
                            new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),
                            new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7]))
                        );
                        break;
                    case "spawnAreaIntruders":
                        spawnAreaIntruders = new Quadrilateral(
                            new Point(Integer.parseInt(items[0]), Integer.parseInt(items[1])),
                            new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),
                            new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),
                            new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7]))
                        );
                        break;
                    case "spawnAreaGuards":
                        spawnAreaGuards = new Quadrilateral(
                            new Point(Integer.parseInt(items[0]), Integer.parseInt(items[1])),
                            new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),
                            new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),
                            new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7]))
                        );
                        break;
                    case "wall":
                        tmp = new Quadrilateral(
                            new Point(Integer.parseInt(items[0]), Integer.parseInt(items[1])),
                            new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),
                            new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),
                            new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7]))
                        );
                        walls.add(tmp);
                        break;
                    case "shaded":
                        tmp = new Quadrilateral(
                            new Point(Integer.parseInt(items[0]), Integer.parseInt(items[1])),
                            new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),
                            new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),
                            new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7]))
                        );
                        shaded.add(tmp);
                        break;
                    case "teleport":
                        Quadrilateral teletmp = new Quadrilateral(
                            new Point(Integer.parseInt(items[0]), Integer.parseInt(items[1])),
                            new Point(Integer.parseInt(items[2]), Integer.parseInt(items[3])),
                            new Point(Integer.parseInt(items[4]), Integer.parseInt(items[5])),
                            new Point(Integer.parseInt(items[6]), Integer.parseInt(items[7]))
                        );
                        teleports.add(teletmp);
                        break;
                    case "texture":
                        // still to do. First the coordinates, then an int with texture type and then a double with orientation
                }
            }
        }
    }

    public ArrayList<Quadrilateral> getWalls(){
        return walls;
    }

    public ArrayList<Quadrilateral> getShaded(){
        return shaded;
    }

    public ArrayList<Quadrilateral> getTeleportals(){
        return teleports;
    }

    public Quadrilateral getTargetArea(){
        return targetArea;
    }

    public int getNumGuards(){
        return numGuards;
    }

    public String getGameFile(){
        return gameFile;
    }

    public String getMapDoc(){
        return mapDoc;
    }

    public double getScaling(){
        return scaling;
    }
}
