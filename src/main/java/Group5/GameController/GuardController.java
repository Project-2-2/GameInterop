package Group5.GameController;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;

public class GuardController extends AgentController {


    private Point position;
    private double radius;
    //the direction an agent is walking
    private Vector2D direction;
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

    /**
     * call this method as an agent if you want to do a movement that includes opening a door
     * you don't have to call the normal move method after this
     * @param distance
     */
    public void openDoor(Distance distance){
        super.openDoor(distance,normalMoveDistance);
    }


    /**
     * call this method as an agent if you want to do a movement that includes opening a window
     * you don't have to call the normal move method after this
     * @param distance
     */
    public void openWindow(Distance distance){
        super.openWindow(distance,normalMoveDistance);
    }

    /**
     * call this method as an agent if you want to do a movement that includes entering a sentry
     * you don't have to call the normal move method after this
     * @param distance
     */
    public void enterSentry(Distance distance){
        super.enterSentry(distance,normalMoveDistance);
    }

}
