package Group5.GameController;

import Interop.Geometry.Angle;
import Interop.Geometry.Point;

public class IntruderController extends AgentController {


    private Point position;
    private double radius;
    //the direction an agent is walking
    private Vector2D direction;
    private Angle angle;

    protected IntruderController(Point position, double radius) {
        super(position, radius);
    }

}
