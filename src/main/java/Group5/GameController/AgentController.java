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
    private String agentType;
    private static Distance intruderViewRange ;
    private static Distance guardViewRange;
    private static Distance[] towerViewRange; //need two distances

    private double maxAngleRotation;

    protected boolean pheroMoneCooldownTimer;
    protected int pheroMoneCoolDownCounter;

    protected boolean teleported;



    protected AgentController(Point position, double radius, double maxRotation, String type, Distance viewRange){
        this.position = position;
        this.radius = radius;
        Point direction = new Point(position.getX(),position.getY());
        angle = position.getClockDirection();
        this.maxAngleRotation = maxRotation;
        pheroMoneCooldownTimer=false;
        pheroMoneCoolDownCounter=0;
        teleported = false;
        agentType=type;
        this.intruderViewRange=viewRange;
        this.guardViewRange=viewRange;

    }

    protected Point getPosition(){
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
        System.out.println("Agent angle: " + this.angle.getRadians());
        Point pOrigin = new Point(Math.abs(p.getX() - q.getX()), Math.abs(p.getY() - q.getY()));
        return Angle.fromRadians(pOrigin.getClockDirection().getRadians() - this.angle.getRadians());
    }

    protected  boolean isOnSentryTower() {
        return this.onSentryTower;
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
        return true;
    }

    public void noAction(NoAction noAction){
        return;
    }


    /**
     * call this method as an agent if you want to do a movement that includes opening a door
     * you don't have to call the normal move method after this
     * @param distance
     * @param maxDistance
     */
    public void openDoor(Distance distance, Distance maxDistance){
        Distance newMaxDistance = new Distance(maxDistance.getValue()*Door.getSlowDownModifier());
        if (distance.getValue()>newMaxDistance.getValue()){
            return;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        //opens the door if there is really a door
        if (GameRunner.openDoorValidility(position,newPosition)){
            move(distance,newMaxDistance);
            return;
        }
        move(distance,maxDistance);
    }

    /**
     * call this method as an agent if you want to do a movement that includes opening a window
     * you don't have to call the normal move method after this
     * @param distance
     * @param maxDistance
     */
    public void openWindow(Distance distance, Distance maxDistance){
        Distance newMaxDistance = new Distance(maxDistance.getValue()*Window.getSlowDownModifier());
        if (distance.getValue()>newMaxDistance.getValue()){
            return;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        //opens the door if there is really a door
        if (GameRunner.openWindowValidility(position,newPosition)){
            move(distance,newMaxDistance);
            return;
        }
        move(distance,maxDistance);
    }

    /**
     * call this method as an agent if you want to do a movement that includes entering a sentry
     * you don't have to call the normal move method after this
     * @param distance
     * @param maxDistance
     */
    public void enterSentry(Distance distance, Distance maxDistance){
        Distance newMaxDistance = new Distance(maxDistance.getValue()*SentryTower.getSlowDownModifer());
        if (distance.getValue()>newMaxDistance.getValue()){
            return;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        //opens the door if there is really a door
        if (GameRunner.enterSentry(position,newPosition)){
            move(distance,newMaxDistance);
            onSentryTower = true;
            return;
        }
        move(distance,maxDistance);
    }


    public boolean teleport(){
        Point newLocation =GameRunner.teleportValidility(position,radius);
        if (newLocation.equals(position)){
            return false;
        }
        position=newLocation;
        return true;
    }

    public void noAction(){
        return;
    }

    /*
    public void vision(){

        ArrayList<Point> rayCasts = visionVectors();

        for (int i =0; i<rayCasts.size();i++){

        }


    }

     */


    /**
     * returns a list of 45 vectors represented raycasts
     * these raycasts can be used to check for colission with objects
     * @return
     */
    /*
    private ArrayList<Point> visionVectors(){

        Point eye = new Point(position.getX(),position.getY());
        ArrayList<Point> rayCasts = new ArrayList<>(45);
        rayCasts.add(eye);
        for (int i =1;i<23;i++){
            double angle = (Math.PI/180)*i;
            //System.out.println(angle);

            //System.out.println("eye");
            //System.out.println(eye.toString());
            Point rotateDegree = Sat.add(eye,Sat.mul(direction,1000));
            Point rayCastVector = Sat.rotate(rotateDegree,angle);
            //System.out.println(rayCastVector.toString());
            rayCasts.add(rayCastVector);
            //rotateDegree = rotateDegree.rotate()
        }

        // System.out.println("BIEM");
        for (int i =0;i<22;i++){
            double angle = (Math.PI/180)*i*-1;

            //System.out.println(angle);

            //System.out.println("eye");
            //System.out.println(eye.toString());
            Point rotateDegree = Sat.add(eye,Sat.mul(   direction,1000));
            Point rayCastVector = Sat.rotate(rotateDegree,angle);
            //System.out.println(rayCastVector.toString());
            rayCasts.add(rayCastVector);
            //rotateDegree = rotateDegree.rotate()
        }

        return rayCasts;
    }
    */

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

    }

}
