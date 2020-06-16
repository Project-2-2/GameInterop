package Group4;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import Interop.Action.*;
import Interop.*;
import Interop.Percept.*;
import Interop.Geometry.*;
import Interop.Agent.*;
import Interop.Utils.*;
import Interop.Percept.Vision.*;
import Group4.OurInterop.*;

public class Agent {
    private static ArrayList<Agent> allAgents;
    private int ID;
    private static int lastID = 0;
    private double currentXLocation;
    private double currentYLocation;
    private Point2D currentLocation = new Point2D.Double();
    private boolean goalReached = false;
    public static final int AGENT_SIZE = 2;
    private ArrayList<AMove> possibleMoves;
    private FieldOfView view;

    public Agent(){
        this.ID = lastID + 1;
        lastID++;

    }

    public Agent(int currentXLocation, int currentYLocation){
        this.view = new FieldOfView(new Distance(15), Angle.fromRadians(Math.PI/6));
        this.currentXLocation = currentXLocation;
        this.currentYLocation = currentYLocation;
        this.currentLocation.setLocation(currentXLocation,currentYLocation);
        this.ID = lastID + 1;
        lastID ++;
    }

    public void setCurrentLocation(double currentXLocation, double currentYLocation){
        this.currentXLocation = currentXLocation;
        this.currentYLocation = currentYLocation;
        currentLocation.setLocation(currentXLocation,currentYLocation);
    }

    public void setCurrentLocation(Point2D location){
        this.currentXLocation = (int) location.getX();
        this.currentYLocation = (int) location.getY();
        this.currentLocation.setLocation(location);
    }
    //moves agent to the specified location
    public void executeMove(AMove m){
        this.currentXLocation = m.getX();
        this.currentYLocation = m.getY();
        this.currentLocation.setLocation(m.getX(), m.getY());
    }

    //call this method when the exploration agent got to its target. Should stop moving after goalReached is true
    public void targetReached(){
        this.goalReached = true;
    }

    public double getCurrentXLocation(){
        return this.currentXLocation;
    }

    public double getCurrentYLocation(){
        return this.currentYLocation;
    }

    public Point2D getCurrentLocation() {
        return currentLocation;
    }

    public int getID(){
        return this.ID;
    }

    public boolean getCurrentStatus(){
        return goalReached;
    }

    public static final double getAgentSize(){
        return AGENT_SIZE;
    }

    public void setGoalReached(boolean goalReached) {
        this.goalReached = goalReached;
    }

    public static int getLastID() {
        return lastID;
    }

    public ArrayList<AMove> getPossibleMoves() {
        return possibleMoves;
    }

}
