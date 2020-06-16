package Group4.Guards;

/**
 * Description/ Idea of this guard
    * if he sees an intruder he follows it
    * if he lost him again, he drops a pheromone telling the guard he lost an intruder
        * basically saying an intruder has to be nearby
**/


import Group9.Game;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.*;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Geometry.Point;

public class FollowGuard implements Guard{
    private double error = 7.5;
    boolean sawintruder = false;
    boolean positioning = false;
    double totalAngle;
    Distance oldSmellDist = null;

    public FollowGuard() {}

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        for(ObjectPercept obj : percepts.getVision().getObjects().getAll()) {
            if (obj.getType() == ObjectPerceptType.Intruder) {
                sawintruder = true;
                if (obj.getPoint().getClockDirection().getDegrees() < error || 360 - obj.getPoint().getClockDirection().getDegrees() < error) {
                    return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
                } else {
                    //System.out.println("Degrees towards target: " + obj.getPoint().getClockDirection().getDegrees());
                    if (obj.getPoint().getClockDirection().getDegrees() > 180){
                        // System.out.println("Rotating: " + (Math.toDegrees(obj.getPoint().getClockDirection().getRadians()-2*Math.PI)));
                        return new Rotate(new Angle(-1* (obj.getPoint().getClockDirection().getRadians()-2*Math.PI)));
                    }
                    else {
                        //System.out.println("Rotating: " + (Math.toDegrees(obj.getPoint().getClockDirection().getRadians())));
                        return new Rotate(new Angle(-1 * (obj.getPoint().getClockDirection().getRadians())));
                    }
                }
            }
            if (obj.getType() == ObjectPerceptType.TargetArea) {
                positioning = true;
            } else if (positioning) {
                if (new Distance(obj.getPoint(), new Point(0.0, 0.0)).getValue() > 0.5) {
                    if (obj.getPoint().getClockDirection().getDegrees() < error || 360 - obj.getPoint().getClockDirection().getDegrees() < error) {
                        return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()));
                    }
                    if (obj.getPoint().getClockDirection().getDegrees() > 180) {
                        return new Rotate(new Angle(-1 * (obj.getPoint().getClockDirection().getRadians() - 2 * Math.PI)));
                    } else {
                        return new Rotate(new Angle(-1 * (obj.getPoint().getClockDirection().getRadians())));
                    }
                } else if (new Distance(obj.getPoint(), new Point(0.0, 0.0)).getValue() < 0.5) {
                    totalAngle = obj.getPoint().getClockDirection().getRadians();
                    if (totalAngle > percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians()) {
                        totalAngle = totalAngle - percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
                        return new Rotate(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle());
                    } else {
                        return new Rotate(obj.getPoint().getClockDirection());
                    }
                } else if (totalAngle == 0) {
                    positioning = false;
                    totalAngle = Math.PI / 2;
                    return new Move(new Distance(10));
                }
            }
        }

        if(sawintruder){
            sawintruder = false;
            return new DropPheromone(SmellPerceptType.values()[1]);
        }

        for(SmellPercept smell : percepts.getSmells().getAll()) {
            if(smell.getType() == SmellPerceptType.Pheromone1){
                if(oldSmellDist.equals(null)) oldSmellDist = smell.getDistance();
                if (oldSmellDist.getValue() < smell.getDistance().getValue()){
                    return  new Rotate(new Angle(10));
                } else {
                    return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
                }
            }
        }

        //look for intruders in the SentryTower
        if(percepts.getAreaPercepts().isInSentryTower()){
            return  new Rotate(Angle.fromDegrees(15));
        }

        for(ObjectPercept obj : percepts.getVision().getObjects().getAll()) {
            if (obj.getType() == ObjectPerceptType.SentryTower) {
                sawintruder = true;
                if (obj.getPoint().getClockDirection().getDegrees() < error || 360 - obj.getPoint().getClockDirection().getDegrees() < error) {
                    return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
                } else {
                    //System.out.println("Degrees towards target: " + obj.getPoint().getClockDirection().getDegrees());
                    if (obj.getPoint().getClockDirection().getDegrees() > 180){
                        // System.out.println("Rotating: " + (Math.toDegrees(obj.getPoint().getClockDirection().getRadians()-2*Math.PI)));
                        return new Rotate(new Angle(-1* (obj.getPoint().getClockDirection().getRadians()-2*Math.PI)));
                    }
                    else {
                        //System.out.println("Rotating: " + (Math.toDegrees(obj.getPoint().getClockDirection().getRadians())));
                        return new Rotate(new Angle(-1 * (obj.getPoint().getClockDirection().getRadians())));
                    }
                }
            }
        }

        if(!percepts.wasLastActionExecuted()) {
            return new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble()));
        }else{
            return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
        }
    }

    private double getSpeedModifier(GuardPercepts guardPercepts)
    {
        SlowDownModifiers slowDownModifiers =  guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getSlowDownModifiers();
        if(guardPercepts.getAreaPercepts().isInWindow())
        {
            return slowDownModifiers.getInWindow();
        }
        else if(guardPercepts.getAreaPercepts().isInSentryTower())
        {
            return slowDownModifiers.getInSentryTower();
        }
        else if(guardPercepts.getAreaPercepts().isInDoor())
        {
            return slowDownModifiers.getInDoor();
        }

        return 1;
    }

}
