package Group7.agent.Intruder;

import Interop.Geometry.Direction;
import Interop.Geometry.Point;

/*Stores info relative to a specific agent.
Meant to be used by the agent classes.
* */
public class AgentState {
    private Point pos;
    private Direction angle; //stores the angle of view of the agent

    public AgentState(Point pos, Direction angle) {
        this.pos = pos;
        this.angle = angle;
    }

    public AgentState(Direction angle){
        this.pos = new Point(0,0);
        this.angle = angle;
    }

    /*The pos data type is a point of doubles. We can either get this double (real) form or the standard rounded integer form
     * */
    public Point getRealPos() {
        return pos;
    }

    public Point getPos() {
        return new Point(Math.round(pos.getX()),Math.round(pos.getY()));
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public void setPos(double x, double y) {
        this.pos = new Point(x,y);
    }

    public int getX(){
        return (int)Math.round(pos.getX());
    }

    public int getY(){
        return (int)Math.round(pos.getY());
    }

    public double getRealX(){
        return pos.getX();
    }

    public double getRealY(){
        return pos.getY();
    }

    public Direction getAngle() {
        return angle;
    }

    public Vector vectorPos(){
        return new Vector(pos);
    }
    public void setAngle(Direction angle) {
        this.angle = angle;
    }
}
