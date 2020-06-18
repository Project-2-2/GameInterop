package Group8.PathFinding;

import Group8.Agents.OccupancyGrid;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * This agent is implemented using the A* algorithm
 * It specifically uses the occupancy grid as memory map
 */
public class AStar {

    private final Angle MAX_ROTATION;
    private OccupancyGrid occupancyGrid;
    double xPosition, yPosition;
    private Point initialLocation;
    //Log-probabilities to add or remove from the map
    private double log_occ = Math.log(0.65/0.35);
    private double log_free = Math.log(0.35/0.65);
    Distance rangeDistance;
    private boolean[][] openSet, closedSet;
    private int counter =0;
    private boolean circumnav = false;


    public AStar(IntruderPercepts percepts) {
        this.occupancyGrid = new OccupancyGrid();
        xPosition = occupancyGrid.occupancyGrid.length/2.0;
        yPosition = occupancyGrid.occupancyGrid[0].length/2.0;
        initialLocation = new Point(xPosition,yPosition);
        MAX_ROTATION = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
        System.out.println(MAX_ROTATION.getDegrees());
        rangeDistance = percepts.getVision().getFieldOfView().getRange();
        closedSet = this.occupancyGrid.occupancyGrid;
    }



    public IntruderAction getMoveIntruder(IntruderPercepts percepts) {
        mapping(percepts);

        //Create empty set and close set
        openSet = this.occupancyGrid.occupancyGrid;

        // There are still nodes which have to be visited
        //if(!checkEnd(openSet)){
            // Find the node with the min/ max log value ?
            // TODO
        //    int i = 0;
        //    int j = 0;
            //
        //    openSet[i][j] = false;
        //    closedSet[i][j] = true;

            // Inspects the neighbours of the node
         //   Point[] neighbours = new Point[10];

        //}
        // All the nodes have been visited

        // BASE CASE
        if(!percepts.wasLastActionExecuted()){
            circumnav = true;
            return new Rotate(Angle.fromRadians(MAX_ROTATION.getRadians()));
        }
        if(circumnav){
            return new Move(new Distance(0.4));
        }

        if (counter%13 == 0) {
            counter++;
            if (percepts.getTargetDirection().getRadians() >= MAX_ROTATION.getRadians()) {
                return new Rotate(Angle.fromRadians(MAX_ROTATION.getRadians()));
            } else {
                return new Rotate(Angle.fromRadians(percepts.getTargetDirection().getRadians() + 0.0001));
            }
        }

        if(((Math.abs(percepts.getTargetDirection().getRadians())) <= 0.001)){
            return new Move(new Distance(0.4));
        }
        else if(percepts.getTargetDirection().getRadians() >= MAX_ROTATION.getRadians()){
            return new Rotate(Angle.fromRadians(MAX_ROTATION.getRadians()));
        }
        else{
            return new Rotate(Angle.fromRadians(percepts.getTargetDirection().getRadians()+0.0001));
        }

    }

    /**
     * This method checks if all the occupancy grid has been investigated
     * @param openSet the occupancy grid
     * @return false if there are still unvisited spots in the grid
     */
    private boolean checkEnd(boolean[][] openSet) {

        for(int i = 0 ; i < openSet.length ; i++){
            for(int j = 0 ; j < openSet[0].length; j++){
                // If there is at least one false
                if(openSet[i][j] == false){
                    return false;
                }
            }
        }
        return true;
    }


    public double occProbability(int occCount, int size) {
        return occCount/size;
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

    /**
     * a binary random variable (0,1) with Mx,y:{free, occupied} -> {0,1} as taken from: https://www.youtube.com/watch?v=Ko7SWZQIawM
     * Given some probability space (theta, P) a R.V. X: theta -> R is a function that maps the sample space to the reals.
     * Using the formula:
     * [x1,occ; x2,occ] = [cos(thetha) , sin(theta); -sin(thetha) , cos(theta)] * [d ; 0] + [x1 ; x2]
     * where d is length of ray, (x1,occ) and (x2,occ) are the x and y coordinate of the endpoint of the rays.
     * theta is the direction the agent is facing
     * Note, this method discretizes the map.
     */
    public void mapping(IntruderPercepts percepts) {
        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();
        Iterator<ObjectPercept> objectPerceptIterator = objectPercepts.iterator();
        //TODO: not the way to get angle


        //I don't understand how I am supposed to use this formula in Java.  This is something Matlab can do.
//        double[][] transformMatrix = {{Math.cos(direction.getDegrees()), Math.sin(direction.getDegrees())},
//                {-Math.sin(direction.getDegrees()), Math.cos(direction.getDegrees())}};
//        double[] normalizeCoefficient = {distance.getValue(), 0};

        while (objectPerceptIterator.hasNext()) {
            ObjectPercept objectPercept = objectPerceptIterator.next();
            //Bresenham line algorithm: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
            //This draws the line from agent location to endpoint of RayCast. O(n)
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

            int explorationSize = (int) (rangeDistance.getValue());

            //define the exploration zone.

            // NW case
            int countTrue = 0;
            if (x2 > x1 && y2 < y1) {
                //this counts everything
                for (int i = y1 - explorationSize; i < y1; i++) {
                    for (int j = x1; j < x1 + explorationSize; j++) {
                        if (!occupancyGrid.occupancyGrid[j][i]) {
                            continue;
                        } else {
                            countTrue++;
                        }
                    }
                }
            }
            // SW case
            else if (x2 > x1 && y2 > y1) {
                for (int i = y1; i < y1 + explorationSize; i++) {
                    for (int j = x1; j < x1 + explorationSize; j++) {
                        if (!occupancyGrid.occupancyGrid[j][i]) {
                            continue;
                        } else {
                            countTrue++;
                        }
                    }
                }
            }
            // SE case
            else if (x2 < x1 && y2 > y1) {
                for (int i = y1; i < y1 + explorationSize; i++) {
                    for (int j = x1 - explorationSize; j < x1; j++) {
                        if (!occupancyGrid.occupancyGrid[j][i]) {
                            continue;
                        } else {
                            countTrue++;
                        }
                    }
                }
            }
            // NE case
            else if (x2 < x1 && y2 < y1) {
                for (int i = y1 - explorationSize; i < y1; i++) {
                    for (int j = x1 - explorationSize; j < x1; j++) {
                        if (!occupancyGrid.occupancyGrid[j][i]) {
                            continue;
                        } else {
                            countTrue++;
                        }
                    }
                }
            }
            //Agent is facing the endpoint
            else {
                //TODO: rotate the other direction.
                System.out.println("Agent is staring at a corner.  You need to add a case if agent is staring at a endpoint.");
            }

            //countTrue not updating?
            double odds = odd(countTrue, explorationSize * explorationSize);

            //update log map
            // NW
            if (x2 > x1 && y2 < y1) {
                //this counts everything
                for (int i = y1 - explorationSize; i < y1; i++) {
                    for (int j = x1; j < x1 + explorationSize; j++) {
                        if (occupancyGrid.logMap[j][i] == 0) {
                            //at top left
                            if (i == 0 && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top right
                            else if (i == 0 && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom left
                            else if (i == occupancyGrid.logMap[0].length && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom right
                            else if (i == occupancyGrid.logMap[0].length && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top but not corner
                            else if (i == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom but not corner
                            else if (i == occupancyGrid.logMap[0].length) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at left but not corner
                            else if (j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at right but not corner
                            else if (j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //if not at corner
                            else {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                        } else {
                            double logValue = logOdds(occupancyGrid.logMap[j][i], odds);
                            occupancyGrid.logUpdate(j, i, logValue);
                        }
                    }
                }
            }
            // SW
            else if (x2 > x1 && y2 > y1) {
                //this counts everything
                for (int i = y1; i < y1 + explorationSize; i++) {
                    for (int j = x1; j < x1 + explorationSize; j++) {
                        if (occupancyGrid.logMap[j][i] == 0) {
                            //at top left
                            if (i == 0 && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top right
                            else if (i == 0 && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom left
                            else if (i == occupancyGrid.logMap[0].length && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom right
                            else if (i == occupancyGrid.logMap[0].length && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top but not corner
                            else if (i == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom but not corner
                            else if (i == occupancyGrid.logMap[0].length) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at left but not corner
                            else if (j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at right but not corner
                            else if (j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //if not at corner
                            else {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                        } else {
                            double logValue = logOdds(occupancyGrid.logMap[j][i], odds);
                            occupancyGrid.logUpdate(j, i, logValue);
                        }
                    }
                }
            }
            // SE
            else if (x2 < x1 && y2 > y1) {
                //this counts everything
                for (int i = y1; i < y1 + explorationSize; i++) {
                    for (int j = x1 - explorationSize; j < x1; j++) {
                        if (occupancyGrid.logMap[j][i] == 0) {
                            //at top left
                            if (i == 0 && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top right
                            else if (i == 0 && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom left
                            else if (i == occupancyGrid.logMap[0].length && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom right
                            else if (i == occupancyGrid.logMap[0].length && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top but not corner
                            else if (i == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom but not corner
                            else if (i == occupancyGrid.logMap[0].length) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at left but not corner
                            else if (j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at right but not corner
                            else if (j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //if not at corner
                            else {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                        } else {
                            double logValue = logOdds(occupancyGrid.logMap[j][i], odds);
                            occupancyGrid.logUpdate(j, i, logValue);
                        }
                    }
                }
            }
            // NE
            else if (x2 < x1 && y2 < y1) {
                //this counts everything
                for (int i = y1; i < y1 + explorationSize; i++) {
                    for (int j = x1 - explorationSize; j < x1; j++) {
                        if (occupancyGrid.logMap[j][i] == 0) {
                            //at top left
                            if (i == 0 && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top right
                            else if (i == 0 && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom left
                            else if (i == occupancyGrid.logMap[0].length && j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom right
                            else if (i == occupancyGrid.logMap[0].length && j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at top but not corner
                            else if (i == 0) {
                                double adjValues = occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j - 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at bottom but not corner
                            else if (i == occupancyGrid.logMap[0].length) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at left but not corner
                            else if (j == 0) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j + 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //at right but not corner
                            else if (j == occupancyGrid.logMap.length - 1) {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                            //if not at corner
                            else {
                                double adjValues = occupancyGrid.logMap[j][i - 1] + occupancyGrid.logMap[j - 1][i] + occupancyGrid.logMap[j][i + 1] + occupancyGrid.logMap[j + 1][i];
                                if (adjValues == 0) {
                                    occupancyGrid.logUpdate(j, i, log_free);
                                } else {
                                    occupancyGrid.logUpdate(j, i, logOdds(adjValues, odds));
                                }
                            }
                        } else {
                            double logValue = logOdds(occupancyGrid.logMap[j][i], odds);
                            occupancyGrid.logUpdate(j, i, logValue);
                        }
                    }
                }
            } else {
                System.out.println("you need to get a N,S,E,W case for log map updating.");
            }
        }
    }

}
