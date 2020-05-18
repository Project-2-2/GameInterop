package Group5.Agent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Group5.GameController.Door;
import Group5.GameController.SentryTower;
import Group5.GameController.ShadedArea;
import Group5.GameController.Vision;
import Interop.Action.Action;

import Group5.GameController.AgentController;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.io.FileWriter;
import java.io.FileReader;
import java.util.Arrays;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.Scanner;


public class QLearning {
    
    public enum State {
        
        Guard(0),
        Intruder(1),
        Wall(2),
        Window(3),
        Door(4),
        Teleport(5),
        SentryTower(6),
        ShadedArea(7),
        TargetArea(8),
        EmptySpace(9),
        Noise(10),
        Yell(11);
    
        // Smell is used only when intruders are more than 1 - if so, uncomment block of code below
//        Pheromone1(12),
//        Pheromone2(13),
//        Pheromone3(14),
//        Pheromone4(15),
//        Pheromone5(16);
    
        private int value;
        State(int value) {
            this.value = value;
        }
    }
    
    private State prevState;
    private State currState;
    private int prevAction = 0;
    private int currAction;
    private static float epsilon;
    private float gamma;
    private float alpha;
    private int numActions;
    private int numStates;
    private double[][] qTable;
    
    private Point agentPosition;
    private ObjectPercept state;
    private ObjectPercepts objectsInVision;
    private Random rn;
//    private double EPSILON_DECAY = 0.0000001;
    private Deque<Action> actionQueue;
    
    /**
     *@gamma   : The discount factor for future rewards
     *@epsilon : The probability the agent makes a random move
     *@alpha   : The learning rate for the agent
     *@board   : A reference to a game object for the agent to interact with
     */
    public QLearning(float gamma, float epsilon, float alpha,
            int numActions, int numStates) {
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.alpha = alpha;
        this.numActions = numActions;
        this.numStates = numStates;
        this.qTable = new double[numStates][numActions];
//        this.qTable = readTableFromFile();
        this.rn = new Random();
        this.actionQueue = new LinkedList<>();
    }
    
    /**
     *
     * @param state Objects in vision
     * @return index with maximum value action from Q-Table
     */
    public int getMaxValueAction(ObjectPerceptType state) {
        State[] allStates = State.class.getEnumConstants();
        int stateIndex = 0;
        for (State tempState : allStates) {
            if (state.name().equals(tempState.name())) {
                stateIndex = tempState.value;
            }
        }
        double[] qTablePart = qTable[stateIndex];
        int maxIndex = getMaxValue(qTablePart);
        double chance = Math.random();
        Random rand = new Random();
        
        // Perform random action if chance is smaller than epsilon value
        // Otherwise return maxValue action index from Q-table
        if (chance < epsilon) {
            return rand.nextInt(this.numActions);
        } else {
            return maxIndex;
        }
    }
    
    /**
     *
     * @param state Smells detected
     * @return index with maximum value action from Q-Table
     */
    public int getMaxValueAction(SmellPerceptType state) {
        State[] allStates = State.class.getEnumConstants();
        int stateIndex = 0;
        for (State tempState : allStates) {
            if (state.name().equals(tempState.name())) {
                stateIndex = tempState.value;
            }
        }
        double[] qTablePart = qTable[stateIndex];
        int maxIndex = getMaxValue(qTablePart);
        double chance = Math.random();
        Random rand = new Random();
    
        // Perform random action if chance is smaller than epsilon value
        // Otherwise return maxValue action index from Q-table
        if (chance < epsilon) {
            return rand.nextInt(this.numActions);
        } else {
            return maxIndex;
        }
    }
    
    /**
     *
     * @param state Sounds detected
     * @return index with maximum value action from Q-Table
     */
    public int getMaxValueAction(SoundPerceptType state) {
        State[] allStates = State.class.getEnumConstants();
        int stateIndex = 0;
        for (State tempState : allStates) {
            if (state.name().equals(tempState.name())) {
                stateIndex = tempState.value;
            }
        }
        double[] qTablePart = qTable[stateIndex];
        int maxIndex = getMaxValue(qTablePart);
        double chance = Math.random();
        Random rand = new Random();
    
        // Perform random action if chance is smaller than epsilon value
        // Otherwise return maxValue action index from Q-table
        if (chance < epsilon) {
            return rand.nextInt(this.numActions);
        } else {
            return maxIndex;
        }
    }
    
    /**
     *
     * @param state Smells detected
     * @param currentAction maxValue action to be executed
     * @param reward
     */
    protected void updateQTable(SmellPerceptType state, int currentAction, float reward) {
        State[] allStates = State.class.getEnumConstants();
        if (this.prevState == null) {
            this.prevState = State.EmptySpace;
        }
        
        this.currAction = currentAction;
        
        for (State cState : allStates) {
            if (state.name().equals(cState.name())) {
                this.currState = cState;
                break;
            }
        }
        
        //Bellman Equation to update Q-table
        double update = reward + gamma * findMaxQState(this.currState.value) - qTable[this.prevState.value][this.prevAction];
        qTable[this.prevState.value][this.prevAction] = qTable[this.prevState.value][this.prevAction] + alpha * update;
        
        this.prevAction = this.currAction;
        this.prevState = this.currState;
    }
    
    /**
     *
     * @param state Sounds detected
     * @param currentAction maxValue action to be executed
     * @param reward
     */
    protected void updateQTable(SoundPerceptType state, int currentAction, float reward) {
        State[] allStates = State.class.getEnumConstants();
        if (this.prevState == null) {
            this.prevState = State.EmptySpace;
        }
        
        this.currAction = currentAction;
        
        for (State cState : allStates) {
            if (state.name().equals(cState.name())) {
                this.currState = cState;
                break;
            }
        }
    
        //Bellman Equation to update Q-table
        double update = reward + gamma * findMaxQState(this.currState.value) - qTable[this.prevState.value][this.prevAction];
        qTable[this.prevState.value][this.prevAction] = qTable[this.prevState.value][this.prevAction] + alpha * update;
        
        this.prevAction = this.currAction;
        this.prevState = this.currState;
    }
    
    /**
     *
     * @param state Objects in vision
     * @param currentAction maxValue action to be executed
     * @param reward
     */
    protected void updateQTable(ObjectPerceptType state, int currentAction, float reward) {
        State[] allStates = State.class.getEnumConstants();
        if (this.prevState == null) {
            this.prevState = State.EmptySpace;
        }
        
        this.currAction = currentAction;
    
        for (State cState : allStates) {
            if (state.name().equals(cState.name())) {
                this.currState = cState;
                break;
            }
        }
    
        //Bellman Equation to update Q-table
        double update = reward + gamma * findMaxQState(this.currState.value) - qTable[this.prevState.value][this.prevAction];
        qTable[this.prevState.value][this.prevAction] = qTable[this.prevState.value][this.prevAction] + alpha * update;
        
        this.prevAction = this.currAction;
        this.prevState = this.currState;
        
        // For printing the Q-table
//        for (int i = 0; i < numStates; i++) {
//            for (int j = 0; j < numActions; j++) {
//                System.out.print("" + qTable[i][j] + ",");
//            }
//            System.out.println();
//        }
//        System.out.println();
    }
    
    protected double [][] getQTable(){
        return this.qTable;
    }
    
    private double findMaxQState(int stateIndex) {
        
        double[] qTablePart = qTable[stateIndex];
        int max = getMaxValue(qTablePart);
        double maxValue = qTablePart[max];
        return maxValue;
    }
    
    /**
     * @param qTablePart
     *         whole qTable as input
     * @return the index of the max value of the part of the Qtable that references the current state
     */
    private int getMaxValue(double[] qTablePart) {
        int max = 0;
        double maxValue = qTablePart[0];
        for (int i = 0; i < qTablePart.length; i++) {
            if (qTablePart[i] > maxValue) {
                max = i;
            }
        }
        return max;
    }
    
    /**
     * Writing the Q-Table to a file locally
     */
    protected void writeTableToFile(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < numStates; i++){
            for(int j = 0; j < numActions; j++){
                builder.append(qTable[i][j]+"");
                if(j < numActions - 1)
                    builder.append(" ");
            }
            builder.append("\n");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/Group5/QTable.txt"));
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Reading the Q-table from a file locally
     */
    private double[][] readTableFromFile() {
        try {
            Scanner sc = new Scanner(new BufferedReader(new FileReader("src/main/java/Group5/QTable.txt")));
            double [][] initialTable = new double[this.numStates][this.numActions];
            while(sc.hasNextLine()) {
                for (int i=0; i<initialTable.length; i++) {
                    String[] line = sc.nextLine().trim().split(" ");
                    for (int j=0; j<line.length; j++) {
                        initialTable[i][j] = Double.parseDouble(line[j]);
                    }
                }
            }
            return initialTable;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}