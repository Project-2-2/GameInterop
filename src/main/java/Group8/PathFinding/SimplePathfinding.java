package Group8.PathFinding;

import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.FieldOfView;

import java.util.*;

public class SimplePathfinding {

    private final LinkedList<IntruderAction> actionQueue;
    private final Angle MAX_ROT;
    private boolean first = true;


    public SimplePathfinding(IntruderPercepts percepts) {
        MAX_ROT = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
        actionQueue = new LinkedList<>();
        System.out.println(MAX_ROT.getDegrees());
    }

    public IntruderAction getMoveIntruder(IntruderPercepts percepts) {
        while (!actionQueue.isEmpty()){
            System.out.println("Returning queued action");
            return actionQueue.poll();
        }

        return new NoAction();
    }

    private void generateRotationSequence(Angle rot){
        double degreesLeft = rot.getDegrees();
        if(degreesLeft > 0) {
            while (degreesLeft != 0) {
                if (degreesLeft >= MAX_ROT.getDegrees()) {
                    actionQueue.add(new Rotate(MAX_ROT));
                    degreesLeft -= MAX_ROT.getDegrees();
                } else {
                    actionQueue.add(new Rotate(Angle.fromDegrees(degreesLeft)));
                    degreesLeft = 0;
                }
            }
        }
        else if(degreesLeft < 0){
            while (degreesLeft != 0) {
                if (Math.abs(degreesLeft) >= MAX_ROT.getDegrees()) {
                    actionQueue.add(new Rotate(Angle.fromDegrees(-MAX_ROT.getDegrees())));
                    degreesLeft += MAX_ROT.getDegrees();
                } else {
                    actionQueue.add(new Rotate(Angle.fromDegrees(-degreesLeft)));
                    degreesLeft = 0;
                }
            }
        }
        else{
            // Rotating 0 degrees?
            return;
        }
    }

    private Rotate getRotation(IntruderPercepts percepts){
        // TODO: Implement a way to figure out how to rotate

        return new Rotate(Angle.fromRadians(0));
    }

    private boolean checkAngle(IntruderPercepts percepts){
        // TODO: Check if you are going in the correct direction

        return true;
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
//boolean corAngle = checkAngle(percepts);

//return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
//        if(corAngle) {
//            // Walk straight forwards as long as you can
//            if (percepts.wasLastActionExecuted()) {
//                return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
//            }
//            else{
//                return new Rotate(Angle.fromRadians(Math.PI));
//            }
//        }
//        else{
//            // Rotate towards the correct angle
//            return getRotation(percepts);
//        }