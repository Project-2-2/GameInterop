package Group5.GameController;

import Interop.Action.DropPheromone;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Action.Sprint;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;

public class IntruderController extends AgentController {


    private Point position;
    private double radius;
    //the direction an agent is walking
    private Point direction;
    private Angle angle;

    private Distance normalMoveDistance;
    private Distance sprintDistance;

    private double maxAngleRotation;


    protected IntruderController(Point position, double radius, double moveDistance, double sprintDistance, double maxAngleRotation) {
        super(position, radius, maxAngleRotation);
        normalMoveDistance = new Distance(moveDistance);
        this.sprintDistance = new Distance(sprintDistance);
    }


    public void move(Distance distance){
        super.move(distance,normalMoveDistance);
    }


    public void move(Move move){ super.move(move.getDistance(),normalMoveDistance);}


    /**
     * first rotates then moves
     * @param move
     * @param rotate
     */
    public void moveWithRotation(Move move, Rotate rotate){
        super.rotate(rotate.getAngle());
        super.move(move.getDistance(),normalMoveDistance);
    }

    public void sprint(Sprint sprint){
        if(sprint.getDistance().getValue()>sprintDistance.getValue()){
            return;
        }

        double newX= position.getX()+sprint.getDistance().getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+sprint.getDistance().getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        if(GameRunner.moveValidility(position,newPosition)){
            position = newPosition;
        }



    }

    //TODO smell has to be implemented
    public void dropPheromone(DropPheromone dropPheromone){

    }


    //TODO IMPLEMENT COOLDOWN
    public void sprint(Distance distance){
        if (distance.getValue()>sprintDistance.getValue()){
            return;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        if(GameRunner.moveValidility(position,newPosition)){
            position = newPosition;
        }
    }

    /**
     * call this method as an agent if you want to do a movement that includes opening a door
     * you don't have to call the normal move method after this
     * @param move distance of the movement
     */
    public void openDoor(Move move){
        super.openDoor(move.getDistance(),normalMoveDistance);
    }


    /**
     * call this method as an agent if you want to do a movement that includes opening a window
     * you don't have to call the normal move method after this
     * @param move distance of the movement
     */
    public void openWindow(Move move){
        super.openWindow(move.getDistance(),normalMoveDistance);
    }

    /**
     * call this method as an agent if you want to do a movement that includes entering a sentry
     * you don't have to call the normal move method after this
     * @param move distance of the movement
     */
    public void enterSentry(Move move){
        super.enterSentry(move.getDistance(),normalMoveDistance);
    }

}
