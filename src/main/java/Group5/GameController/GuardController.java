package Group5.GameController;

import Interop.Geometry.Angle;
import Interop.Geometry.Point;

public class GuardController extends AgentController {


    private Point position;
    private double radius;
    //the direction an agent is walking
    private Vector2D direction;
    private Angle angle;

    protected GuardController(Point position, double radius) {
        super(position, radius);
    }
}
