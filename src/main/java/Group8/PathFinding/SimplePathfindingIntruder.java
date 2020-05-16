package Group8.PathFinding;

import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.FieldOfView;

import java.util.ArrayList;
import java.util.List;

public class SimplePathfindingIntruder {


    public IntruderAction getMoveIntruder(IntruderPercepts percepts){
        boolean corAngle = checkAngle(percepts);


        if(corAngle) {
            // Walk straight forwards as long as you can
            if (percepts.wasLastActionExecuted()) {
                return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
            }
        }
        else{
            // Rotate towards the correct angle
            return getRotation(percepts);
        }


        return null;
    }

    private Rotate getRotation(IntruderPercepts percepts){
        // TODO: Implement a way to figure out how to rotate

        return new Rotate(Angle.fromRadians(0));
    }

    private boolean checkAngle(IntruderPercepts percepts){
        // TODO: Check if you are going in the correct direction

        return false;
    }

    private double getSpeedModifier(IntruderPercepts intruderPercepts)
    {
        SlowDownModifiers slowDownModifiers = intruderPercepts.getScenarioIntruderPercepts().getScenarioPercepts().getSlowDownModifiers();
        if(intruderPercepts.getAreaPercepts().isInWindow())
        {
            return slowDownModifiers.getInWindow();
        }
        else if(intruderPercepts.getAreaPercepts().isInSentryTower())
        {
            return slowDownModifiers.getInSentryTower();
        }
        else if(intruderPercepts.getAreaPercepts().isInDoor())
        {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }












    public List<IntruderAction> generateActionSequenceIntruder(Point a, Point b, IntruderPercepts intruderPercepts){
        ArrayList<IntruderAction> sequence = new ArrayList<>();
        FieldOfView FOV = intruderPercepts.getVision().getFieldOfView();
        Angle viewAngle = FOV.getViewAngle();


        return sequence;
    }


}
