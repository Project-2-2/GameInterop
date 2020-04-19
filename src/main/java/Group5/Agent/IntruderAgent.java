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
import java.util.*;


public class IntruderAgent implements Interop.Agent.Intruder {
    private Deque<IntruderAction> actionQueue;
    private ObjectPercept state;
    private QLearning intruderQL;
    private float finalReward = -1f;
    private static final int NUMBER_OF_POSSIBLE_ACTIONS = 3;
    float gamma = 0.92f;
    float epsilon = 0.5f;
    float alpha = 0.1f;
    
    public IntruderAgent(AgentController agentController){
        this.actionQueue = new LinkedList<>();
        intruderQL = new QLearning(gamma, epsilon, alpha, agentController,
                NUMBER_OF_POSSIBLE_ACTIONS, ObjectPerceptType.class.getEnumConstants().length);
    }
    
    public enum Actions {
        Rotate(0), Move(1),
        NoAction(2), Sprint(3), Yell(4);
    
        private int value;
        Actions(int value) {
            this.value = value;
        }
    }
    
    
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        
        return null;
    }
    
    public IntruderAction getAction(AgentController agentController, ObjectPercepts objectsInVision){
        explore(agentController, objectsInVision);
        if (actionQueue.isEmpty()) return new Move(new Distance(1));
        else return actionQueue.pop();
    }
    
    // Random rotation
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
    
    protected Sprint sprintTowards(Point point, AgentController agentController){
        return new Sprint(new Distance(new Point(0, 0), agentController.getRelativePosition(point)));
    }
    
    protected Rotate rotateTowards(Point point, AgentController agentController) {
        return new Rotate(agentController.getRelativeAngle(new Point(0, 0),
                agentController.getRelativePosition(point)));
    }
    
    protected Move walkTowards(Point point, AgentController agentController) {
        return new Move(new Distance(new Point(0, 0), agentController.getRelativePosition(point)));
    }
    
    protected ObjectPercept getVisionState(ObjectPercepts objectsInVision, AgentController agentController){
        ArrayList<ObjectPercept> visionObjects = new ArrayList<>();
        visionObjects.addAll(objectsInVision.getAll());
        Vision.bubbleSort(visionObjects, agentController);
        
        // Just deal with the fist object in vision for now
        for (ObjectPercept objectPercept : visionObjects) {
            return objectPercept;
        }
        return null;
    }
    
    public void explore(AgentController agentController, ObjectPercepts objectsInVision) {
    
        if (!objectsInVision.getAll().isEmpty()) {
            state = this.getVisionState(objectsInVision, agentController);
            int maxValueAction = intruderQL.getMaxValueAction(state.getType());
            for (IntruderAgent.Actions currAction : IntruderAgent.Actions.class.getEnumConstants()) {
                if (currAction.value == maxValueAction) {
                    if (currAction.value == 0) {
                        actionQueue.add(randomRotate());
                        float reward = getReward(objectsInVision, agentController);
                        finalReward += reward;
                        intruderQL.updateQTable(state.getType(), objectsInVision, reward);
                    }
                    else if (currAction.value == 1) {
                        actionQueue.add(walkTowards(state.getPoint(), agentController));
                        float reward = getReward(objectsInVision, agentController);
                        finalReward += reward;
                        intruderQL.updateQTable(state.getType(), objectsInVision, reward);
                    }
                    else if (currAction.value == 2) {
                        actionQueue.add(doNothing());
                        float reward = getReward(objectsInVision, agentController);
                        finalReward += reward;
                        intruderQL.updateQTable(state.getType(), objectsInVision, reward);
                    }
                }
                float reward = getReward(objectsInVision, agentController);
                finalReward += reward;
                intruderQL.updateQTable(state.getType(), objectsInVision, reward);
            }
            intruderQL.writeTableToFile();
        }
    }
    
    /**
     * Calculate reward for Intruder agent and pass it to the Q-Learning algorithm
     * @param
     * @return
     */
    protected float getReward(ObjectPercepts objectsInVision, AgentController agentController){
        float reward = -1f;
        ArrayList<ObjectPercept> visionObjects = new ArrayList<>();
        visionObjects.addAll(objectsInVision.getAll());
        Vision.bubbleSort(visionObjects, agentController);
        
        // Assign some basic reward for now, need to fix this later
        for (ObjectPercept objectPercept : visionObjects) {
            if (objectPercept.getType() == ObjectPerceptType.TargetArea) {
                reward = 1f;
                return reward;
            }
            else if (objectPercept.getType() == ObjectPerceptType.Door) {
                reward = 0.1f;
                return reward;
            }
            else if (objectPercept.getType() == ObjectPerceptType.Window) {
                reward = 0.1f;
                return reward;
            }
            else if (objectPercept.getType() == ObjectPerceptType.Teleport){
                reward = 0.1f;
                return reward;
            }
            else if (objectPercept.getType() == ObjectPerceptType.ShadedArea){
                reward = 0.1f;
                return reward;
            }
            else if (objectPercept.getType() == ObjectPerceptType.EmptySpace){
                reward = 0f;
                return reward;
            }
            else{
                reward = -1f;
                return reward;
            }
        }
        return reward;
    }
    
}
