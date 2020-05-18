package Group5.GameController;

import Group5.Agent.Explorer;
import Group5.Agent.IntruderAgent;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Action.Sprint;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;

public class IntruderController extends AgentController {




    private Distance normalMoveDistance;
    private Distance sprintDistance;

    private double maxAngleRotation;

    protected boolean sprintCooldownTimer;
    protected int sprintCoolDownCounter;

    protected Explorer explorer;
    protected IntruderAgent intruderAgent;
    
    protected boolean isCaptured;




    protected IntruderController(Point position, double radius, double moveDistance, double sprintDistance, double maxAngleRotation,Distance viewRange) {
        super(position, radius, maxAngleRotation,"intruder",viewRange);
        normalMoveDistance = new Distance(moveDistance);
        this.sprintDistance = new Distance(sprintDistance);
        sprintCooldownTimer=false;
        sprintCoolDownCounter=0;
        explorer = new Explorer(1, this);
//        intruderAgent = new IntruderAgent(this);
        isCaptured=false;
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

        if(GameRunner.moveValidity(position,newPosition,sprint.getDistance(),sprintDistance)){
            position = newPosition;
        }

        sprintCooldownTimer=true;



    }


    public boolean dropPheromone(SmellPercept type){
        if (super.dropPheromone(type)){
            GameRunner.addPheromoneIntruders(type, super.getPosition());
            return true;
        }

        return false;

    }


    //TODO IMPLEMENT COOLDOWN
    public void sprint(Distance distance){
        if (distance.getValue()>sprintDistance.getValue()){
            return;
        }
        double newX= position.getX()+distance.getValue()*Math.cos(angle.getRadians());
        double newY= position.getY()+distance.getValue()*Math.sin(angle.getRadians());

        Point newPosition = new Point(newX,newY);

        if(GameRunner.moveValidity(position,newPosition,distance,sprintDistance)){
            position = newPosition;
        }
    }

    protected Direction getTargetDirection(){
        return GameRunner.getTargetDirection(this);
    }



}
