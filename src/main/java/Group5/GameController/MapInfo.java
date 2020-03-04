package Group5.GameController;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

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
                        targetArea = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]));
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
                    case "shaded":
                        tmp = new Area(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]));
                        shaded.add(tmp);
                        break;
                    case "teleport":
                        TelePortal teletmp = new TelePortal(Integer.parseInt(items[0]),Integer.parseInt(items[1]),Integer.parseInt(items[2]),Integer.parseInt(items[3]),Integer.parseInt(items[4]),Integer.parseInt(items[5]),Double.parseDouble(items[6]));
                        teleports.add(teletmp);
                        break;
                    case "texture":
                        // still to do. First the coordinates, then an int with texture type and then a double with orientation
                }
            }
        }
    }

}
