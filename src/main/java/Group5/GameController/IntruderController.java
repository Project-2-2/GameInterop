package Group5.GameController;

import Group5.Agent.Explorer;
import Interop.Action.DropPheromone;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Action.Sprint;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPerceptType;

public class IntruderController extends AgentController {




    private Distance normalMoveDistance;
    private Distance sprintDistance;

    private double maxAngleRotation;

    protected boolean sprintCooldownTimer;
    protected int sprintCoolDownCounter;

    public Explorer explorer;




    protected IntruderController(Point position, double radius, double moveDistance, double sprintDistance, double maxAngleRotation,Distance viewRange) {
        super(position, radius, maxAngleRotation,"intruder",viewRange);
        normalMoveDistance = new Distance(moveDistance);
        this.sprintDistance = new Distance(sprintDistance);
        sprintCooldownTimer=false;
        sprintCoolDownCounter=0;
        explorer = new Explorer(1, this);
    }


    public void move(Distance distance){
        super.move(distance,normalMoveDistance);
    }


    public boolean move(Move move){
        return super.move(move.getDistance(),normalMoveDistance);
    }


    /**
     * first rotates then moves
     * @param move
     * @param rotate
     */
    public void moveWithRotation(Move move, Rotate rotate){
        super.rotate(rotate.getAngle());
        super.move(move.getDistance(),normalMoveDistance);
    }

    public void sprint(Sprint sprint){
        if(sprint.getDistance().getValue()>sprintDistance.getValue()||sprintCooldownTimer){
            return;
        }

        double newX= position.getX()+sprint.getDistance().getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+sprint.getDistance().getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        if(GameRunner.moveValidility(position,newPosition,sprint.getDistance(),sprintDistance)){
            position = newPosition;
        }

        sprintCooldownTimer=true;



    }

    //TODO smell has to be implemented
    public void dropPheromone(DropPheromone dropPheromone){
        super.dropPheromone(dropPheromone, SmellPerceptType.Pheromone1);

    }


    //TODO IMPLEMENT COOLDOWN
    public void sprint(Distance distance){
        if (distance.getValue()>sprintDistance.getValue()){
            return;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        if(GameRunner.moveValidility(position,newPosition,distance,sprintDistance)){
            position = newPosition;
        }
    }

    /**
     * call this method as an agent if you want to do a movement that includes opening a door
     * you don't have to call the normal move method after this
     * @param move distance of the movement
     */
    public void openDoor(Move move){
        super.openDoor(move.getDistance(),normalMoveDistance);
    }


    /**
     * call this method as an agent if you want to do a movement that includes opening a window
     * you don't have to call the normal move method after this
     * @param move distance of the movement
     */
    public void openWindow(Move move){
        super.openWindow(move.getDistance(),normalMoveDistance);
    }

    /**
     * call this method as an agent if you want to do a movement that includes entering a sentry
     * you don't have to call the normal move method after this
     * @param move distance of the movement
     */
    public void enterSentry(Move move){
        super.enterSentry(move.getDistance(),normalMoveDistance);
    }

}
