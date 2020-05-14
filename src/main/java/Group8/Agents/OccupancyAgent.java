package Group8.Agents;

import Group9.Game;
import Interop.Action.DropPheromone;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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

    //For optimality sake OccupancyGrid().update() would determine whether log likelihood is 0.5 is (<,>).
    //I am leaving it incase we want to do some fancy data analytics.
    //private ArrayList<ArrayList<Double>> log_prob_map; //initially set to zero

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


    private int suroundUpdateIteration = 1;

    public OccupancyAgent() {
        this.occupancyGrid = new OccupancyGrid();
        double xPosition = occupancyGrid.occupancyGrid.size()/2.0;
        double yPosition = occupancyGrid.occupancyGrid.size()/2.0;
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

        //operations

        //record
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

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if(percepts.wasLastActionExecuted()) {
            if(suroundUpdateIteration == 1) {
                //update rotation
                suroundUpdateIteration ++;

                //get range() tells us how far to update the OccupancyGrid
                percepts.getVision().getFieldOfView().getRange();
                //percepts.getViewAngle() tells which direction of the grid to update on.
                percepts.getVision().getFieldOfView().getViewAngle();


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


    public void conditionalProbabilities() {
        //recall: P(A^c|B) = 1 - P(A | B)
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
     * @param totalRay the total count of ray count and space we want to compute.
     * @return the probability that a grid space is occupied
     */
    public double occProbability(int occCount, int totalRay) {
        return occCount/totalRay;
    }

    /**
     * Probability that an unknown space is occupied given the total count of occupied spaces
     * @param occCount the count of true occupied spaces percieved by the agent.
     * @return the probability that a grid space is occupied
     */
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
        double xSize, ysize = occupancyGrid.occupancyGrid.size() * 2;

    }
}
