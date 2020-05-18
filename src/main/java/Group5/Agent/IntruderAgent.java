package Group5.Agent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

import Group5.GameController.AgentController;
import Group5.GameController.MapInfo;
import Group5.GameController.Vision;
import Interop.Action.Action;
import Interop.Action.DropPheromone;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Action.Sprint;
import Interop.Action.Yell;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;
import Interop.Percept.Scenario.ScenarioIntruderPercepts;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.*;


public class IntruderAgent implements Interop.Agent.Intruder {
    private Deque<IntruderAction> actionQueue;
    private ObjectPercept visionState;
    private SmellPercept smellState;
    private SoundPercept soundState;
    
    // Add primitive memory system checking for already seen rounded X and Y coordinates
    private Set<List> seenObjects;
    private QLearning intruderQL;
    private static final int NUMBER_OF_POSSIBLE_ACTIONS = 12;
    private static final int NUMBER_OF_POSSIBLE_SINGLE_STATES = 17;
    
    float gamma = 0.92f;
    float epsilon = 0.1f;
    float alpha = 0.1f;
    
    public IntruderAgent(){
        this.actionQueue = new LinkedList<>();
        this.seenObjects = new HashSet<>();
        intruderQL = new QLearning(gamma, epsilon, alpha,
                NUMBER_OF_POSSIBLE_ACTIONS, NUMBER_OF_POSSIBLE_SINGLE_STATES);
    }
    
    public enum Actions {
        RotateTowards(0), MoveTowards(1),
        NoAction(2), MoveStraight(3),
        RandomRotate(4), RandomMove(5),
        Sprint(6), DropPheromone1(7),
        DropPheromone2(8), DropPheromone3(9),
        DropPheromone4(10), DropPheromone5(11);
    
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
    
    protected Move randomWalk(IntruderPercepts intruderPercepts){
        Random rd = new Random();
        return new Move(new Distance(intruderPercepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder().getValue() * rd.nextDouble()));
    }
    
    protected Rotate doNothing(){
        return new Rotate(Angle.fromDegrees(0));
    }
    
//    protected Move moveStraight(){
//        return new Move(new Distance(1));
//    }
    
    protected Move moveStraight(IntruderPercepts intruderPercepts){
        return new Move(intruderPercepts.getScenarioIntruderPercepts().getMaxMoveDistanceIntruder());
    }
    
    
    protected Sprint sprintTowards(IntruderPercepts intruderPercepts){
        return new Sprint(intruderPercepts.getScenarioIntruderPercepts().getMaxSprintDistanceIntruder());
    }
    
    protected Rotate rotateTowards(Point point) {
        return new Rotate(Angle.fromRadians(point.getClockDirection().getRadians()));
    }
    
    protected Rotate rotateTowards(double radians) {
        return new Rotate(Angle.fromRadians(radians));
    }
    
    protected Move walkTowards(Point point) {
        return new Move(new Distance(point.getDistanceFromOrigin().getValue()));
    }
    
    protected DropPheromone dropPheromone1(IntruderPercepts p) {
        return new DropPheromone(SmellPerceptType.Pheromone1);
    }
    protected DropPheromone dropPheromone2(IntruderPercepts p) {
        return new DropPheromone(SmellPerceptType.Pheromone2);
    }
    protected DropPheromone dropPheromone3(IntruderPercepts p) {
        return new DropPheromone(SmellPerceptType.Pheromone3);
    }
    protected DropPheromone dropPheromone4(IntruderPercepts p) {
        return new DropPheromone(SmellPerceptType.Pheromone4);
    }
    protected DropPheromone dropPheromone5(IntruderPercepts p) {
        return new DropPheromone(SmellPerceptType.Pheromone5);
    }
    
    private boolean hearSound(IntruderPercepts percepts) {
        if (percepts.getSounds().getAll().isEmpty())
            return false;
        else
            return true;
    }
    
    private boolean smellPheromone(IntruderPercepts percepts) {
        if (percepts.getSmells().getAll().isEmpty())
            return false;
        else
            return true;
    }
    
    protected ObjectPercept getVisionState(IntruderPercepts intruderPercepts){
        Set<ObjectPercept> vision = intruderPercepts.getVision().getObjects().getAll();
        ArrayList<ObjectPerceptType> visionPerceptTypes = new ArrayList<>();
        List<ObjectPercept> visionObjectsSorted = new ArrayList<>(vision);
        for (ObjectPercept e : vision) {
            visionPerceptTypes.add(e.getType());
        }
    
        if (visionPerceptTypes.contains(ObjectPerceptType.Guard)) {
            for (ObjectPercept s : vision) {
                if (s.getType() == ObjectPerceptType.Guard) {
                    return s;
                }
            }
        }
        else {
            int n = visionObjectsSorted.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    double tempDistance1 =
                            intruderPercepts.getVision().getFieldOfView().getRange().getValue() - visionObjectsSorted.get(j).getPoint().getDistanceFromOrigin().getValue();
                    double tempDistance2 =
                            intruderPercepts.getVision().getFieldOfView().getRange().getValue() - visionObjectsSorted
                                    .get(j + 1).getPoint().getDistanceFromOrigin().getValue();
                    if (tempDistance1 > tempDistance2) {
                        ObjectPercept temp = visionObjectsSorted.get(j);
                        visionObjectsSorted.set(j, visionObjectsSorted.get(j + 1));
                        visionObjectsSorted.set(j + 1, temp);
                    }
                }
            }
            if (!visionObjectsSorted.isEmpty())
                return visionObjectsSorted.get(0);
        }
        return null;
    }
    
    protected SoundPercept getSoundState(IntruderPercepts intruderPercepts){
        Set<SoundPercept> sounds = intruderPercepts.getSounds().getAll();
        ArrayList<SoundPerceptType> soundPerceptTypes = new ArrayList<>();
        for (SoundPercept s : sounds) {
            soundPerceptTypes.add((s.getType()));
        }
        
        if (soundPerceptTypes.contains(SoundPerceptType.Yell) &&
                (soundPerceptTypes.contains(SoundPerceptType.Noise) || !soundPerceptTypes.contains(SoundPerceptType.Noise))) {
            for (SoundPercept s : sounds) {
                if (s.getType() == SoundPerceptType.Yell) {
                    return s;
                }
            }
        }
        else if (sounds.size() > 0){
            for(SoundPercept sound: sounds) {
                return sound;
            }
        }
        return null;
    }
    
    protected SmellPercept getSmellState(IntruderPercepts intruderPercepts){
        List<SmellPercept> smells = new ArrayList<SmellPercept> (intruderPercepts.getSmells().getAll());
        
        if (!smells.isEmpty())
            return smells.get(0);
        
        return null;
    }
    
    public void explore(IntruderPercepts intruderPercepts) {
        
        this.soundState = this.getSoundState(intruderPercepts);
        this.visionState = this.getVisionState(intruderPercepts);
        this.smellState = this.getSmellState(intruderPercepts);
    
        if (this.visionState != null && intruderPercepts.wasLastActionExecuted() && this.soundState == null) {
            for (IntruderAgent.Actions currAction : IntruderAgent.Actions.class.getEnumConstants()) {
                int maxValueAction = intruderQL.getMaxValueAction(this.visionState.getType());
                if (currAction.value == maxValueAction) {
                    if (currAction.value == 0) {
                        actionQueue.add(rotateTowards(this.visionState.getPoint()));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 1) {
                        actionQueue.add(walkTowards(this.visionState.getPoint()));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 2) {
                        actionQueue.add(doNothing());
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 3) {
                        actionQueue.add(moveStraight(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 4) {
                        actionQueue.add(randomRotate());
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 5) {
                        actionQueue.add(randomWalk(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 6) {
                        actionQueue.add(sprintTowards(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 7) {
                        actionQueue.add(dropPheromone1(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 8) {
                        actionQueue.add(dropPheromone2(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 9) {
                        actionQueue.add(dropPheromone3(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 10) {
                        actionQueue.add(dropPheromone4(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    } else if (currAction.value == 11) {
                        actionQueue.add(dropPheromone5(intruderPercepts));
                        float reward = getReward(this.visionState);
                        
                        // Add rounded X and Y coordinates of Points to the seen objects
                        this.seenObjects.add(Arrays.asList(round(this.visionState.getPoint().getX(),2),
                                round(this.visionState.getPoint().getY(),2)));
                        intruderQL.updateQTable(this.visionState.getType(), currAction.value, reward);
                    }
                }
            }
            intruderQL.writeTableToFile();
        }
    
        else if (this.visionState != null && !intruderPercepts.wasLastActionExecuted()) {
            actionQueue.add(randomRotate());
            float reward = -2f;
            intruderQL.updateQTable(this.visionState.getType(), 4, reward);
        }
    
        if (intruderPercepts.wasLastActionExecuted() && this.soundState != null) {
            int maxValueAction = intruderQL.getMaxValueAction(this.soundState.getType());
            for (IntruderAgent.Actions currAction : IntruderAgent.Actions.class.getEnumConstants()) {
                if (currAction.value == maxValueAction) {
                    if (currAction.value == 0) {
                        actionQueue.add(rotateTowards(this.soundState.getDirection().getRadians()));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 1) {
                        actionQueue.add(moveStraight(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 2) {
                        actionQueue.add(doNothing());
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 3) {
                        actionQueue.add(moveStraight(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                        //                        System.out.println(currAction);
                    } else if (currAction.value == 4) {
                        actionQueue.add(randomRotate());
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 5) {
                        actionQueue.add(randomWalk(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 6) {
                        actionQueue.add(sprintTowards(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 7) {
                        actionQueue.add(dropPheromone1(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 8) {
                        actionQueue.add(dropPheromone2(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 9) {
                        actionQueue.add(dropPheromone3(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 10) {
                        actionQueue.add(dropPheromone4(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    } else if (currAction.value == 11) {
                        actionQueue.add(dropPheromone5(intruderPercepts));
                        float reward = getReward(this.soundState);
                        intruderQL.updateQTable(this.soundState.getType(), currAction.value, reward);
                    }
                }
            }
            intruderQL.writeTableToFile();
        }
    
        if (intruderPercepts.wasLastActionExecuted() && this.smellState != null) {
            int maxValueAction = intruderQL.getMaxValueAction(this.smellState.getType());
            for (IntruderAgent.Actions currAction : IntruderAgent.Actions.class.getEnumConstants()) {
                if (currAction.value == maxValueAction) {
                    if (currAction.value == 1) {
                        actionQueue.add(moveStraight(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 2) {
                        actionQueue.add(doNothing());
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 3) {
                        actionQueue.add(moveStraight(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 4) {
                        actionQueue.add(randomRotate());
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 5) {
                        actionQueue.add(randomWalk(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 6) {
                        actionQueue.add(sprintTowards(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 7) {
                        actionQueue.add(dropPheromone1(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 8) {
                        actionQueue.add(dropPheromone2(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 9) {
                        actionQueue.add(dropPheromone3(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 10) {
                        actionQueue.add(dropPheromone4(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    } else if (currAction.value == 11) {
                        actionQueue.add(dropPheromone5(intruderPercepts));
                        float reward = getReward(this.smellState);
                        intruderQL.updateQTable(this.smellState.getType(), currAction.value, reward);
                    }
                }
            }
            intruderQL.writeTableToFile();
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
                actionQueue.add(moveStraight(intruderPercepts));
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
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))){
                reward = 0f;
            }
            else{
                reward = 0f;
            }
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Guard) {
            reward = -3f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Intruder) {
            reward = -1f;
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Door) {
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))) {
                reward = -0.8f;
            }
            else{
                reward = -1f;
            }
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Window) {
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))) {
                reward = -0.8f;
            }
            else{
                reward = -1f;
            }
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Teleport){
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))) {
                reward = -0.8f;
            }
            else{
                reward = -1f;
            }
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.SentryTower){
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))) {
                reward = -0.8f;
            }
            else{
                reward = -1f;
            }
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.ShadedArea){
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))) {
                reward = -0.8f;
            }
            else{
                reward = -1f;
            }
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.EmptySpace){
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))) {
                reward = -0.8f;
            }
            else{
                reward = -1f;
            }
            return reward;
        }
        else if (state.getType() == ObjectPerceptType.Wall){
            if (!this.seenObjects.contains(Arrays.asList(round(state.getPoint().getX(),2),
                    round(state.getPoint().getY(),2)))) {
                reward = -0.8f;
            }
            else{
                reward = -1f;
            }
            return reward;
        }
        return reward;
    }
    
    protected float getReward(SoundPercept state){
        float reward = -1f;
        
        // Assign some basic reward for now, need to fix this later
        if (state.getType() == SoundPerceptType.Noise) {
            reward = -1f;
            return reward;
        }
        else{
            reward = -3f;
            return reward;
        }
    }
    
    protected float getReward(SmellPercept state){
        float reward = -1f;
        
        // Assign some basic reward for now, need to fix this later
        if (state.getType() == SmellPerceptType.Pheromone1) {
            reward = -1f;
            return reward;
        }
        else if (state.getType() ==  SmellPerceptType.Pheromone2){
            reward = -1f;
            return reward;
        }
        else if (state.getType() ==  SmellPerceptType.Pheromone3){
            reward = -1f;
            return reward;
        }
        else if (state.getType() ==  SmellPerceptType.Pheromone4){
            reward = -1f;
            return reward;
        }
        else if (state.getType() ==  SmellPerceptType.Pheromone5){
            reward = -1f;
            return reward;
        }
        return reward;
    }
    
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    private boolean seenCoordinates(double x, double y) {
    
        return true;
    }
}
