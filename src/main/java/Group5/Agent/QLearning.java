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
    private float epsilon;
    private float gamma;
    private float alpha;
    private List<Double> legalActions;
    private int numActions;
    private int numStates;
    private float[][] qTable;
    
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
        this.qTable = new float[numStates][numActions];
        this.rn = new Random();
        this.epsilonDecay = 0.0001;
        this.agentController = agentController;
        this.actionQueue = new LinkedList<>();
    }
    
//    /*
//     *@state   : The current x,y coordinates of the agent
//     *Returns an ArrayList containing all legal moves for the agent to make
//     */
//    private ArrayList<double[]> getLegalMoves(Point agentPosition) {
//
//        ArrayList<double[]> legalMoves = new ArrayList<>();
//        this.agentPosition = agentController.getPosition();
//        double[] xCandidates =
//                { this.agentPosition.getX() - 1, this.agentPosition.getX(), this.agentPosition.getX() + 1 };
//        double[] yCandidates =
//                { this.agentPosition.getY() - 1, this.agentPosition.getY(), this.agentPosition.getY() + 1 };
//
//        for (double x : xCandidates) {
//            for (double y : yCandidates) {
//                double[] move = { x, y };
//                legalMoves.add(move);
//            }
//        }
//        return legalMoves;
//    }
    
    public int getMaxValueAction(ObjectPerceptType state) {
        State[] allStates = State.class.getEnumConstants();
        int stateIndex = 0;
        for (State tempState : allStates) {
            if (state.name().equals(tempState.name())) {
                stateIndex = tempState.value;
            }
        }
        float[] qTablePart = qTable[stateIndex];
        int maxIndex = getMaxValue(qTablePart);
        double chance = Math.random();
        Random rand = new Random();
        
        if (chance < epsilon) {
            return rand.nextInt(3);
        } else {
            return maxIndex;
        }
    }
    
    protected void updateQTable(ObjectPerceptType state, ObjectPercepts objectsInVision, float reward) {
        State[] allStates = State.class.getEnumConstants();
        if (this.prevState == null) {
            this.prevState = State.EmptySpace;
        }
    
        for (State currState : allStates) {
            if (state.name().equals(currState.name())) {
                this.prevState = currState;
            }
        }
    
        ArrayList<ObjectPercept> visionObjects = new ArrayList<>();
        visionObjects.addAll(objectsInVision.getAll());
        Vision.bubbleSort(visionObjects, agentController);
        for (int i = 0; i < numActions; i++) {
            //Bellman Equation
            float update = reward + gamma * findMaxQState(this.prevState.value) - qTable[this.prevState.value][i];
            qTable[this.prevState.value][i] = qTable[this.prevState.value][i] + alpha * update;
        }
//        for (int i = 0; i < numStates; i++) {
//            for (int j = 0; j < numActions; j++) {
//                System.out.print("" + qTable[i][j] + ",");
//            }
//            System.out.println();
//        }
//        System.out.println();
    }
    
    protected float [][] getQTable(){
        return this.qTable;
    }
    
    public float findMaxQState(int stateIndex) {
        
        float[] qTablePart = qTable[stateIndex];
        int max = getMaxValue(qTablePart);
        float maxValue = qTablePart[max];
        return maxValue;
    }
    
    /**
     * @param qTablePart
     *         whole qTable as input
     * @return the index of the max value of the part of the Qtable that references the current state
     */
    public int getMaxValue(float[] qTablePart) {
        //float[] array = Q_part;
        int max = 0;
        float maxValue = qTablePart[0];
        for (int i = 0; i < qTablePart.length; i++) {
            if (qTablePart[i] > maxValue) {
                max = i;
            }
        }
        return max;
    }
    
    public void writeTableToFile(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < numStates; i++)//for each row
        {
            for(int j = 0; j < numActions; j++)//for each column
            {
                builder.append(qTable[i][j]+"");//append to the output string
                if(j < numActions - 1)//if this is not the last row element
                    builder.append(",");//then add comma (if you don't like commas you can use spaces)
            }
            builder.append("\n");//append new line at the end of the row
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/Group5/QTable.txt"));
            writer.write(builder.toString());//save the string representation of the board
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}