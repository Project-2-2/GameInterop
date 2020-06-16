package Group11.Agents;

import Group9.math.Vector2;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;

/*
  To efficiently store the map for the agent and for an efficient reading of the map for any algorithm,
  we impose a grid structure on the continuous space map. The smaller the grid, the more detailed and accurate
  the stored map will be, but also more details means more storage point, thus being less efficient.
  A clear balance between the details stored, and the efficiency of the grid is therefore key for the
  successful use and implementation of this map storing method.

  Each grid has a value that represents the object, if any, in that grid according to the perception of the agent.
  All valid values for the grid are described below, the project guidelines can be consulted for an extensive
  description of each object.

  The objects are sorted from empty (free) tot solid (walls).
  0 - Empty: the agent is free to "walk" here
  1 - Flags (pheromones): follow?
  2 - Stairs (teleport)
  3 - Target area
  4 - Friendly agent
  5 - Hostile agent
  6 - Opaque object: door, shaded area and sentry tower
  7 - Walls: the agent cannot "walk" here

  !!! IF ANY OBJECTS ARE MISSING OR NEW ONES ARE NEEDED FOR AN ALGORITHM, PLEASE ADD THEM ACCORDINGLY !!!

  This class is meant to be used by guards as well as intruders, therefore it is general and not always applicable
  to both because each agent has different abilities.

 */

/**
 * Todo
 *  - Calibrate object position on the map
 *  

 */

public class SquareMap {

    // Our map
    private static ArrayList<ArrayList<String>> mentalMap; // Overall storage of the explored map from spawning point

    public ArrayList<ArrayList<Square>> squareMap;

    // Necessary variables
    private static double gridSize = 1; // Grid size
    private static int agentSize = 1; // Agent size
    private static int objects = 0;
    private static double agentX = 0.0;
    private static double agentY = 0.0;
    private static double direction = 0;
    private static int vision = 10;
    private static double newAngle = 0.0;
    private static double oldAngle = 0.0;


    public static void initializeMap() {
        agentX = 0.0;
        agentY = 0.0;
        direction = 0;
        mentalMap = new ArrayList<>();

        // Initialize starting position
        int startSize = (int) (agentSize/ gridSize);
        update(startSize, 0, 0, "@");

        // Initialize buffer
        int bufferSize = (int) (vision / gridSize); // Viewing distance / grid size

        // Extend the map north
        extendNorth(bufferSize, ".");

        // Extend the map east
        extendEast(bufferSize, ".");

        // Extend the map south
        extendSouth(bufferSize, ".");

        // Extend the map west
        extendWest(bufferSize, ".");

        // Visualize the initial mental map
        drawMap();
    }

    private static void extendNorth(int rows, String element) {
        for (int row = 0; row < rows; row++) {
            mentalMap.add(0, new ArrayList<>());

            for (int col = 0; col < mentalMap.get(row+1).size(); col++) {
                mentalMap.get(0).add(col, element);
            }
            agentY += gridSize;
        }
    }

    private static void extendEast(int cols, String element) {
        for (int row = 0; row < mentalMap.size(); row++) {

            for (int col = 0; col < cols; col++) {
                mentalMap.get(row).add(element);
            }
        }
    }

    private static void extendSouth(int rows, String element) {
        for (int row = 0; row < rows; row++) {
            mentalMap.add(new ArrayList<>());

            for (int col = 0; col < mentalMap.get(row).size(); col++) {
                mentalMap.get(mentalMap.size()-1).add(element);
            }
        }
    }

    private static void extendWest(int cols, String element) {
        for (int row = 0; row < mentalMap.size(); row++) {

            for (int col = 0; col < cols; col++) {
                mentalMap.get(row).add(col, element);
            }
        }
        for (int i = 0; i < cols; i++) { agentX += gridSize; }
    }

    private static void update(int size, int rowStart, int colStart, String element){
        for (int row = rowStart; row < (rowStart + size); row++) {
            mentalMap.add(row, new ArrayList<>());

            for (int col = colStart; col < (colStart+ size); col++) {
                mentalMap.get(row).add(col,element);
            }
        }
    }

    public static void updateMap(GuardPercepts percepts, Rotate rotatedAngle, Move movedDistance) {

        // Check if the agent has been initialised
        if (rotatedAngle != null || movedDistance != null) {

            // Update agent position to current location and rotation angle
            updateLocation(percepts, rotatedAngle, movedDistance);

            // Expand map
            if ((Math.ceil(agentY - vision) / gridSize) < 0) {
                //System.out.println("Expand north " + ((agentY - vision) / gridSize) + " < 0 ");
                extendNorth((int) (Math.ceil(Math.abs((agentY - vision) / gridSize))), ".");
            }
            if ((Math.ceil(agentX - vision) / gridSize) < 0) {
                //System.out.println("Expand west " + ((agentX - vision) / gridSize) + " < 0 ");
                extendWest((int) (Math.ceil(Math.abs((agentX - vision) / gridSize))), ".");
            }
            if ((Math.ceil(agentX + vision) / gridSize) > mentalMap.get(1).size()) {
                //System.out.println("Expand east " + ((agentX + vision) / gridSize) + " > " + mentalMap.get(1).size());
                extendEast((int) (Math.ceil(Math.abs((agentX + vision) / gridSize) - mentalMap.get(1).size())), ".");
            }
            if ((Math.ceil(agentY + vision) / gridSize) > mentalMap.size()) {
                //System.out.println("Expand south " + ((agentY + vision) / gridSize) + " > " + mentalMap.size());
                extendSouth((int) (Math.ceil(Math.abs((agentY + vision) / gridSize) - mentalMap.size())), ".");
            }

            // Set objects to array
            ObjectPercept[] array = percepts.getVision().getObjects().getAll().toArray(new ObjectPercept[0]);

            // For every visible object
            for (int i = 0; i < percepts.getVision().getObjects().getAll().size(); i++) {

                // If an object is solid (a wall)
                if (array[i].getType().isSolid()) {

                    // Calculate the angle of the object point
                    double objectAngle = Math.atan((array[i].getPoint().getX() / array[i].getPoint().getY()));

                    // Calculate the distance of the object point to the agent
                    double objectDistance = Math.sqrt(Math.pow(Math.abs(array[i].getPoint().getY()), 2) + Math.pow(Math.abs(array[i].getPoint().getX()), 2));

                    // Add the agent angle to the object angle
                    objectAngle += direction;//rotatedAngle.getAngle().getRadians();


                    // Check if the angle is more or less than 1 full circle
                    if (objectAngle > (2 * Math.PI)) {
                        objectAngle -= (2 * Math.PI);
                    }
                    if (direction < 0) {
                        direction += (2 * Math.PI);
                    }
                    double objectY = ((Math.cos(objectAngle) * objectDistance) + agentY);
                    double objectX = ((Math.sin(objectAngle) * objectDistance) + agentX);
                    //System.out.println("Obj = " + objectY + " " + objectX);

                    ObjectPerceptType perceptType = array[i].getType();
                    updateMental(objectX, objectY, "1");

                }
            }
        }
        // Visualize the updated mental map
        drawMap();
    }

    public static void updateLocation(GuardPercepts percepts, Rotate rotatedAngle, Move movedDistance) {
        if (rotatedAngle != null) {
            System.out.println("Angle = " + rotatedAngle.getAngle().getRadians());
        }
        System.out.println("Distance = " + movedDistance.getDistance().getValue());
        System.out.println("Last executed = " + percepts.wasLastActionExecuted());

        if (!percepts.wasLastActionExecuted()) {

            // Get the current agent direction
            direction += rotatedAngle.getAngle().getRadians();
            if (direction > (2 * Math.PI)) {
                direction -= (2 * Math.PI);
            }
            if (direction < 0) {
                direction += (2 * Math.PI);
            }
        }

        else {
            // Get the moved position of the agent
            agentX += (Math.sin(direction) * movedDistance.getDistance().getValue());
            agentY += (Math.cos(direction) * movedDistance.getDistance().getValue());
        }
        System.out.println("Direction = " + direction);
    }

    private static void drawMap () {
        // Method for visualising the grid map.
        System.out.println();

        for (ArrayList<String> strings : mentalMap) {
            for (String string : strings) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }
    private static void updateMental(double x, double y, String element){
        int mapPosX = (int) Math.ceil(x/gridSize);
        int mapPosY = (int) Math.ceil(y/gridSize);

        //System.out.println("Loc = " + mapPosY + " " + mapPosX);
        //mentalMap.get((int) agentY).set((int) agentX, "+");

        mentalMap.get(mapPosY).set(mapPosX, element);
    }
}
