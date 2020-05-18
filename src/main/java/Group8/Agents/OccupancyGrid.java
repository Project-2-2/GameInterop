package Group8.Agents;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Thomas Sijpkens
 */
public class OccupancyGrid {
    //the higher the value of xsize and ysize the greater the accuracy at cost of complexity.
    private final double xsize = 80; //X size of map
    private final double ysize = 120; //Y size of map
    private double grid_size;

    //OccupancyGrid to be updated by the agent.
    //public ArrayList<ArrayList<Boolean>> occupancyGrid = new ArrayList<ArrayList<Boolean>>();
    //public ArrayList<ArrayList<Double>> logMap = new ArrayList<ArrayList<Double>>();

    //This automatically sets all values to false but now we can't go further
    public boolean[][] occupancyGrid = new boolean[(int)xsize][(int)ysize];
    public double[][] logMap = new double[(int)xsize][(int)ysize];
    public OccupancyGrid() {
        //instansiate all columns of occupancyGrid

//        for(int i = 0; i <= ysize; i++) {
//            occupancyGrid.add(new ArrayList<Boolean>());
//            logMap.add(new ArrayList<Double>());
//        }
//
//        //By setting occupancyGrid all to false we assume entire world is empty at instansiation of agent.
//        //pre-allocation is faster in the long run
//        //TODO: O(n^2) find better way of implementing.
//        for(int i = 0; i <= xsize; i++) {
//            for(int j = 0; j <= ysize; j++){
//                occupancyGrid.get(i).add(i, false);
//                //Equal change of being empty and occupied
//                logMap.get(i).add(i, 0.5);
//            }
//        }
    }

    /**
     * Updates the occupancy grid based on OccupancyAgent calculation.
     * This is called upon from the OccupancyAgent.
     * @param indexX the x index where the agent makes an update to.
     * @param indexY the y index where the agent make an update to
     * @param occValue index set true if grid is occupied, else false.
     */
    public void update(int indexX, int indexY, Boolean occValue) {
        occupancyGrid[indexX][indexY] = occValue;
    }

    /**
     * Updates the occupancy grid based on OccupancyAgent calculation.
     * This is called upon from the OccupancyAgent.
     * @param indexX the x index where the agent makes an update to.
     * @param indexY the y index where the agent make an update to
     */
    public void update(int indexX, int indexY) {
        occupancyGrid[indexX][indexY] = true;
    }

    public void logUpdate(int x, int y, double value) { logMap[x][y] = value; }

    public double getLogValue(int x, int y) {
        return logMap[x][y];
    }
}

class OccupancyGridTest {
    public static void main(String[] args) {
        OccupancyGrid occupancyGrid = new OccupancyGrid();

        System.out.println(Arrays.deepToString(occupancyGrid.occupancyGrid));
        System.out.println(Arrays.deepToString(occupancyGrid.logMap));

    }
}
