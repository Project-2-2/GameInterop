
//What the triangulation method has to return
//What class or method will call the Intrude1's triangulation method

package Group9.agent.Intruder;

import Group9.math.Vector2;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.LinkedList;
public class Intruder1 implements Intruder {
    private Cell start;
    private History history = new History();
    private LinkedList<Cell> toBeProcessed = new LinkedList<>();
    private Cell target;
    private Coordinate position;
    private boolean firstTime;
    private double lookingDirection;

    public Intruder1()
    {
        start = new Cell(0, 0);
        position = new Coordinate(0,0);
        firstTime = true;
        lookingDirection = 0;
    }
    /*
    //Attribute
    Cell start;

    //Constructor
    public Intruder1(Cell startPosition)
    {
        this.start = startPosition;
    }
    */


    //////////////////////////////
    //Main
    /*
    Cell target = new Cell(); Object address of memory
    target.setX(3.57688);
    target.setY(3.50009);

    Intruder1 I1 = new Intruder1();

    I1.triangulation(target);   target --> address of Cell Object
    //from here on the object is in target
     */
    ////////////////////////////////

    /*
    //Triangulation method
    public double triangulation(Cell target_coords) { //target_coords --> Cell Object same of target
        //from here on the object is in target_coords
        double target_x = target_coords.getX();
        double target_y = target_coords.getY();

        double my_x = this.start.getX();
        double my_y = this.start.getY();

        double distance_x = Math.abs(target_x - my_x);
        double distance_y = Math.abs(target_y - my_y);
        double distance_to_target = Math.sqrt(distance_x*distance_x + distance_y*distance_y);
        return distance_to_target;
    }
    */


    //Methods
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        //building the "mind-map" of the building
        Direction direction = percepts.getTargetDirection();
        Angle alpha = percepts.getVision().getFieldOfView().getViewAngle();
        Distance range = percepts.getVision().getFieldOfView().getRange();
        ViewArea view = new ViewArea(range, alpha, lookingDirection, position.getX(), position.getY());
        toBeProcessed.add(start);
        history.setUnProcessed();
        explore(view, percepts.getVision().getObjects());
        //Making a decision based on the mind-map
        if(firstTime || closeEnough(position.getX(), target.getMidX()) && closeEnough(position.getY(), target.getMidY()))
        {
            this.firstTime = false;
            //determine a new target
            double moveDistance = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue();
            LinkedList<Coordinate> borderPoints = getBorderPoints(position, moveDistance);
            this.target = findTarget(borderPoints);
        }
        double targetDirection = getDirection(target, start);
        if (targetDirection != lookingDirection)
        {
            double turn = targetDirection - lookingDirection;
            double maxTurn = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
            if (turn > maxTurn)
            {
                turn = maxTurn;
            }
            changeLookingDirection(turn);
            return new Rotate(Angle.fromRadians(turn));
        }
        else
        {
            double distance = getDistance(new Coordinate(target.getMidX(), target.getMidY()), new Coordinate(position.getX(), position.getY()));
            //update position
            position.add(Math.cos(lookingDirection) * distance, Math.sin(lookingDirection) * distance);
            return new Move(new Distance(distance));
        }
    }
    public double getDirection(Cell target, Cell position)
    {
        Vector2 v = new Vector2(target.getMidX() - position.getX(), target.getMidY() - position.getMidY());
        v = v.normalise();
        return v.getAngle();
    }
    public Cell findTarget(LinkedList<Coordinate> borderPoints)
    {
        double highest = Double.NEGATIVE_INFINITY;
        Cell target = new Cell();
        for (Coordinate point: borderPoints)
        {
            Cell possibleTarget = history.getCell(new Coordinate(point.getX(), point.getY()));
            if(possibleTarget != null)
            {
                double score = possibleTarget.getScore();
                if (score > highest)
                {
                    highest = score;
                    target = possibleTarget;
                }
            }
        }
        return target;
    }

    public void explore(ViewArea view, ObjectPercepts objects)
    {
        while(toBeProcessed.size() > 0)
        {
            if (toBeProcessed.get(0) != null)
            {
                double contained = view.partContained(toBeProcessed.get(0), objects);
                if (contained > 0.0)
                {
                    LinkedList<Cell> unprocessed = toBeProcessed.get(0).getUnprocessed(history);
                    history.addAll(unprocessed);
                    toBeProcessed.addAll(unprocessed);

                }
            }
            toBeProcessed.remove(0);
        }
    }
    public LinkedList<Coordinate> getBorderPoints(Coordinate position, double moveDistance)
    {
        LinkedList<Coordinate> borderPoints = new LinkedList<>();
        borderPoints.add(new Coordinate(position.getX() + 1, position.getY() + 1));
        borderPoints.add(new Coordinate(position.getX() + 1, position.getY()));
        borderPoints.add(new Coordinate(position.getX() + 1, position.getY() -1));
        borderPoints.add(new Coordinate(position.getX(), position.getY() -1));
        borderPoints.add(new Coordinate(position.getX() -1, position.getY() -1));
        borderPoints.add(new Coordinate(position.getX() -1, position.getY()));
        borderPoints.add(new Coordinate(position.getX()-1, position.getY() + 1));
        borderPoints.add(new Coordinate(position.getX(), position.getY() + 1));
        /*
        LinkedList<Coordinate> borderPoints = new LinkedList<>();
        LinkedList<Coordinate> inBetweenPoints = new LinkedList<>();

        for(int i=0; i<Math.floor(moveDistance); i++)
        {

            double x1 = - Math.floor(moveDistance);
            double y1 = 0;

            y1++;
            Coordinate c1 = new Coordinate(Math.ceil(-moveDistance * Math.cos(Math.asin(y1 / moveDistance))), y1);

            x1++;
            Coordinate c2 = new Coordinate(x1, Math.floor(moveDistance * Math.sin(Math.acos(x1 / moveDistance))));

            if(!contains(inBetweenPoints, c1))
            {
                inBetweenPoints.add(c1);
            }
            if (!contains(inBetweenPoints, c2))
            {
                inBetweenPoints.add(c2);
            }
        }

        for (Coordinate c: inBetweenPoints)
        {
            borderPoints.add(new Coordinate(start.getX() + c.getX(), start.getY() + c.getY()));
            borderPoints.add(new Coordinate(start.getX() - c.getX(), start.getY() + c.getY()));
            borderPoints.add(new Coordinate(start.getX() + c.getX(), start.getY() - c.getY()));
            borderPoints.add(new Coordinate(start.getX() - c.getX(), start.getY() - c.getY()));
        }

         */
        return borderPoints;
    }
    public boolean contains(LinkedList<Coordinate> list, Coordinate c)
    {
        for (Coordinate e : list)
        {
            if (e.equals(c))
            {
                return true;
            }
        }
        return false;
    }
    public double getDistance(Coordinate c1, Coordinate c2)
    {
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2));
    }
    private boolean closeEnough(double d1, double d2)
    {
        double slack = 1e-3;
        if(Math.abs(d1 - d2) < slack)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void changeLookingDirection(double angle)
    {
        lookingDirection += angle;
        lookingDirection = lookingDirection % (2*Math.PI);
    }
}
