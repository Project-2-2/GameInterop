package Group11.Agents;

import Group11.Point;
import Group11.Segment;
import Interop.Geometry.Angle;


public class Agent {
    private Point location;
    private Angle direction;
    private Segment vision;
    private boolean lastActionExecuted = true;

    public Agent(Point location, Angle angle){
        this.location = location;
        this.direction = angle;
        this.vision = new Segment(location, new Point(location.getX()+1, location.getY()+1));
    }
    public Agent(){
        this.location = new Point(0,0);
        this.direction = Angle.fromRadians(0);
    }
    public Point getLocation(){
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void moveForward(double distance) {
        location.setX((-Math.cos(direction.getRadians())*distance)+location.getX());
        location.setY((Math.sin(direction.getRadians())*distance)+location.getY());
        vision.setA(new Point((-Math.sin(direction.getRadians())*distance)+location.getX(), (Math.cos(direction.getRadians()))*distance+location.getY()));
        vision.setB(new Point((-Math.sin(direction.getRadians())*5)+location.getX(), (Math.cos(direction.getRadians())*5)+location.getY()));
    }
    public Point getMovingToPoint( double distance){
        return  new Point((-Math.cos(direction.getRadians())*distance)+location.getX(),(Math.sin(direction.getRadians())*distance)+location.getY());
    }
    public void rotateBy(Angle angle){
        double radOld = direction.getRadians();
        double radNew = angle.getRadians();
        if((radOld+radNew)>=2*Math.PI){
            direction = Angle.fromRadians(radOld+radNew-2*Math.PI);
        }else if((radOld+radNew)<0){
            direction = Angle.fromRadians(2*Math.PI-(radNew+radOld));
        }
        else{
            direction = Angle.fromRadians(radOld+radNew);
        }
    }
    public Angle getAngle(){
        return direction;
    }
    public Segment getVision(){ return vision;}

    public boolean isLastActionExecuted() {
        return lastActionExecuted;
    }

    public void setLastActionExecuted(boolean lastActionExecuted) {
        this.lastActionExecuted = lastActionExecuted;
    }
}

