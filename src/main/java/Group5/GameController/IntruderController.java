package Group5.GameController;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;

public class IntruderController extends AgentController {


    private Point position;
    private double radius;
    //the direction an agent is walking
    private Vector2D direction;
    private  Angle angle;

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

}
