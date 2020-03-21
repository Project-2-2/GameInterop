package Group5.GameController;

import Interop.Action.DropPheromone;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Action.Yell;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;

public class GuardController extends AgentController {


    private Point position;
    private double radius;
    //the direction an agent is walking
    private Point direction;
    private Angle angle;

    private Distance normalMoveDistance;

    private double maxAngleRotation;

    protected GuardController(Point position, double radius, double maxAngleRotation, double moveDistance) {
        super(position, radius, maxAngleRotation);
        normalMoveDistance = new Distance(moveDistance);
    }


    public void move(Distance distance){
        super.move(distance,normalMoveDistance);
    }

    public void move(Move move){ super.move(move.getDistance(),normalMoveDistance);}


    /**
     * first rotates the moves
     * @param move
     * @param rotate
     */
    public void moveWithRotation(Move move, Rotate rotate){
        super.rotate(rotate.getAngle());
        super.move(move.getDistance(),normalMoveDistance);
    }





    //TODO  when sound is implemented
    public void yell(Yell yell){

    }

    //TODO smell has to be implemented
    public void dropPheromone(DropPheromone dropPheromone){

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
