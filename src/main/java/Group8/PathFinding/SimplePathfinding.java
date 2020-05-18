package Group8.PathFinding;

import Group9.Game;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Utils.Utils;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;


public class SimplePathfinding {

    private final Angle MAX_ROTATION;
    private final double EPS = 1e-6;

    /*
    * The angle is never set directly towards the goal since this produces a NaN value when generating intruderpercepts
    * The deviation is a a way to make sure it is never exactly 0
    */
    public SimplePathfinding(IntruderPercepts percepts) {
        MAX_ROTATION = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
    }

    public IntruderAction getMoveIntruder(IntruderPercepts percepts) {
        if((Math.abs(percepts.getTargetDirection().getRadians())) <= EPS){
            return new Move(new Distance(percepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * getSpeedModifier(percepts)));

        }
        if(percepts.getTargetDirection().getRadians() > MAX_ROTATION.getRadians()){
            //System.out.println("io");
            return new Rotate(Angle.fromRadians(MAX_ROTATION.getRadians()));
        }else{
            //System.out.println(Utils.isRealNumber(percepts.getTargetDirection().getRadians()));
            //System.out.println(String.format("Angle is real: %b, number: %f (in rad)",Utils.isRealNumber(percepts.getTargetDirection().getRadians()),percepts.getTargetDirection().getRadians()));
            return new Rotate(Angle.fromRadians(percepts.getTargetDirection().getRadians() + deviation()));
        }
    }

    private double deviation(){
        if(Game._RANDOM.nextBoolean()) {
            return Game._RANDOM.nextDouble() * EPS;
        }else{
            return -Game._RANDOM.nextDouble() * EPS;
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
}
    //private final LinkedList<IntruderAction> actionQueue;
//    private final Angle MAX_ROT;
//
//
//    public SimplePathfinding(IntruderPercepts percepts) {
//        MAX_ROT = percepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
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
//                if (degreesLeft >= MAX_ROT.getDegrees()) {
//                    actionQueue.add(new Rotate(MAX_ROT));
//                    degreesLeft -= MAX_ROT.getDegrees();
//                } else {
//                    actionQueue.add(new Rotate(Angle.fromDegrees(degreesLeft)));
//                    degreesLeft = 0;
//                }
//            }
//        }
//        else if(degreesLeft < 0){
//            while (degreesLeft != 0) {
//                if (Math.abs(degreesLeft) >= MAX_ROT.getDegrees()) {
//                    actionQueue.add(new Rotate(Angle.fromDegrees(-MAX_ROT.getDegrees())));
//                    degreesLeft += MAX_ROT.getDegrees();
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
