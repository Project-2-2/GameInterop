package Group8.Agents;

import Group9.Game;
import Interop.Action.DropPheromone;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPercepts;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * @author Thomas Sijpkens
 * class OccupancyAgent uses and Occupancy grid representation of the environment.  This allows us to use ML approaches for the agent.
 */
public class OccupancyAgent implements Guard {
    private Point initialLocation;
    private Random rng;
    private OccupancyGrid occupancyGrid;

    private double xsize; //X size of map
    private double ysize; //Y size of map
    private double grid_size; //how thicc the wall assumption be.

    //Agent has pose defined by (x,y,theta)
    private final int DEGREE_OF_FREEDOM = 3;
    //index 1 is x coordinate, 2 is y coordinate, 3 is robot's heading(theta).
    private double[] pose = new double[3];

    //some modifiable
    private double alpha = 1.0; //How thick can obstacles be
    private double beta = 5.0 * Math.PI/180; //width of FOV.
    private double z_max = 150.0; //how much info could be stored from the laser

    //Log-probabilities to add or remove from the map
    private double log_occ = Math.log(0.65/0.35);
    private double log_free = Math.log(0.35/0.65);

    private GuardPercepts percepts;
    //why the fuck is Angle protected?  I can't use it to define viewAngle.
    private double viewAngle = 45.0;
    private final int viewRays = 45;
    private final double viewRange = 6.0; //I don't know how to pull this of if they are defined

    double xPosition, yPosition;

    private int suroundUpdateIteration = 1;
    private ObjectPercept objectPercept;
    private ObjectPercepts objectPercepts;

    public OccupancyAgent() {
        this.occupancyGrid = new OccupancyGrid();
        xPosition = occupancyGrid.occupancyGrid.length/2.0;
        yPosition = occupancyGrid.occupancyGrid[0].length/2.0;
        initialLocation = new Point(xPosition,yPosition);
        rng = new Random();
    }

    //as defined in https://www.youtube.com/watch?v=Ko7SWZQIawM

    /**
     * @return an optimal estimate of the state of a acell given by a MAP decision rule
     * a cell C is Occupied if P[Mx,y = 1] > P(Mx,y = 0)
     * else if P[Mx,y = 1] > P(Mx,y = 0) then C is empty
     * otw, P[Mx,y = 1] = P(Mx,y = 0) then C is unknown.
     */
    public void maximumAPosteriori() {
        return;
    }


    /**
     * This action is called by another agent, agent B, upon accessing a communication point made by agent A.
     * Agent B verifies false positive rate that of agent A's occupancyGrid.
     *
     * THIS SERVES AS DATA ANALYSIS FOR PHASE 3, TO SEE IF THERE ARE ANY POINTS OF IMPROVEMENTS!
     * !DO NOT CALL METHOD IN FINAL SUBMISSION, OR ELSE AGENT IS EVEN SLOWER!
     *
     * There are only 4 conditional probabilities.
     * P(z = 1|Mx,y = 1) : True occupied measurement
     * P(z = 0|Mx,y = 1) : False free measurement
     * P(z = 1|Mx,y = 0) : False occupied measurement
     * P(z = 0|Mx,y = 0) : True free measurement
     *
     * TODO: develop this into mother duck strategy.
     */
    public void verify(GuardPercepts guardPercepts) throws IOException {
        //take area of inspection

        //Area of inspection.
        ArrayList<ArrayList<Boolean>> inspectionArea = new ArrayList<ArrayList<Boolean>>();

        double inspectionSize = 4 * viewRange;
        //move to center

        //instansiate all columns of occupancyGrid
        for(int i = 0; i <= inspectionSize; i++) {
            inspectionArea.add(new ArrayList<Boolean>());
        }

        FileWriter writer = new FileWriter("verifyOutput.txt");
    }

    private double getSpeedModifier(GuardPercepts guardPercepts)
    {
        SlowDownModifiers slowDownModifiers =  guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers();
        if(guardPercepts.getAreaPercepts().isInWindow())
        {
            return slowDownModifiers.getInWindow();
        }
        else if(guardPercepts.getAreaPercepts().isInSentryTower())
        {
            return slowDownModifiers.getInSentryTower();
        }
        else if(guardPercepts.getAreaPercepts().isInDoor())
        {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }

    /**
     * Basic idea of the algorithm:
     * 1. Calculate the occupancy grid around the map
     * 2. Find longest straight path that can be moved by the agent.
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return
     */
    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if(!percepts.wasLastActionExecuted()) {
            Distance range;
            Angle direction;
            if(suroundUpdateIteration == 1) {
                //update rotation
                suroundUpdateIteration ++;

                //get range() tells us how far to update the OccupancyGrid
                range = percepts.getVision().getFieldOfView().getRange();
                //percepts.getViewAngle() tells which x,y direction we update on
                direction = percepts.getVision().getFieldOfView().getViewAngle();


                // This means that I would halve to calculate 45 degrees with the log update
                return new Rotate(Angle.fromDegrees(90));

            } else if(suroundUpdateIteration == 2) {
                //update rotation
                suroundUpdateIteration ++;

                return new Rotate(Angle.fromDegrees(90));
            } else if(suroundUpdateIteration == 3) {
                //update rotation
                suroundUpdateIteration ++;
                return new Rotate(Angle.fromDegrees(90));

            } else if(suroundUpdateIteration == 4) {
                //update rotation
                suroundUpdateIteration ++;
                return new Rotate(Angle.fromDegrees(90));

            } else {
                //replace rng.nextDouble() to rotating degree.
                return new Rotate(Angle.fromDegrees(rng.nextDouble()));
            }
        } else {
            suroundUpdateIteration = 1;
            //decision rule for finding the longest path
            return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
        }
//        if(!percepts.wasLastActionExecuted())
//        {
//            if(Math.random() < 0.1)
//            {
//                return new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]);
//            }
//            return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
//        }
//        else
//        {
//            return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
//        }
    }

    /**
     * Converts our Prior Map to Posterior map using Bayes formula:
     * P(Mx,y | z) = (P(z | Mx,y) * P(Mx,y)) / P(z)
     */
    public void posteriorMap(GuardPercepts percepts){
        Distance distanceToNearestObject = percepts.getVision().getFieldOfView().getRange();
        Angle directionFacing = percepts.getVision().getFieldOfView().getViewAngle();

    }

    public void priorMap(){
        Distance distanceToNearestObject = percepts.getVision().getFieldOfView().getRange();
        Angle directionFacing = percepts.getVision().getFieldOfView().getViewAngle();

    }


    /**
     * a binary random variable (0,1) with Mx,y:{free, occupied} -> {0,1}https://www.youtube.com/watch?v=Ko7SWZQIawM
     * Given some probability space (theta, P) a R.V. X: theta -> R is a function that maps the sample space to the reals.
     */
    public void Occupancy() {

    }

    /**
     * Probability that an unknown space is occupied given the total count of occupied spaces
     * @param occCount the count of true occupied spaces percieved by the agent.
     * @param size of exploration space
     * @return the probability that a grid space is occupied
     */
    public double occProbability(int occCount, int size) {
        return occCount/size;
    }

    /**
     * Probability that an unknown space is occupied given the total count of occupied spaces
     * @param occCount the count of true occupied spaces percieved by the agent.
     * @return the probability that a grid space is occupied
     */
    //This is if we can use the real input size but I can't work it out yet.
    public double occProbability(int occCount) {
        return occCount/viewRays;
    }


    /**
     * Assumes that the environment is normally distributed
     * This is the random version occProbability() which does not take into account what the agent knows.
     * @return the probability that environment is occupied
     */
    public double probabililityAGauss() {
        return rng.nextGaussian();
    }

    /**
     * This is the backup strategy of the agent should it still not be able to find the intruder.
     * The basic idea is to have an even larger OccupancyGrid to fine-tune the calculation.
     * fine-grained grid map where an occupancy variable associated with each cell.
     * i.e. it is just an array of probability using Occupancy() Mx,y
     * Requires Bayesian filtering to maintain a occupancy grid map.
     *  Recursively update p(Mx,My) for each cell
     */
    public void noCigar() {
        int newXsize = occupancyGrid.occupancyGrid.length * 2;
        int newYsize = occupancyGrid.occupancyGrid[0].length * 2;

        boolean[][] newOccGrid = new boolean[newXsize][newYsize];
        double[][] newLogMap = new double[newXsize][newYsize];

        xPosition = Math.round(newOccGrid.length / 2);
        yPosition = Math.round(newOccGrid[0].length / 2);

        //reinitialize calculate as old
    }

    /**
     * Instead of keeping track of all probabilities we keep track of odd of a cell being occupied.
     * Odd= (X happens) / (X not happen) = P(X)/P(!X)
     * So then calculation for Baye's is:
     * 1. Odd((Mx,y =1) given z) = P(Mx,y = 1 | z) / P(Mx,y = 1 | z)
     * 2. P(Mx,y=0|z) = P(z|Mx,y = 0)P(Mx,y = 0)/ P(z)
     * @param count count of occupied space value counted by the agent
     * @return the odds of occupied over empty
     */
    public double odd(int count, int size){
        return occProbability(count, size)/(1-occProbability(count, size));
    }

    /**
     * Makes computation even simpler if we we take Math.log(odds)
     * log((P(Mx,y=1|z)/P(Mx,y=0|z))=log((P(Mx,y=1|z)*P(Mx,y=1)/(P(Mx,y=0|z)*P(Mx,y=0)))
     * @param meas is P(z|Mx,y=1)|P(z|Mx,y=0) derived from the above equation
     * @param odd is the odd of occuped over empty
     * @return the log-update rule.
     */
    public double logOdds(double meas, double odd){
        //actual update rule is log odd += log odd meas but this is close enough.
        return Math.log(meas) + Math.log(odd);
    }

    //localize global map into our arraylist.
    /**
     * Using the formula:
     * [x1,occ; x2,occ] = [cos(thetha) , sin(theta); -sin(thetha) , cos(theta)] * [d ; 0] + [x1 ; x2]
     * where d is length of ray, (x1,occ) and (x2,occ) are the x and y coordinate of the endpoint of the rays.
     * theta is the direction the agent is facing
     * Note, this method discretizes the map.
     */
    public void mapping(GuardPercepts percepts, ObjectPercept objectPercept, ObjectPercepts objectPercepts) {
        //objectPercepts.getAll();
        //TODO: not the way to get angle
        Angle direction = percepts.getVision().getFieldOfView().getViewAngle();
        Distance distance = percepts.getVision().getFieldOfView().getRange();
        double[][] transformMatrix = {{Math.cos(direction.getDegrees()), Math.sin(direction.getDegrees())},
                {-Math.sin(direction.getDegrees()), Math.cos(direction.getDegrees())}};
        double[] normalizeCoefficient = {distance.getValue(), 0};

        //check the walls.  Is there no faster way of doing this?

        //Bresenham line algorithm: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
        //This draws the line from agent location to endpoint of RayCast. O(n)

        //TODO: this is for one endpoint find a way to do this for all endpoints: ObjectPercepts
        int x1 = (int) xPosition;
        int y1 = (int) yPosition;
        int x2 = (int) objectPercept.getPoint().getX();
        int y2 = (int) objectPercept.getPoint().getY();

        int m_new = 2 * (y2 - y1);
        int slope_error_new = m_new - (x2 - x1);


        // Boolean occupancy.
        for (int x = x1, y = y1; x <= x2; x++) {
            if (x == x2 && y == y2) {

                //set only last value to true
                occupancyGrid.update(x, y);
                break;
            } else {
                occupancyGrid.update(x, y, false);

                // Add slope to increment angle formed
                slope_error_new += m_new;

                // Slope error reached limit, time to
                // increment y and update slope error.
                if (slope_error_new >= 0) {
                    y++;
                    slope_error_new -= 2 * (x2 - x1);
                }
            }
        }

        x1 = (int) xPosition;
        y1 = (int) yPosition;

        // log update: the belief that something is indeed unoccupied should increase even further.
        for (int x = x1, y = y1; x <= x2; x++) {
            //check if value is true or false.
            if (occupancyGrid.occupancyGrid[x][y]) {
                occupancyGrid.logUpdate(x, y, log_occ + occupancyGrid.getLogValue(x, y));
            } else {
                occupancyGrid.logUpdate(x, y, log_free + occupancyGrid.getLogValue(x, y));
            }
        }

        int explorationSize = (int) (distance.getValue());

        //define the exploration zone.


        // NW case
        int[] rowCount = new int[explorationSize];
        int[] colCount = new int[explorationSize];
        if (x2 > x1 && y2 > y1) {

            //value for logOdds(,odds)
            int countTrue = 0;
            //this counts everything
            for (int i = y1 - explorationSize; i < y1; i++) {
                for (int j = x1; j < x1 + explorationSize; j++) {
                    if (occupancyGrid.occupancyGrid[j][i]) {
                        countTrue++;
                    }
                }
            }
        }


//            //this assumes that walls only horizontally - decouples independence maybe be more accurate
//            int rowCountIndex = 0;
//            //row count
//            for(int i  = y1-explorationSize; i < y1; i++) {
//                int countTrueRow =0;
//                for(int j = x1; j < x1 + explorationSize; j++) {
//                    //check if value is true.
//                    if(occupancyGrid.occupancyGrid[j][i]) {
//                        countTrueRow++;
//                    }
//
//                    if(j == x1 + explorationSize - 1) {
//                        //record the count true values in row
//                        rowCount[rowCountIndex] = countTrueRow;
//                        rowCountIndex++;
//                    }
//                }
//            }
//
//        }
//        // SW case
//        else if(x2 > x1 && y2 < y1) {
//
//        }
//        // SE case
//        else if(x2 < x1 && y2 < y1) {
//
//        }
//        // NE case
//        else if(x2 < x1 && y2 > y1) {
//
//        }
//        //Agent is facing the endpoint
//        else {
//
//        }
    }


}
