package Group5.GameController;


import Interop.Geometry.Angle;
import Interop.Geometry.Point;

import java.util.ArrayList;

/**
 * this class checks for every agent which movements it can do
 */
public class AgentController {

    private Point position;
    private double radius;
    //the direction an agent is walking
    private Vector2D direction;
    private  Angle angle;

    protected AgentController(Point position, double radius){
        this.position = position;
        this.radius = radius;
        direction = new Vector2D(position);
        angle = position.getClockDirection();

    }

    protected Point getPosition(){
        return position;
    }


    public void rotate(Angle angle){
        double vectorLength = direction.length();
        double x= vectorLength*Math.cos(angle.getRadians());
        double y = vectorLength*Math.sin(angle.getRadians());
        direction = new Vector2D(x,y);
    }



}
