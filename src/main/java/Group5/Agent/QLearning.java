package Group5.Agent;

import java.io.IOException;
import java.util.ArrayList;
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
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;


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
        EmptySpace(9);
        
        private int value;
        
        State(int value) {
            this.value = value;
        }
    }
    
    private State prevState;
    private State currState;
    private int prevAction = 0;
    private int currAction;
    private float epsilon;
    private float gamma;
    private float alpha;
    private List<Double> legalActions;
    private int numActions;
    private int numStates;
    private double[][] qTable;
    
    private Point agentPosition;
    private ObjectPercept state;
    private AgentController agentController;
    private ObjectPercepts objectsInVision;
    private Random rn;
    private double epsilonDecay;
    private Deque<Action> actionQueue;
    
    /*
     *@gamma   : The discount factor for future rewards
     *@epsilon : The probability the agent makes a random move
     *@alpha   : The learning rate for the agent
     *@board   : A reference to a game object for the agent to interact with
     */
    public QLearning(float gamma, float epsilon, float alpha, AgentController agentController,
            int numActions, int numStates) {
        
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.alpha = alpha;
        this.numActions = numActions;
        this.numStates = numStates;
        this.qTable = new double[numStates][numActions];
//        this.qTable = new double [][]{
//                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//                {-1.9662777989086195,-2.472447754072641,-2.4696678985330607,-2.4796416410479747,-1.9080988371698346,-1.737134594574407},
//                {-0.6430906600515617,-0.6807711143525313,-0.6608904824194621,-0.6561722656360214,-0.6256462906671728,-0.6770173587089482},
//                {-0.10000000149011612,0.0,0.0,0.0,0.0,0.0},
//                {-1.8845545180957446,-1.9061786316237466,-1.8668967747145913,-1.9226932273347828,-1.717385686571351,-1.9255868290016818},
//                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//                {-0.27100000362098214,-0.2967600048285723,-0.190000002682209,-0.190000002682209,-0.27100000362098214,-0.27100000362098214},
//                {0.0,0.0,0.0,0.0,0.0,0.0,0.0},
//                {-0.10000000149011612,0.0,0.0,-1.0222914416837978,-1.0040120377926016,0.0}
//        };
        this.rn = new Random();
        this.epsilonDecay = 0.0001;
        this.agentController = agentController;
        this.actionQueue = new LinkedList<>();
    }
    
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
        
        if (chance < epsilon) {
            return rand.nextInt(6);
        } else {
            return maxIndex;
        }
    }
    
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
        
//        ArrayList<ObjectPercept> visionObjects = new ArrayList<>();
//        visionObjects.addAll(objectsInVision.getAll());
//        Vision.bubbleSort(visionObjects, agentController);
        //Bellman Equation
        double update = reward + gamma * findMaxQState(this.currState.value) - qTable[this.prevState.value][this.prevAction];
        qTable[this.prevState.value][this.prevAction] = qTable[this.prevState.value][this.prevAction] + alpha * update;
        
        this.prevAction = this.currAction;
        this.prevState = this.currState;
        
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
    
    public double findMaxQState(int stateIndex) {
        
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
    public int getMaxValue(double[] qTablePart) {
        //float[] array = Q_part;
        int max = 0;
        double maxValue = qTablePart[0];
        for (int i = 0; i < qTablePart.length; i++) {
            if (qTablePart[i] > maxValue) {
                max = i;
            }
        }
        return max;
    }
    
    public void writeTableToFile(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < numStates; i++){
            for(int j = 0; j < numActions; j++){
                builder.append(qTable[i][j]+"");
                if(j < numActions - 1)
                    builder.append(",");
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
}