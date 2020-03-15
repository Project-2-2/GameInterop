package Group5.GameController;


import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
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


    private double maxAngleRotation;

    protected AgentController(Point position, double radius, double maxRotation){
        this.position = position;
        this.radius = radius;
        direction = new Vector2D(position);
        direction = new Vector2D(2,2);
        angle = position.getClockDirection();
        this.maxAngleRotation = maxRotation;

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


    /**
     * call this method to do a movement
     * @param distance
     * @param maxDistance
     */
    public void move(Distance distance, Distance maxDistance){
        if (distance.getValue()>maxDistance.getValue()){
            return;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        if(GameRunner.moveValidility(position,newPosition)){
            position = newPosition;
        }
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

    public void vision(){

        ArrayList<Vector2D> rayCasts = visionVectors();

        for (int i =0; i<rayCasts.size();i++){

        }


    }

    /**
     * returns a list of 45 vectors represented raycasts
     * these raycasts can be used to check for colission with objects
     * @return
     */
    private ArrayList<Vector2D> visionVectors(){

        Vector2D eye = new Vector2D(position);
        ArrayList<Vector2D> rayCasts = new ArrayList<>(45);
        rayCasts.add(eye);
        for (int i =1;i<23;i++){
            double angle = (Math.PI/180)*i;
            //System.out.println(angle);

            //System.out.println("eye");
            //System.out.println(eye.toString());
            Vector2D rotateDegree = new Vector2D(eye).add(new Vector2D(direction).mul(1000));
            Vector2D rayCastVector = rotateDegree.rotate(angle);
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
            Vector2D rotateDegree = new Vector2D(eye).add(new Vector2D(direction).mul(1000));
            Vector2D rayCastVector = rotateDegree.rotate(angle);
            //System.out.println(rayCastVector.toString());
            rayCasts.add(rayCastVector);
            //rotateDegree = rotateDegree.rotate()
        }

        return rayCasts;


    }



}
