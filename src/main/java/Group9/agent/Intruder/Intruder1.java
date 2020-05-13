
//What the triangulation method has to return
//What class or method will call the Intrude1's triangulation method

package Group9.agent.Intruder;

import Group9.agent.container.IntruderContainer;
import Group9.math.Vector2;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.LinkedList;
public class Intruder1 implements Intruder {
    Cell position;
    History history = new History();
    LinkedList<Cell> toBeProcessed = new LinkedList<>();

    public Intruder1()
    {
        position = new Cell(0, 0);
        Cell above = position.addAbove();
        Cell below = position.addBelow();
        Cell left = position.addLeft();
        Cell right = position.addRight();
    }
    /*
    //Attribute
    Cell position;

    //Constructor
    public Intruder1(Cell startPosition)
    {
        this.position = startPosition;
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

        double my_x = this.position.getX();
        double my_y = this.position.getY();

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
        ViewArea view = new ViewArea(range, alpha, direction, position.getX(), position.getY());
        toBeProcessed.add(position);
        explore(view);
        double moveDistance = percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue();
        Coordinate p = new Coordinate(position.getX(), position.getY());
        LinkedList<Coordinate> borderPoints = getBorderPoints(p, moveDistance);
        Cell target = findTarget(position, borderPoints);
        double targetDirection = getDirection(target, position);
        if (targetDirection != alpha.getRadians())
        {
            return new Rotate(Angle.fromRadians(targetDirection - alpha.getRadians()));
        }
        else
        {
            double distance = getDistance(new Coordinate(target.getMidX(), target.getMidY()), new Coordinate(position.getMidX(), position.getMidY()));
            position = target;
            return new Move(new Distance(distance));
        }
    }
    public double getDirection(Cell target, Cell position)
    {
        Vector2 v = new Vector2(target.getMidX() - position.getX(), target.getMidY() - position.getMidY());
        v = v.normalise();
        double angle = v.getAngle();
        return angle;
    }
    public Cell findTarget(Cell position, LinkedList<Coordinate> borderPoints)
    {
        double highest = Double.NEGATIVE_INFINITY;
        Cell target = new Cell();
        for (Coordinate point: borderPoints)
        {
            Cell possibleTarget = position.find(point.getX(), point.getY());
            double score = possibleTarget.getScore();
            if (score > highest)
            {
                highest = score;
                target = possibleTarget;
            }
        }
        return target;
    }

    public void explore(ViewArea view)
    {
        toBeProcessed.forEach(p -> p.setProcessed(false));
        while(toBeProcessed.size() > 0)
        {
            if (view.partContained(toBeProcessed.get(0)) > 0.0)
            {
                toBeProcessed.addAll(toBeProcessed.get(0).getUnprocessed());
            }
            toBeProcessed.remove(0);
        }
    }
    public LinkedList<Coordinate> getBorderPoints(Coordinate position, double moveDistance)
    {
        LinkedList<Coordinate> borderPoints = new LinkedList<>();
        LinkedList<Coordinate> inBetweenPoints = new LinkedList<>();

        boolean done = false;
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
            borderPoints.add(new Coordinate(position.getX() + c.getX(), position.getY() + c.getY()));
            borderPoints.add(new Coordinate(position.getX() - c.getX(), position.getY() + c.getY()));
            borderPoints.add(new Coordinate(position.getX() + c.getX(), position.getY() - c.getY()));
            borderPoints.add(new Coordinate(position.getX() - c.getX(), position.getY() - c.getY()));
        }

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
}
