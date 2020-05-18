package Group8.PathFinding;

import Group9.Game;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Utils.Utils;

import java.util.LinkedList;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;


public class SimplePathfinding {

    private LinkedList<IntruderAction> actionQueue;
    private final Angle MAX_ROTATION;
    private final double EPS = 1e-6;

    /*
    * The angle is never set directly towards the goal since this produces a NaN value when generating intruderpercepts
    * The deviation is a a way to make sure it is never exactly 0
    */
    public SimplePathfinding(IntruderPercepts percepts) {
        MAX_ROTATION = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
        actionQueue = new LinkedList<>();
    }

    public IntruderAction getMoveIntruder(IntruderPercepts percepts) {
        if(!actionQueue.isEmpty()){
            return actionQueue.poll();
        }
        else{
            if((Math.abs(percepts.getTargetDirection().getRadians())) <= EPS){
                return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
            }
            else {
                generateRotationSequence(percepts.getTargetDirection());
                if (actionQueue.peek() == null) {
                    return new NoAction();
                } else {
                    return actionQueue.poll();
                }
            }
        }

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

    private void generateRotationSequence(Angle rot){
      double radiansLeft = rot.getRadians();
      if(radiansLeft > 0) {
          while (radiansLeft != 0) {
              if (radiansLeft > MAX_ROTATION.getRadians()) {
                  actionQueue.add(new Rotate(MAX_ROTATION));
                  radiansLeft -= MAX_ROTATION.getRadians();
              } else {
                  actionQueue.add(new Rotate(Angle.fromRadians(radiansLeft - EPS)));
                  radiansLeft = 0;
              }
          }
      }
      else if(radiansLeft < 0){
          while (radiansLeft != 0) {
              if (Math.abs(radiansLeft) > MAX_ROTATION.getRadians()) {
                  actionQueue.add(new Rotate(Angle.fromRadians(-MAX_ROTATION.getRadians())));
                  radiansLeft += MAX_ROTATION.getRadians();
              } else {
                  actionQueue.add(new Rotate(Angle.fromRadians(-radiansLeft + EPS)));
                  radiansLeft = 0;
              }
          }
      }
      else{
          // Rotating 0 radians?
          return;
      }
  }
}
    //private final LinkedList<IntruderAction> actionQueue;
//    private final Angle MAX_ROTATION;
//
//
//    public SimplePathfinding(IntruderPercepts percepts) {
//        MAX_ROTATION = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
//        actionQueue = new LinkedList<>();
//    }
//
//    public IntruderAction getMoveIntruder(IntruderPercepts percepts) {
//        while (!actionQueue.isEmpty()){
//            return actionQueue.poll();
//        }
//
//        boolean corAngle = checkAngle(percepts);
//        System.out.println(corAngle);
//        if(corAngle) {
//            // Walk straight forwards as long as you can
////            if (percepts.wasLastActionExecuted()) {
////                return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
////            }
////            else{
////                // Generate the rotational actions
////                generateRotationSequence(Angle.fromRadians(Math.PI * Game._RANDOM.nextDouble()));
////                // Return the first one
////                return actionQueue.poll();
////            }
//                            return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));
//        }
//        else{
//            // Rotate towards the correct angle
//            generateRotationSequence(Angle.fromRadians(-percepts.getTargetDirection().getRadians()));
//            if(!actionQueue.isEmpty()){
//                return actionQueue.poll();
//            }
//            return new NoAction();
//        }
//    }
//
//    private void generateRotationSequence(Angle rot){
//        double degreesLeft = rot.getDegrees();
//        if(degreesLeft > 0) {
//            while (degreesLeft != 0) {
//                if (degreesLeft >= MAX_ROTATION.getDegrees()) {
//                    actionQueue.add(new Rotate(MAX_ROTATION));
//                    degreesLeft -= MAX_ROTATION.getDegrees();
//                } else {
//                    actionQueue.add(new Rotate(Angle.fromDegrees(degreesLeft)));
//                    degreesLeft = 0;
//                }
//            }
//        }
//        else if(degreesLeft < 0){
//            while (degreesLeft != 0) {
//                if (Math.abs(degreesLeft) >= MAX_ROTATION.getDegrees()) {
//                    actionQueue.add(new Rotate(Angle.fromDegrees(-MAX_ROTATION.getDegrees())));
//                    degreesLeft += MAX_ROTATION.getDegrees();
//                } else {
//                    actionQueue.add(new Rotate(Angle.fromDegrees(-degreesLeft)));
//                    degreesLeft = 0;
//                }
//            }
//        }
//        else{
//            // Rotating 0 degrees?
//            return;
//        }
//    }
//
//    private boolean checkAngle(IntruderPercepts percepts){
//        System.out.println(percepts.getTargetDirection().getRadians());
//        if(percepts.getTargetDirection().getRadians() == 0){
//            return true;
//        }
//        return false;
//    }
