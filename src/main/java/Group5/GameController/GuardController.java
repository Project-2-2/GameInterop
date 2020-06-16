package Group5.GameController;

import Group5.Agent.Explorer;
import Interop.Action.DropPheromone;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Action.Yell;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;

public class GuardController extends AgentController {




    private Distance normalMoveDistance;

    private double maxAngleRotation;

    protected Explorer explorer;

    protected GuardController(Point position, double radius, double maxAngleRotation, double moveDistance, Distance viewRange) {
        super(position, radius, maxAngleRotation,"guard",viewRange);
        normalMoveDistance = new Distance(moveDistance);
        explorer = new Explorer(1, this);
    }

    public void move(Distance distance){
        super.move(distance,normalMoveDistance);
    }

    public void move(Move move){ super.move(move.getDistance(),normalMoveDistance);}


    /**
     * first rotates the moves
     * @param move
     * @param rotate
     */
    public void moveWithRotation(Move move, Rotate rotate){
        super.rotate(rotate.getAngle());
        super.move(move.getDistance(),normalMoveDistance);
    }





    public void yell(Hearing hearing){
        hearing.yellSound(this.getPosition());
    }


    public boolean dropPheromone(SmellPercept type){
        if (super.dropPheromone(type)){
            GameRunner.addPheromoneGuards(type, super.getPosition());
            return true;
        }

        return false;

    }



}
