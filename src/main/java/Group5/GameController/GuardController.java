package Group5.GameController;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;

public class GuardController extends AgentController {


    private Point position;
    private double radius;
    //the direction an agent is walking
    private Vector2D direction;
    private  Angle angle;

    private Distance normalMoveDistance;

    private double maxAngleRotation;

    protected GuardController(Point position, double radius, double maxAngleRotation, double moveDistance) {
        super(position, radius, maxAngleRotation);
        normalMoveDistance = new Distance(moveDistance);
    }
}
