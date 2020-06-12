package Group5.Agent.Guard;

import Group5.GameController.AgentController;
import Group5.GameController.Smell;
import Group5.GameController.Vision;
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
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.ScenarioGuardPercepts;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.*;
import java.util.concurrent.Callable;

public class GuardExplorer implements Guard {

    private Queue<GuardAction> actionQueue = new LinkedList<>();
    private int enteredSentryTower; //0 if didn't entered a sentry tower the last 30 turns

    private Angle lastDirectionIntruder;
    private Distance lastDistanceToIntruder;
    private boolean rotateToIntruder;
    private int lastTimeSawIntruder;
    private int droppedPheromone;
    private int movedSomewhere;

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        //return explore(percepts);
        //if queue is empty otherwise do actions inside queue
//        if (!percepts.wasLastActionExecuted()){
//            actionQueue.clear();
//        }
        if (actionQueue.size()<=0)
            explore(percepts);

        if (this.enteredSentryTower != 0)
            this.enteredSentryTower --;

        if (droppedPheromone != 0)
            droppedPheromone --;

        if (lastTimeSawIntruder>0){
            lastTimeSawIntruder--;

        }else{
            rotateToIntruder = false;
        }

        //System.out.println(actionQueue.size());
        //System.out.println("yes");
        if (actionQueue.size() == 0){
            return new Move(new Distance(1));
        }

        return actionQueue.poll();

    }

    public void addActionToQueue(GuardAction action, GuardPercepts percepts) {
        double maxMoveRange = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()*getSpeedModifier(percepts);
        Angle maxRotationAngle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle();

        if (action instanceof Rotate ) {
            double rotateValue = ((Rotate) action).getAngle().getDegrees();
            if (rotateValue > maxRotationAngle.getDegrees()) {
                if (rotateValue>180){
                    rotateValue = rotateValue-360;
                }
                while (rotateValue > 0) {
                    if (rotateValue > maxRotationAngle.getDegrees()) {
                        actionQueue.add(new Rotate(maxRotationAngle));
                        rotateValue -= maxRotationAngle.getDegrees();
                    }else {
                        actionQueue.add(new Rotate(Angle.fromDegrees(rotateValue)));
                        rotateValue = 0;
                    }
                }
                while (rotateValue < 0) {
                    if (Math.abs(rotateValue) > maxRotationAngle.getDegrees()) {
                        actionQueue.add(new Rotate(Angle.fromRadians(maxRotationAngle.getRadians()*-1)));

                        rotateValue += maxRotationAngle.getDegrees();
                    }else {
                        actionQueue.add(new Rotate(Angle.fromDegrees(rotateValue)));
                        rotateValue = 0;
                    }
                }

            }else
                actionQueue.add(action);
        }else if (action instanceof Move) {
            double distance = ((Move) action).getDistance().getValue();
            if (Math.abs(distance) > maxMoveRange) {
                while (distance > 0) {
                    if (distance > maxMoveRange) {
                        actionQueue.add(new Move(new Distance(maxMoveRange)));
                        distance -= maxMoveRange;
                    }else {
                        actionQueue.add(new Move(new Distance(distance)));
                        distance = 0;
                    }
                }
            }else
                actionQueue.add(action);

        }else
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

        if (!percepts.wasLastActionExecuted()){
            moveParallelToWall(percepts, vision);
            return;
        }

//        if (!percepts.wasLastActionExecuted()){
//            addActionToQueue(new Rotate(Angle.fromDegrees(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees())), percepts);
//            addActionToQueue(new Rotate(Angle.fromDegrees(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees())), percepts);
//            return;
//        }

        if (visionPerceptTypes.contains(ObjectPerceptType.Intruder)) {
            actionQueue.clear();
            followIntruder(percepts,vision);
            return;
        }
        if (soundPerceptTypes.contains(SoundPerceptType.Yell)&&Math.random()<=0.95){
            rotateToYell(percepts);
            return;
        }

        if (visionPerceptTypes.contains(ObjectPerceptType.SentryTower)) {
            towerInViewRange(percepts);
            this.enteredSentryTower = 500;
            return;
        }

        //higher probability to check sound when it saw an intruder recently
        if ((soundPerceptTypes.size()>0&&Math.random()<=0.2)||(soundPerceptTypes.size()>0&&lastTimeSawIntruder>0&&Math.random()<=0.5)){
            rotateToNoise(percepts);
//            System.out.println("check");
            return;
        }

        if (visionPerceptTypes.contains(ObjectPerceptType.Teleport)) {
            goToSomewhere(percepts,vision,ObjectPerceptType.Teleport);
            return;
        }



        if (visionPerceptTypes.contains(ObjectPerceptType.Door)) {
            goToSomewhere(percepts,vision, ObjectPerceptType.Door);
            return;
        }

        if (visionPerceptTypes.contains(ObjectPerceptType.Window)) {
            goToSomewhere(percepts,vision,ObjectPerceptType.Window);
            return;
        }



        if (lastTimeSawIntruder>0){
            //System.out.println("remembered intruder");
            //System.out.println(lastDirectionIntruder.getDegrees());
            percepts.getVision().getFieldOfView();
            if (!rotateToIntruder){
                addActionToQueue(new Rotate(lastDirectionIntruder),percepts);
                rotateToIntruder = true;
                return;
            }
            addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()*getSpeedModifier(percepts))),percepts);
            return;
        }

        if(percepts.getAreaPercepts().isInDoor() && droppedPheromone == 0) {
//            System.out.println("door: drop pheromone type 2");
            dropPheromone(percepts,SmellPerceptType.Pheromone2);
            this.droppedPheromone=500;
            return;
        }

        if(percepts.getAreaPercepts().isInWindow() && droppedPheromone == 0) {
//            System.out.println("window: drop pheromone type 2");
            dropPheromone(percepts,SmellPerceptType.Pheromone2);
            this.droppedPheromone=500;
            return;
        }

        if(percepts.getAreaPercepts().isJustTeleported() && droppedPheromone == 0) {
//            System.out.println("teleported: drop pheromone type 2");
            dropPheromone(percepts,SmellPerceptType.Pheromone2);
            this.droppedPheromone=500;
            return;
        }

        if (Math.random() <= 0.1 && droppedPheromone == 0) {
            dropPheromone(percepts, SmellPerceptType.Pheromone1);
            this.droppedPheromone = 500;
            return;
        }

/*
        Set<SmellPercept> pheromone1 =  smellPheromone(percepts, SmellPerceptType.Pheromone1);
        if (!pheromone1.isEmpty()) {
            System.out.println("leave epxlored zone ");
            GuardAction action = leaveExploredZone(percepts);
            if (action != null) {
                addActionToQueue(action, percepts);
                return;
            }
        }

 */

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
                    //addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts))), percepts);
                    //return new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts)));
                    //return;
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
//            System.out.println("kak");

            Angle randomAngle = Angle.fromDegrees(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees()*Math.random());
            addActionToQueue(new Rotate(randomAngle), percepts);
            return;
            //return new Rotate(randomAngle);
        }
    }

    public void goToSomewhere(GuardPercepts percepts, Set<ObjectPercept> vision, ObjectPerceptType objPType){
        double angleToDoorsDegrees=0;
        int count=0;
        int atNegativeAngle=0;
        double distance = 0;
        for(ObjectPercept e : vision) {
            if (e.getType() == objPType) {
                distance+=Math.sqrt(e.getPoint().getX()*e.getPoint().getX() + e.getPoint().getY()*e.getPoint().getY());
             if (e.getPoint().getClockDirection().getDegrees() > 180) {
//                 System.out.println("Angle: " + (e.getPoint().getClockDirection().getDegrees() - 360));
                 angleToDoorsDegrees += e.getPoint().getClockDirection().getDegrees() - 360;
                 atNegativeAngle++;
             }
                else {
//                 System.out.println("Angle: " + e.getPoint().getClockDirection().getDegrees());
                 angleToDoorsDegrees += e.getPoint().getClockDirection().getDegrees();
             }
                count++;
            }
        }
        if(this.movedSomewhere > 5){
            Angle randomAngle = Angle.fromDegrees(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees() * Math.random());
            addActionToQueue(new Rotate(randomAngle), percepts);
            this.movedSomewhere = 0;
        }
        else if(Math.abs(angleToDoorsDegrees/count)<5 && (distance/count)<8) {
            this.movedSomewhere++;
//            System.out.println("Average distance: " + distance/count);
//            System.out.println("Number of percepts: " + vision.size());
//            System.out.println("Number of door percepts: " + count);
//            System.out.println("Angle towards door: " + angleToDoorsDegrees);
//            System.out.println("Average Angle: " + angleToDoorsDegrees/count);
            addActionToQueue(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts))), percepts);
//            System.out.println("MOVING TO REACH DOOR");
        }
        else if(angleToDoorsDegrees/count>5 && distance/count<8){
            this.movedSomewhere++;
//            System.out.println("Average distance: " + distance/count);
//            System.out.println("Number of percepts: " + vision.size());
//            System.out.println("Number of door percepts: " + count);
//            System.out.println("Angle towards door: " + angleToDoorsDegrees);
//            System.out.println("Average Angle: " + angleToDoorsDegrees/count);
            addActionToQueue(new Move(new Distance(1)), percepts);
            addActionToQueue(new Rotate(Angle.fromDegrees(-angleToDoorsDegrees / count)), percepts);

//            System.out.println("ROTATING TO FACE DOOR");
        }
        else {
//            System.out.println("can't enter door, try later");
            Angle randomAngle = Angle.fromDegrees(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getDegrees() * Math.random());
            addActionToQueue(new Rotate(randomAngle), percepts);
        }
    }

    public void goToWindow(GuardPercepts percepts, Set<ObjectPercept> vision){

    }
    public void goToTeleport(GuardPercepts percepts, Set<ObjectPercept> vision){

    }

    public ArrayList<ObjectPercept> seeIntruder(GuardPercepts percepts, Set<ObjectPercept> vision){
        ArrayList<ObjectPercept> intruders = new ArrayList<>();
        for (ObjectPercept obj: vision) {
            if (obj.getType().toString().equals("Intruder")) {
                intruders.add(obj);
            }
        }
        return intruders;

    }

    public void followIntruder(GuardPercepts percepts, Set<ObjectPercept> vision) {
        // System.out.println("found intruder");
        double angleToIntruder = 0;
        int count = 0;
        double distanceToIntruder = 0;
        ArrayList<ObjectPercept> intruderPercept = seeIntruder(percepts, vision);
        Direction intruderMovement = null;

        for (int i = 0; i < intruderPercept.size(); i++) {
            ObjectPercept e = intruderPercept.get(i);
            distanceToIntruder += Math.sqrt(e.getPoint().getX() * e.getPoint().getX() + e.getPoint().getY() * e.getPoint().getY());
            if (e.getPoint().getClockDirection().getDegrees() > 180) {
//                System.out.println("Angle: " + (e.getPoint().getClockDirection().getDegrees() - 360));
                angleToIntruder += e.getPoint().getClockDirection().getDegrees() - 360;
            } else {
//                System.out.println("Angle: " + e.getPoint().getClockDirection().getDegrees());
                angleToIntruder += e.getPoint().getClockDirection().getDegrees();
            }
            count++;
        }

//            if (e != null) {
//                distanceToIntruder = distanceToIntruder + Math.abs(percepts.getVision().getFieldOfView().getRange().getValue() - e.getPoint().getDistanceFromOrigin().getValue());
//                if (intruderMovement==null){
//                    intruderMovement = Direction.fromClockAngle(e.getPoint());
//                }else{
//                    Direction newDirection = Direction.fromClockAngle(e.getPoint());
//                    double movement = newDirection.getDegrees()-intruderMovement.getDegrees();
//                    while (movement<0){
//                        movement = movement+360;
//
//                    }
//                    while (movement>360){
//                        movement = movement-360;
//                    }
//                    if (movement!=360){
//                        intruderMovement = Direction.fromDegrees(movement);
//                    }else{
//                        intruderMovement = Direction.fromDegrees(0);
//                    }
//
//                }
//
//                if (Angle.fromDegrees(0).getDistance(e.getPoint().getClockDirection()).getDegrees() > 180) {
//                    angleToIntruder = angleToIntruder + e.getPoint().getClockDirection().getDegrees() - 360;
//                } else {
//                    angleToIntruder = angleToIntruder + Angle.fromDegrees(0).getDistance(e.getPoint().getClockDirection()).getDegrees();
//                }
//                count++;
//            }


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
        lastDirectionIntruder = Angle.fromDegrees(-angleToIntruder / count);
        //lastDirectionIntruder = intruderMovement;
        //System.out.println(intruderMovement.getDegrees());
        lastDistanceToIntruder = new Distance(distanceToIntruder);

        lastTimeSawIntruder = 0;

        if (Math.abs(angleToIntruder / count) > 15) {
            if (Math.random() < 0.2) {
//                System.out.println("yelled");
                addActionToQueue(new Yell(), percepts);
                return;
            }
            addActionToQueue(new Rotate(Angle.fromDegrees(-angleToIntruder / count)), percepts);
            return;
            //return new Rotate(Angle.fromDegrees(angleToIntruder/count));
        } else {
//            System.out.println("distance");
//            System.out.println(distanceToIntruder);
            addActionToQueue(new Move(new Distance(distanceToIntruder / count)), percepts);
            return;
//                System.out.println("poep");
        }
        //return new Move(new Distance(1));
    }

    public void rotateToNoise(GuardPercepts guardPercepts){
//        System.out.println("rotated to noise");
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
//        System.out.println("rotated to yell");
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

    private double getSpeedModifier(GuardPercepts guardPercepts) {
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
    public double getDirection(AgentController agent, ObjectPercept object) {
        double angle = Math.atan2(agent.getPosition().getY() - object.getPoint().getY(), agent.getPosition().getX() - object.getPoint().getX());
        angle = angle-Math.PI/2;

        if (angle > Math.PI)
            angle = 2*Math.PI-angle;

        else if (angle < -Math.PI)
            angle = -2*Math.PI-angle;

        return angle;
    }

    /**
     * if sees a sentryTower, he goes on it and look around
     * @param p
     */
    private void towerInViewRange(GuardPercepts p) {
        ObjectPercept sentryTower = null;
        for (ObjectPercept obj: p.getVision().getObjects().getAll()) {
            if (obj.getType() == ObjectPerceptType.SentryTower) {
                sentryTower = obj;
            }
        }

        if (Angle.fromDegrees(0).getDistance(sentryTower.getPoint().getClockDirection()).getDegrees() > 180)
            addActionToQueue(new Rotate(Angle.fromDegrees(Angle.fromDegrees(0).getDistance(sentryTower.getPoint().getClockDirection()).getDegrees() - 360)), p);

        else
            addActionToQueue(new Rotate(Angle.fromDegrees(Angle.fromDegrees(0).getDistance(sentryTower.getPoint().getClockDirection()).getDegrees())), p);


        Distance dist = new Distance(p.getVision().getFieldOfView().getRange().getValue());
        addActionToQueue(new Move(dist), p);
        lookInAllDirection(p);
    }

    /**
     * Makes the agent look around himself
     * @param percepts
     */
    private void lookInAllDirection(GuardPercepts percepts) {
        for (int i = 0; i < 4; i++)
            addActionToQueue(new Rotate(Angle.fromDegrees(45)), percepts);
    }

    /**
     * Drop pheromone if does no hear sound, does not smell another pheromone and does not see intruder
     * @param p
     */
    private void dropPheromone(GuardPercepts p, SmellPerceptType type) {
        //if (!hearSound(p) && smellPheromone(p) != null && seeIntruder(p, p.getVision().getObjects().getAll())==null)
            addActionToQueue(new DropPheromone(type), p);
    }

    private boolean hearSound(GuardPercepts percepts) {
        if (percepts.getSounds().getAll().isEmpty())
            return false;
        else
            return true;
    }

    private Set<SmellPercept> smellPheromone(GuardPercepts percepts) {
        if (percepts.getSmells().getAll().isEmpty())
            return null;
        else
            return percepts.getSmells().getAll();
    }

    /**
     * @param percepts
     * @param type
     * @return Set of pheromone of Type type
     */
    private Set<SmellPercept> smellPheromone(GuardPercepts percepts, SmellPerceptType type) {
        Set<SmellPercept> toReturn = new HashSet<>();
        for (SmellPercept s: percepts.getSmells().getAll()) {
            if (s.getType() == type) {
                toReturn.add(s);
            }
        }
        return toReturn;
    }


    /**
     * If smells a pheromone where he wanted to go, he changes directions
     */
    private GuardAction leaveExploredZone(GuardPercepts p) {
        Set<SmellPercept> smell = smellPheromone(p);
        if (smell != null) {
            for (SmellPercept sp : smell)
                if (sp.getType().toString().equals("Pheromone1"))
                    return new Rotate(Angle.fromRadians(Math.PI / 2));
        }

        return null;
    }


}
