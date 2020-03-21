package Group5.GameController;


import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
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

    private Point position;
    private double radius;
    private Vector2D direction;  //the direction an agent is walking
    private Angle angle;
    private Distance viewDistance;


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

    /**
     *
     * @param agent the agent you want to update vision
     * @return An ObjectPercepts (object containing the perceived objects)
     */
    private ObjectPercepts vision(AgentController agent) {
        double targetX, targetY;
        ArrayList<Area> areas = Area.getAreas();
        ArrayList<ObjectPercept> perceivedObjects = new ArrayList<>();

        double viewRange = agent.viewDistance.getValue();

        FieldOfView fieldOfView = new FieldOfView(new Distance(5.0), Angle.fromRadians(Math.PI/4));

        double currentX = agent.getPosition().getX();
        double currentY = agent.getPosition().getY();
        double angle = agent.angle.getDegrees();

        for (double i=-22.5; i <=22.5; i++){
            Vector2D point1 = new Vector2D(currentX, currentY);

            if (angle + i > 360) {
                targetX = viewRange * Math.cos(angle + i - 360) + currentX;
                targetY = viewRange * Math.sin(angle + i - 360) + currentY;

            }else if (angle + i < 0) {
                targetX = viewRange * Math.cos(angle + i + 360) + currentX;
                targetY = viewRange * Math.sin(angle + i + 360) + currentY;

            }else{
                targetX = viewRange * Math.cos(angle + i) + currentX;
                targetY = viewRange * Math.sin(angle + i) + currentY;

            }
            Vector2D point2 = new Vector2D(targetX, targetY);
            Vector2D[] vector1 = {point1, point2};
            ArrayList<ArrayList<Vector2D>> positions;
            for (Area area : areas) {
                positions = area.getVectorPosition();
                for (ArrayList<Vector2D> arr: positions) {
                    Vector2D[] vector2 = {arr.get(0), arr.get(1)};

                    if (Sat.hasCollided(vector1, vector2)) {
                        perceivedObjects.add(new ObjectPercept(area.getObjectsPerceptType(), new Point(0,0) ));
                    }
                }
            }
        }
        Area.bubbleSort(perceivedObjects, agent);
        checkPerceivedObjects(perceivedObjects);
        Set<ObjectPercept> toReturn = new HashSet<>(perceivedObjects);
        return new ObjectPercepts(toReturn);
    }

    /**
     * Checks which object you can see
     */
    private void checkPerceivedObjects(ArrayList<ObjectPercept> perceivedObjects) {
        boolean seeFarther = true; // false if there is an area in front that is not opaque

        for (ObjectPercept object : perceivedObjects) {
            if (!seeFarther) {
                perceivedObjects.remove(object);
            } else if (object.getType().isOpaque()) {
                seeFarther = false;
            }
        }
    }

}
