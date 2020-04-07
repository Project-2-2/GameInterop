package Group5.GameController;


import Group5.Agent.Explorer;
import Interop.Action.DropPheromone;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * this class checks for every agent which movements it can do
 */
public class AgentController {

    protected Point position;
    private double radius;
    //the angle the agent is currently facing
    protected Angle angle;
    protected boolean onSentryTower;
    protected boolean inShadedArea;
    private String agentType;
    private static Distance intruderViewRange ;
    private static Distance guardViewRange;
    private static Distance[] towerViewRange; //need two distances
    private static Distance shadedAreaIntruderViewRange;
    private static Distance shadedAreaGuardViewRange;
    private boolean isMoving=true;

    private double maxAngleRotation;

    protected boolean pheroMoneCooldownTimer;
    protected int pheroMoneCoolDownCounter;

    protected boolean teleported;



    protected AgentController(Point position, double radius, double maxRotation, String type, Distance viewRange){
        this.position = position;
        this.radius = radius;
        Point direction = new Point(position.getX(),position.getY());
        angle = Angle.fromRadians(0);
        this.maxAngleRotation = maxRotation;
        pheroMoneCooldownTimer=false;
        pheroMoneCoolDownCounter=0;
        teleported = false;
        agentType=type;
        this.intruderViewRange=viewRange;
        this.guardViewRange=viewRange;
        onSentryTower=false;
        inShadedArea=false;

    }

    public Point getPosition(){
        return position;
    }

    /**
     * This method is necessary for the agent. Otherwise we would need to break the rules for the simplest operations.
     * @param p A point of interest
     * @return The relative position of the agent to that point.
     */
    public Point getRelativePosition(Point p){
        return new Point(position.getX() - p.getX(), position.getY() - p.getY());
    }

    protected Angle getAngle() {
        return this.angle;
    }

    /**
     * This method is necessary for the agent. Otherwise we would need to break the rules for the simplest operations.
     * @param p A point defining one end of a line
     * @param q A point defining the other end of a line
     * @return The relative rotation of the agent with respect to that line.
     */
    public Angle getRelativeAngle(Point p, Point q){
        System.out.println("Agent angle: " + this.angle.getDegrees());
        Point pOrigin = new Point(Math.abs(p.getX() - q.getX()), Math.abs(p.getY() - q.getY()));
        return Angle.fromRadians(pOrigin.getClockDirection().getRadians() - this.angle.getRadians());
    }

    protected  boolean isOnSentryTower() {
        return this.onSentryTower;
    }

    protected boolean isInShadedArea(){
        return this.inShadedArea;
    }

    protected Distance[] getTowerViewRange() {
        return towerViewRange;
    }

    public void rotate(Angle addAngle){
        //changes the angle
        this.angle=Angle.fromRadians(this.angle.getRadians()+addAngle.getRadians());
        //System.out.println(angle.getDegrees());
        //reduces the angle so it has a value between 0 and 2pi but same value
        this.angle=Angle.fromRadians(this.angle.getRadians()%(2*Math.PI));
        //System.out.println(angle.getDegrees());

    }

    public Distance getViewRange() {
        Distance viewRange;
        if (this.agentType.equals("guard")) {
            viewRange = guardViewRange;

        }else {
            viewRange = intruderViewRange;
        }

        return viewRange;
    }

    public Distance getShadedAreaViewRange() {
        Distance viewRange;
        if (this.agentType.equals("guard")) {
            viewRange = shadedAreaGuardViewRange;

        }else {
            viewRange = shadedAreaIntruderViewRange;
        }

        return viewRange;
    }

    public boolean getIsMoving(){
        return isMoving;
    }

    /**
     * call this method to do a movement
     * @param distance
     * @param maxDistance
     */
    public boolean move(Distance distance, Distance maxDistance){
        if (distance.getValue()>maxDistance.getValue()){
            return false;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        if(GameRunner.moveValidility(position,newPosition,distance,maxDistance)){
            if (teleported==false){
                position = newPosition;
            }
            teleported=false;
        }
        isMoving = true;
        return true;
    }



    //TODO smell has to be implemented
    public void dropPheromone(DropPheromone dropPheromone, SmellPerceptType type){
        if (pheroMoneCooldownTimer){
            return;
        }

        pheroMoneCooldownTimer=true;


    }

    protected void setPosition(Point to){
        position = to;
    }

    public static void main(String[] args){
        IntruderController i = new IntruderController(new Point(1,1),1 , 2, 2, 2, new Distance(2));

        Angle a = i.getRelativeAngle(new Point(2,2), new Point(3, 2));
        System.out.println(a.getDegrees());

        Angle b = i.getRelativeAngle(new Point(0, 0), new Point(1, 0));
        System.out.println(b.getDegrees());

//        System.out.println("When angle is 0: " + i.explorer.correctAngleToWall(Angle.fromRadians(0)).getRadians());
//        System.out.println("When angle is pi/2: " + i.explorer.correctAngleToWall(Angle.fromRadians(Math.PI/2)).getRadians());
//        System.out.println("When angle is pi: " + i.explorer.correctAngleToWall(Angle.fromRadians(Math.PI)).getRadians());
//        System.out.println("When angle is -pi/2: " + i.explorer.correctAngleToWall(Angle.fromRadians(-Math.PI/2)).getRadians());
//        System.out.println("When angle is -pi: " + i.explorer.correctAngleToWall(Angle.fromRadians(-Math.PI)).getRadians());
//        System.out.println("When angle is -82.45 degrees: " + i.explorer.correctAngleToWall(Angle.fromDegrees(-82.45)).getRadians());
//        System.out.println("When angle is -182.45 degrees: " + i.explorer.correctAngleToWall(Angle.fromDegrees(-182.45)).getRadians());


    }

}
