package Group11.Agents.Guards;

import Group11.Point;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.Predicate;

public class FollowerGuard extends Guard {
    private boolean once = true;
    private ArrayList<GuardAction> queue = new ArrayList<>();
    RandomGuard randomGuard = new RandomGuard();

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if(once){
            super.setScenarioSettings(percepts);
            once = false;
        }

        Set<ObjectPercept> intruderSet =  percepts.getVision().getObjects().filter(objectPercept -> {
            if (objectPercept.getType().equals(ObjectPerceptType.Intruder)) {
                return true;
            }
            return false;
        }).getAll();
        if(!intruderSet.isEmpty()){
            ObjectPercept[] intruders = new ObjectPercept[intruderSet.size()];
            intruderSet.toArray(intruders);
            double x = 0;
            double y = 0;
            for(ObjectPercept intruder : intruders){
                x += intruder.getPoint().getX();
                y += intruder.getPoint().getY();
            }
            x /= intruders.length;
            y /= intruders.length;
            queue = super.moveTo(percepts.getAreaPercepts(),new Point(x, y));
            return queue.remove(0);
        }
        if(!queue.isEmpty()){
            GuardAction action = queue.remove(0);
            if(action instanceof Move){
                if(((Move) action).getDistance().getValue() == super.maxSpeed*super.slowDownInSentry){
                    double currentMaxSpeed = super.currentMaxSpeed(percepts.getAreaPercepts());
                    return new Move(new Distance(currentMaxSpeed));
                }
            }
            return action;
        }
        if(percepts.getAreaPercepts().isInSentryTower()){
            return new Rotate(Angle.fromDegrees(10));
        }
        return randomGuard.getAction(percepts);
    }
}
