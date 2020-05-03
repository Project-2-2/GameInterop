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
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.ScenarioGuardPercepts;

import java.util.*;

public class GuardExplorer implements Guard {

    private Deque<GuardAction> actionQueue;



    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        if (actionQueue.isEmpty())
            explore(percepts);

        return actionQueue.removeFirst();

    }

    public void addActionToQueue(GuardAction action, GuardPercepts percepts) {
        double maxMoveRange = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
        Angle maxRotationAngle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle();

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

            }
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
            actionQueue.add(action);

    }

    public void explore(GuardPercepts percepts){
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
                    addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts))), percepts);
                }
            }
            //System.out.println("biem");
            //System.out.println(angleToWallsDegrees);
            System.out.println(angleToWallsDegrees/count);
            if(angleToWallsDegrees!=0){
                addActionToQueue(new Rotate(Angle.fromDegrees(angleToWallsDegrees/count)), percepts);
            }
            //System.out.println(vision.size());
            //return new Rotate(Angle.fromDegrees(angleToWallsDegrees/count));
        }

        if (!percepts.wasLastActionExecuted()){
            System.out.println("kak");
            addActionToQueue(new Rotate(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle()), percepts);
        }
        addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts))), percepts);
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
