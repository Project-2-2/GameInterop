package Group6.Agent.Guard;

import Group6.Agent.Behaviour.*;
import Group6.Geometry.Vector;
import Group6.WorldState.Contract.Object;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;



public class ScoutGuard implements Guard {

    private Vector lastSeen;
    private Queue<GuardAction> actions = new LinkedList<>();

    public GuardAction getAction(GuardPercepts percepts) {
        if(!actions.isEmpty()){
            return actions.poll();
        }
        if(!getIntruder(percepts).getAll().isEmpty()){

            if(lastSeen == null){
                lastSeen = meanPosition(getIntruder(percepts));
                return(GuardAction)new FollowIntruderBehaviour().getAction(percepts);
            }
            else{
                Vector now = meanPosition(getIntruder(percepts));
                Vector dir = now.subtract(lastSeen).toUnitVector();
                double speed = now.subtract(lastSeen).getLength();
                Vector target = dir.multiply(speed*4);

                actions.addAll(collectPoints(percepts,target));
                lastSeen = now;
                return actions.poll();
            }
        }
        else{
            return (GuardAction)new ExploreBehaviour().getAction(percepts);
        }
    }


    public static ObjectPercepts getIntruder(Percepts percepts) {
        return percepts.getVision().getObjects().filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Intruder);
    }
    public static Set<SoundPercept> getSoundYell(Percepts percept) {
        return percept.getSounds().filter(soundPercept -> soundPercept.getType() == SoundPerceptType.Yell).getAll();
    }
    public Vector meanPosition(ObjectPercepts calc){
        Vector mean = new Vector(0,0);
        for(ObjectPercept op : calc.getAll()) {
            mean = mean.add(new Vector(op.getPoint().getX(), op.getPoint().getY()));

        }
        return mean.divide(calc.getAll().size());
    }

    public Queue<GuardAction> collectPoints(GuardPercepts percepts, Vector target){
        double angle =  Math.atan2(target.getY(), target.getX()) - Math.atan2(1,0);
        int fullRotation = (int)(angle / percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians());
        double remain = (angle % percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians());

        LinkedList<GuardAction> actions = new LinkedList<>();
        for(int i = 0; i < fullRotation; i++){
            actions.push(new Rotate(Angle.fromRadians( percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians()*Math.signum(angle))));
        }
        if(Math.abs(remain)>0){
            actions.push(new Rotate(Angle.fromRadians(remain)));
        }
        double distance = target.getLength();
        int fullMoves = (int)(distance/percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue());
        double leftMoves = (double)(distance%percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue());
        for(int i = 0; i < fullMoves; i++) {
            actions.push(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue())));
        }
        if(leftMoves > 0){
            actions.push(new Move(new Distance(leftMoves)));
        }
        return actions;
    }


}
