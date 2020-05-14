package Group5.Agent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

import Group5.GameController.AgentController;
import Group5.GameController.MapInfo;
import Group5.GameController.Vision;
import Interop.Action.Action;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Action.Sprint;
import Interop.Action.Yell;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;
import Interop.Percept.Scenario.ScenarioIntruderPercepts;


import java.util.*;


public class IntruderAgent implements Interop.Agent.Intruder {
    private Deque<IntruderAction> actionQueue;
    private ObjectPercept state;
    private QLearning intruderQL;
    private static final int NUMBER_OF_POSSIBLE_ACTIONS = 7;
    float gamma = 0.92f;
    float epsilon = 0.2f;
    float alpha = 0.1f;
    
    public IntruderAgent(){
        this.actionQueue = new LinkedList<>();
        intruderQL = new QLearning(gamma, epsilon, alpha,
                NUMBER_OF_POSSIBLE_ACTIONS, ObjectPerceptType.class.getEnumConstants().length);
    }
    
    public enum Actions {
        RotateTowards(0), MoveTowards(1),
        NoAction(2), MoveStraight(3),
        RandomRotate(4), RandomMove(5),
        Sprint(6);
    
        private int value;
        Actions(int value) {
            this.value = value;
        }
    }
    
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        explore(percepts);
        return actionQueue.pop();
        
    }
    
    protected Rotate randomRotate(){
        Random rd = new Random();
        return new Rotate(Angle.fromDegrees(1 + (359) * rd.nextDouble()));
    }
    
    protected Rotate randomWalk(){
        Random rd = new Random();
        return new Rotate(Angle.fromDegrees(1 + (359) * rd.nextDouble()));
    }
    
    protected Rotate doNothing(){
        return new Rotate(Angle.fromDegrees(0));
    }
    
    protected Move moveStraight(){
        return new Move(new Distance(1));
    }
    
    protected Sprint sprintTowards(IntruderPercepts intruderPercepts){
        return new Sprint(intruderPercepts.getScenarioIntruderPercepts().getMaxSprintDistanceIntruder());
    }
    
    protected Rotate rotateTowards(Point point) {
        return new Rotate(Angle.fromRadians(point.getClockDirection().getRadians()));
    }
    
    protected Move walkTowards(Point point) {
        return new Move(new Distance(point.getDistanceFromOrigin().getValue()));
    }
    
    protected ObjectPercept getVisionState(Set<ObjectPercept> objectsInVision, IntruderPercepts intruderPercepts){
        List<ObjectPercept> visionObjects = new ArrayList<>(objectsInVision);
    
        int n = visionObjects.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                double tempDistance1 = intruderPercepts.getVision().getFieldOfView().getRange().getValue()-visionObjects.get(j).getPoint().getDistanceFromOrigin().getValue();
                double tempDistance2 = intruderPercepts.getVision().getFieldOfView().getRange().getValue()-visionObjects.get(j+1).getPoint().getDistanceFromOrigin().getValue();
                if (tempDistance1 > tempDistance2) {
                    ObjectPercept temp = visionObjects.get(j);
                    visionObjects.set(j, visionObjects.get(j + 1));
                    visionObjects.set(j + 1, temp);
                }
            
            }
        }
        return visionObjects.get(0);
    }
    
    public void explore(IntruderPercepts intruderPercepts) {
    
        Set<ObjectPercept> vision = intruderPercepts.getVision().getObjects().getAll();
        ArrayList<ObjectPerceptType> visionPerceptTypes = new ArrayList<>();
    
        intruderPercepts.getScenarioIntruderPercepts().getScenarioPercepts().getMaxRotationAngle();
        for (ObjectPercept e : vision) {
            visionPerceptTypes.add(e.getType());
        }
    
        if (!visionPerceptTypes.isEmpty() && intruderPercepts.wasLastActionExecuted()) {
            state = this.getVisionState(vision, intruderPercepts);
            int maxValueAction = intruderQL.getMaxValueAction(state.getType());
            for (IntruderAgent.Actions currAction : IntruderAgent.Actions.class.getEnumConstants()) {
                if (currAction.value == maxValueAction) {
                    if (currAction.value == 0) {
                        actionQueue.add(rotateTowards(state.getPoint()));
                        float reward = getReward(state);
                        intruderQL.updateQTable(state.getType(), currAction.value, reward);
//                        System.out.println(currAction);
                    } else if (currAction.value == 1) {
                        actionQueue.add(walkTowards(state.getPoint()));
                        float reward = getReward(state);
                        intruderQL.updateQTable(state.getType(), currAction.value, reward);
//                        System.out.println(currAction);
                    } else if (currAction.value == 2) {
                        actionQueue.add(doNothing());
                        float reward = getReward(state);
                        intruderQL.updateQTable(state.getType(), currAction.value, reward);
//                        System.out.println(currAction);
                    } else if (currAction.value == 3) {
                        actionQueue.add(moveStraight());
                        float reward = getReward(state);
                        intruderQL.updateQTable(state.getType(), currAction.value, reward);
//                        System.out.println(currAction);
                    } else if (currAction.value == 4) {
                        actionQueue.add(randomRotate());
                        float reward = getReward(state);
                        intruderQL.updateQTable(state.getType(), currAction.value, reward);
//                        System.out.println(currAction);
                    } else if (currAction.value == 5) {
                        actionQueue.add(randomWalk());
                        float reward = getReward(state);
                        intruderQL.updateQTable(state.getType(), currAction.value, reward);
//                        System.out.println(currAction);
                    } else if (currAction.value == 6) {
                        actionQueue.add(sprintTowards(intruderPercepts));
                        float reward = getReward(state);
                        intruderQL.updateQTable(state.getType(), currAction.value, reward);
                        //                        System.out.println(currAction);
                    }
                }
            }
            intruderQL.writeTableToFile();
        }
    
        else if (!visionPerceptTypes.isEmpty() && !intruderPercepts.wasLastActionExecuted()) {
            actionQueue.add(randomRotate());
            float reward = -2f;
            intruderQL.updateQTable(state.getType(), 4, reward);
        }
        
        else {
            double chance = Math.random();
            double randomThreshold = 0.001;
            if (chance < randomThreshold) {
                actionQueue.add(randomRotate());
                float reward = -1f;
                intruderQL.updateQTable(ObjectPerceptType.EmptySpace, Actions.RandomRotate.value, reward);
            }
            
            else {
//                System.out.println(" MOVE STRAIGHT");
                actionQueue.add(moveStraight());
                float reward = -1f;
                intruderQL.updateQTable(ObjectPerceptType.EmptySpace, Actions.MoveStraight.value, reward);
            }
            intruderQL.writeTableToFile();
        }
    }
    
    protected float getReward(ObjectPercept state){
        float reward = -1f;

    // Assign some basic reward for now, need to fix this later
        if (state.getType() == ObjectPerceptType.TargetArea) {
            reward = 0f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Guard) {
            reward = -3f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Intruder) {
            reward = -0.8f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Door) {
            reward = -1f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Window) {
            reward = -1f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Teleport){
            reward = -1f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.SentryTower){
            reward = -1f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.ShadedArea){
            reward = -1f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.EmptySpace){
            reward = -1f;
            return reward;
        }
        else{
            reward = -1f;
            return reward;
        }
    }
}
