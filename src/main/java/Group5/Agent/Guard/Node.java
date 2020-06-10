package Group5.Agent.Guard;

import Interop.Geometry.Point;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Set;

public class Node {

    //the center of the node
    //first point node must be 0,0
    private Point center;
    //list to add all objects in the area
    private ArrayList<ObjectPerceptType> objectList;
    //to save how long a node has been unvisited
    private int nodeIdleness;

    private double radius;

    public Node(Percepts percepts, Point position){
        center = position;
        nodeIdleness = 0;
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();
        objectList = new ArrayList<>();
        for (ObjectPercept e : vision){
            objectList.add(e.getType());
        }
        radius = percepts.getVision().getFieldOfView().getRange().getValue();
    }

    public void updateIdleness(){
        nodeIdleness++;
    }

    public void visitNodeAgain(Percepts percepts){
        nodeIdleness = 0;
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();
        for (ObjectPercept e : vision){
            if (!objectList.contains(e)){
//                System.out.println(e.getType());
                objectList.add(e.getType());
//                System.out.println(vision.size());
            }
        }
    }

    public boolean agentInNode(Point agentPosition){
        if (getDistance(center,agentPosition)<=radius){
            return true;
        }
        return false;

    }

    public static double getDistance(Point x, Point y){
        return Math.sqrt(Math.pow(x.getX()-y.getX(),2)+Math.pow(x.getY()-y.getY(),2));
    }
}
