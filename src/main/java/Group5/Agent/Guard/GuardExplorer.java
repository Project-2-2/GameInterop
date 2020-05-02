package Group5.Agent.Guard;

import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Set;

public class GuardExplorer implements Guard {


    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        return explore(percepts);
    }

    public GuardAction explore(GuardPercepts percepts){
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();

        if (vision.contains(ObjectPerceptType.EmptySpace)){
            System.out.println("heia");
        }
        //System.out.println(vision.size());
        if (!percepts.wasLastActionExecuted()&&vision.size()>0) {
            double angleToWallsDegrees = 0;
            int count = 0;
            for (ObjectPercept e : vision) {
                //prevents to turn away from a intruder
                if (!(e.getType()== ObjectPerceptType.Intruder)){
                    if (Angle.fromDegrees(0).getDistance(e.getPoint().getClockDirection()).getDegrees()>180){
                        angleToWallsDegrees = angleToWallsDegrees + e.getPoint().getClockDirection().getDegrees()-360;
                    }else{
                        angleToWallsDegrees = angleToWallsDegrees + Angle.fromDegrees(0).getDistance(e.getPoint().getClockDirection()).getDegrees();
                    }
                    count++;
                    //System.out.println(Angle.fromDegrees(0).getDistance(e.getPoint().getClockDirection()).getDegrees());
                    //System.out.println(e.getPoint().getClockDirection().getDegrees());
                }
                //TODO Agents get stuck if it sees only a corner of door
                //this is necessary otherwise it gets stuck in a door, however sometimes it gets stuck by this if it sees only a corner of door
                if(e.getType()==ObjectPerceptType.Door){
                    //System.out.println("door found");
                    return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
                }
            }
            //System.out.println("biem");
            //System.out.println(angleToWallsDegrees);
            System.out.println(angleToWallsDegrees/count);
            if(angleToWallsDegrees!=0){
                return new Rotate(Angle.fromDegrees(angleToWallsDegrees/count));
            }
            //System.out.println(vision.size());
            //return new Rotate(Angle.fromDegrees(angleToWallsDegrees/count));
        }

        if (!percepts.wasLastActionExecuted()){
            System.out.println("kak");
            return new Rotate(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle());
        }
        return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
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
