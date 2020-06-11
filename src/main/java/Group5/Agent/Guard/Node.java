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

    //every node is represented as a square, so these are the boundaries of the square
    private double leftBoundary;
    private double rightBoundary;
    private double topBoundary;
    private double bottomBoundary;

    public Node(Percepts percepts, Point position){
        center = position;
//        System.out.println(center.toString());
        nodeIdleness = 0;
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();
        objectList = new ArrayList<>();
        for (ObjectPercept e : vision){
            objectList.add(e.getType());
        }
        radius = percepts.getVision().getFieldOfView().getRange().getValue();
        radius = 30;

        leftBoundary=Math.min(position.getX()+0.5*radius,position.getX()-0.5*radius);
        rightBoundary=Math.max(position.getX()+0.5*radius,position.getX()-0.5*radius);
        topBoundary=Math.max(position.getY()+0.5*radius,position.getY()-0.5*radius);
        bottomBoundary=Math.min(position.getY()+0.5*radius,position.getY()-0.5*radius);
    }


    public void updateIdleness(){
        nodeIdleness++;
    }

    public int getNodeIdleness(){
        return nodeIdleness;
    }

    public void visitNodeAgain(Percepts percepts){
        nodeIdleness = 0;
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();
        for (ObjectPercept e : vision){
            if (!objectList.contains(e.getType())){
//                System.out.println(e.getType());
                objectList.add(e.getType());
//                System.out.println(objectList.size());
            }
        }
    }

    public boolean agentInNode(Point agentPosition){
        return (agentPosition.getY()>bottomBoundary)&(agentPosition.getY()<topBoundary)&(agentPosition.getX()>leftBoundary)&(agentPosition.getX()<rightBoundary);
    }

    public static double getDistance(Point x, Point y){
        return Math.sqrt(Math.pow(x.getX()-y.getX(),2)+Math.pow(x.getY()-y.getY(),2));
    }

    public Point getCenter(){
        return center;
    }
}
