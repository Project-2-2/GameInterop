package Group5.GameController;

import Interop.Agent.Guard;
import Interop.Geometry.Point;

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
    protected ArrayList<Area> shaded;
    protected double baseSpeedIntruder;
    protected double baseSpeedGuard;
    protected double sprintSpeedIntruder;
    protected int gameMode;

    protected ArrayList<GuardController> guards;
    protected ArrayList<IntruderController> intruders;

    public void readMap(String filePath){
        try (Scanner scanner =  new Scanner(Paths.get(filePath), ENCODING.name())){
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
                Area tmp;
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
                        tmp = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]));
                        shaded.add(tmp);
                        break;
                    case "teleport":
                        TelePortal teletmp = new TelePortal(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Integer.parseInt(items[6]),Integer.parseInt(items[7]),Integer.parseInt(items[8]),Integer.parseInt(items[9]));
                        teleports.add(teletmp);
                        break;
                    case "texture":
                        // still to do. First the coordinates, then an int with texture type and then a double with orientation
                }
            }
        }
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

            GuardController guard = new GuardController(guardPosition,4);
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

            IntruderController intruder = new IntruderController(intruderPosition,4);
            intruders.add(intruder);
        }
    }

}
