package Group5.Agent.Guard;

import Group9.Game;
import Interop.Action.DropPheromone;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Smell.SmellPerceptType;

import javax.swing.text.Position;
import java.util.ArrayList;

public class GraphExplorer implements Guard {

    //when this distance has exceeded we have to create a new node
    private double distanceToNewRegion;
    private Node previousNodeVisited;
    //list of all areas the guard has visited
    private ArrayList<Node> nodes;

    private Point position;
    private Angle angle;

    public GraphExplorer(){
        position = new Point(0,0);
        distanceToNewRegion = 8.0;
        nodes = new ArrayList<>();
        angle = Angle.fromDegrees(0);
    }

    /**
     * creates a new node for a new area
     * @param percepts
     */
    public void addAreaToGraph(GuardPercepts percepts){
        boolean newNodeBoolean = true;
        for (int i = 0; i<nodes.size();i++){
            if (nodes.get(i).agentInNode(position)){
                nodes.get(i).visitNodeAgain(percepts);
                newNodeBoolean = false;
                break;
            }
        }
        //TODO now it creates a new node if it is not inside another node and the center of the node is set to the agent's
        //TODO current position, however this is not optimal since we could get overlapping nodes.
        if (newNodeBoolean) {
            System.out.println("created new Area");
            distanceToNewRegion = percepts.getVision().getFieldOfView().getRange().getValue();
            Node newNode = new Node(percepts, position);
            previousNodeVisited = newNode;
            nodes.add(newNode);
        }
    }

    /**
     * call this method to move and update the guard's internal position
     * @param move
     * @return
     */
    public GuardAction move(Move move, GuardPercepts percepts){
        Point movement = new Point(move.getDistance().getValue()*Math.cos(angle.getRadians()),move.getDistance().getValue()*Math.sin(angle.getRadians()));
        position = new Point(position.getX()+movement.getX(),position.getY()+movement.getY());
        if(distanceToNewRegion>move.getDistance().getValue()){
            distanceToNewRegion = distanceToNewRegion-move.getDistance().getValue();
        }else{
            addAreaToGraph(percepts);
        }
//        System.out.println(position.toString());
        return move;
    }

    /**
     * call this method to do a rotation
     * @param rotate
     * @return
     */
    public GuardAction rotate(Rotate rotate){
        angle = Angle.fromDegrees(angle.getDegrees()+rotate.getAngle().getDegrees());
        while (angle.getDegrees()>360){
            angle = Angle.fromDegrees(angle.getDegrees()-360);
        }
        while (angle.getDegrees()<-360){
            angle = Angle.fromDegrees(angle.getDegrees()+360);
        }
        return rotate;
    }

    public void updateNodeIdleness(){
        for (int i =0; i<nodes.size();i++){
            nodes.get(i).updateIdleness();
        }
    }

    /**
     * just a random agent to start with
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return
     */
    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        updateNodeIdleness();

        percepts.getVision().getFieldOfView().getRange();

        if(!percepts.wasLastActionExecuted())
        {
            if(Math.random() < 0.1)
            {
                return new DropPheromone(SmellPerceptType.values()[(int) (Math.random() * SmellPerceptType.values().length)]);
            }

            return rotate(new Rotate(Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble())));
        }
        else
        {
            return move(new Move(new Distance(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts))),percepts);
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
}
