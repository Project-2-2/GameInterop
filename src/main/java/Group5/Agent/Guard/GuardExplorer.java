package Group5.Agent.Guard;

import Group5.GameController.AgentController;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.ScenarioGuardPercepts;

import java.util.*;

public class GuardExplorer implements Guard {

    private Queue<GuardAction> actionQueue = new LinkedList<>();

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        //return explore(percepts);
        //if queue is empty otherwise do actions inside queue
        if (actionQueue.size()<=0){
            explore(percepts);
        }
        //System.out.println(actionQueue.size());
        //System.out.println("yes");
        return actionQueue.poll();

    }

    public void addActionToQueue(GuardAction action, GuardPercepts percepts) {
        double maxMoveRange = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
        Angle maxRotationAngle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle();
        /*
        if (action instanceof Rotate ) {
            double rotateValue = ((Rotate) action).getAngle().getDegrees();
            if (rotateValue > maxRotationAngle.getDegrees()) {
                while (rotateValue > 0) {
                    if (rotateValue > maxRotationAngle.getDegrees()) {
                        actionQueue.add(new Rotate(maxRotationAngle));
                        rotateValue -= maxRotationAngle.getDegrees();
                    }else {
                        actionQueue.add(new Rotate(Angle.fromDegrees(rotateValue)));
                        rotateValue = 0;
                    }
                }

            }else
                actionQueue.add(action);
        }else if (action instanceof Move) {
            double distance = ((Move) action).getDistance().getValue();
            if (distance > maxMoveRange) {
                while (distance > 0) {
                    if (distance > maxMoveRange) {
                        actionQueue.add(new Move(new Distance(maxMoveRange)));
                        distance -= maxMoveRange;
                    }else {
                        actionQueue.add(new Move(new Distance(distance)));
                        distance = 0;
                    }
                }
            }
        }else

         */
            actionQueue.add(action);

    }

    public void explore(GuardPercepts percepts){
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();
        ArrayList<ObjectPerceptType> visionPerceptTypes = new ArrayList<>();
        Set<SoundPercept> sound = percepts.getSounds().getAll();

        ArrayList<SoundPerceptType> soundPerceptTypes = new ArrayList<>();

        percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle();

        for (ObjectPercept e : vision){
            visionPerceptTypes.add(e.getType());
        }


        for (SoundPercept s : sound){
            soundPerceptTypes.add((s.getType()));
        }



        if (visionPerceptTypes.contains(ObjectPerceptType.Intruder)) {
            seeIntruder(percepts,vision);
            return;
        }

        if (soundPerceptTypes.contains(SoundPerceptType.Yell)&&Math.random()<=0.95){
            rotateToYell(percepts);
            return;
        }
        if (soundPerceptTypes.size()>0&&Math.random()<=0.2){
            rotateToNoise(percepts);
//            System.out.println("check");
            return;
        }

        if (!percepts.wasLastActionExecuted()){
            moveParallelToWall(percepts,vision);
            return;
        }
        addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts))), percepts);
        return;
        //return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
    }
    public void moveParallelToWall(GuardPercepts percepts, Set<ObjectPercept> vision){
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
                    addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts))), percepts);
                    //return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
                    return;
                }
            }
            //System.out.println("biem");
            //System.out.println(angleToWallsDegrees);
            // System.out.println(angleToWallsDegrees/count);
            if(angleToWallsDegrees!=0){
                addActionToQueue(new Rotate(Angle.fromDegrees(angleToWallsDegrees/count)), percepts);
                return;
                //return new Rotate(Angle.fromDegrees(angleToWallsDegrees/count));
            }
            //System.out.println(vision.size());
            //return new Rotate(Angle.fromDegrees(angleToWallsDegrees/count));
        }

        if (!percepts.wasLastActionExecuted()){
            System.out.println("kak");

            Angle randomAngle = Angle.fromDegrees(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()*Math.random());
            addActionToQueue(new Rotate(randomAngle), percepts);
            return;
            //return new Rotate(randomAngle);
        }
    }

    public void seeIntruder(GuardPercepts percepts, Set<ObjectPercept> vision){
        // System.out.println("found intruder");
        double angleToIntruder = 0;
        int count = 0;
        double distanceToIntruder = 0;
        for (ObjectPercept e : vision){
            if (e.getType()==ObjectPerceptType.Intruder){
                distanceToIntruder = distanceToIntruder+ Math.abs(percepts.getVision().getFieldOfView().getRange().getValue()-e.getPoint().getDistanceFromOrigin().getValue());
                //System.out.println(distanceToIntruder);

                if (Angle.fromDegrees(0).getDistance(e.getPoint().getClockDirection()).getDegrees()>180){
                    angleToIntruder = angleToIntruder + e.getPoint().getClockDirection().getDegrees()-360;
                }else{
                    angleToIntruder = angleToIntruder + Angle.fromDegrees(0).getDistance(e.getPoint().getClockDirection()).getDegrees();
                }
                count++;
            }
        }
            /*
            if ((distanceToIntruder/count)<percepts.getScenarioGuardPercepts().getScenarioPercepts().getCaptureDistance().getValue()){
                // System.out.println("biem");
                System.out.println(distanceToIntruder/count-percepts.getScenarioGuardPercepts().getScenarioPercepts().getCaptureDistance().getValue());
                addActionToQueue(new NoAction(),percepts);
                return;
                //return new NoAction();
            }
             */
        //System.out.println(angleToIntruder/count);
        if(angleToIntruder/count>15){
            if(Math.random()<0.2){
                System.out.println("yelled");
                addActionToQueue(new Yell(),percepts);
                return;
            }
            if (angleToIntruder/count<=percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
                addActionToQueue(new Rotate(Angle.fromDegrees(angleToIntruder/count)),percepts);
            }else{
                addActionToQueue(new Rotate(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle()),percepts);
            }
            return;
            //return new Rotate(Angle.fromDegrees(angleToIntruder/count));
        }else{
            if (distanceToIntruder/count<=percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()* getSpeedModifier(percepts)){
                addActionToQueue(new Move(new Distance(distanceToIntruder/count)),percepts);
                System.out.println("poep");
            }
            else{
                addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()* getSpeedModifier(percepts))),percepts);
            }
            //return new Move(new Distance(1));
            return;
        }

    }

    public void rotateToNoise(GuardPercepts guardPercepts){
        System.out.println("rotated to noise");
        Set<SoundPercept> sound = guardPercepts.getSounds().getAll();
        double soundDirectionDegrees = 0;
        int count =0;
        for (SoundPercept s : sound){
            soundDirectionDegrees = soundDirectionDegrees +s.getDirection().getDegrees();
            count++;
        }
       // System.out.println(soundDirectionDegrees);
        double soundDirectionDegreesNormalized = soundDirectionDegrees/count;
        //normalize so if rotation is 358 make it -2 since that is allowed by game controller
        if (soundDirectionDegrees/count>180){
            soundDirectionDegreesNormalized = soundDirectionDegreesNormalized - 360;
        }
        //System.out.println(count);
        //System.out.println(soundDirectionDegreesNormalized);
        //if the rotation is too much for one turn
        if (Math.abs(soundDirectionDegreesNormalized)>guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
            if (soundDirectionDegreesNormalized>0){
                while (soundDirectionDegreesNormalized>0){
                    if (soundDirectionDegreesNormalized>=guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
                        addActionToQueue(new Rotate(guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle()),guardPercepts);
                        soundDirectionDegreesNormalized = soundDirectionDegreesNormalized-guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();
                    }else{
                        addActionToQueue(new Rotate(Angle.fromDegrees(soundDirectionDegreesNormalized)),guardPercepts);
                        soundDirectionDegreesNormalized=0;
                    }
                }
            }
            if (soundDirectionDegreesNormalized<=0){
                while (soundDirectionDegreesNormalized<0){
                    if (Math.abs(soundDirectionDegreesNormalized)>=guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
                        addActionToQueue(new Rotate(Angle.fromDegrees(guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()*-1)),guardPercepts);
                        soundDirectionDegreesNormalized = soundDirectionDegreesNormalized+guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();
                    }else{
                        addActionToQueue(new Rotate(Angle.fromDegrees(soundDirectionDegreesNormalized)),guardPercepts);
                        soundDirectionDegreesNormalized=0;
                    }
                }

            }
        }else{
            addActionToQueue(new Rotate(Angle.fromDegrees(soundDirectionDegrees/count)),guardPercepts);
        }
    }

    public void rotateToYell(GuardPercepts guardPercepts){
        System.out.println("rotated to yell");
        Set<SoundPercept> sound = guardPercepts.getSounds().getAll();
        double soundDirectionDegrees = 0;
        int count =0;
        for (SoundPercept s : sound){
            if (s.getType()== SoundPerceptType.Yell){
                soundDirectionDegrees = soundDirectionDegrees +s.getDirection().getDegrees();
                count++;
            }
        }

//        System.out.println(soundDirectionDegrees);
        double soundDirectionDegreesNormalized = soundDirectionDegrees/count;

        if (Math.abs(soundDirectionDegreesNormalized)>guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
            if (soundDirectionDegreesNormalized>0){
                while (soundDirectionDegreesNormalized>0){
                    if (soundDirectionDegreesNormalized>=guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
                        addActionToQueue(new Rotate(guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle()),guardPercepts);
                        soundDirectionDegreesNormalized = soundDirectionDegreesNormalized-guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();
                    }else{
                        addActionToQueue(new Rotate(Angle.fromDegrees(soundDirectionDegreesNormalized)),guardPercepts);
                        soundDirectionDegreesNormalized=0;
                    }
                }
            }
            if (soundDirectionDegreesNormalized<=0){
                while (soundDirectionDegreesNormalized<0){
                    if (Math.abs(soundDirectionDegreesNormalized)>=guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()){
                        addActionToQueue(new Rotate(Angle.fromDegrees(guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()*-1)),guardPercepts);
                        soundDirectionDegreesNormalized = soundDirectionDegreesNormalized+guardPercepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees();
                    }else{
                        addActionToQueue(new Rotate(Angle.fromDegrees(soundDirectionDegreesNormalized)),guardPercepts);
                        soundDirectionDegreesNormalized=0;
                    }
                }

            }
        }else{
            addActionToQueue(new Rotate(Angle.fromDegrees(soundDirectionDegrees/count)),guardPercepts);
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


    /**
     * return how much an agent needs to rotate to face an object
     * @param agent
     * @param object
     * @return
     */
    public double rotateTo(AgentController agent, ObjectPercept object) {
        double angle = Math.atan2(agent.getPosition().getY() - object.getPoint().getY(), agent.getPosition().getX() - object.getPoint().getX());
        angle = angle-Math.PI/2;
        System.out.println(angle);

        if (angle > Math.PI)
            angle = 2*Math.PI-angle;

        else if (angle < -Math.PI)
            angle = -2*Math.PI-angle;


        return angle;
    }






}
