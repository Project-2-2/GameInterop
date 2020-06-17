package Group5.Agent.Guard;

import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;

public class Node {

    //the center of the node
    //first point node must be 0,0
    private Point center;
    //map all objects in a region to their position
    private HashMap<ObjectPerceptType, List<Point>> objectMap;
    //to save how long a node has been unvisited
    private int nodeIdleness;

    private double radius;
    private ArrayList<Node> neighbours;

    //every node is represented as a square, so these are the boundaries of the square
    private double leftBoundary;
    private double rightBoundary;
    private double topBoundary;
    private double bottomBoundary;

    private boolean neverVisited;

    public Node(Percepts percepts, Point position, Point agentPosition, double radius) {
        center = new Point(Math.round(position.getX()), Math.round(position.getY()));
//        System.out.println(center.toString());
        nodeIdleness = 0;
        createObjectMap(percepts, agentPosition);

//        radius = percepts.getVision().getFieldOfView().getRange().getValue();
//        radius = 30;
        this.radius = radius;
        neighbours = new ArrayList<>();


        leftBoundary = Math.min(position.getX() + 0.5 * radius, position.getX() - 0.5 * radius);
        rightBoundary = Math.max(position.getX() + 0.5 * radius, position.getX() - 0.5 * radius);
        topBoundary = Math.max(position.getY() + 0.5 * radius, position.getY() - 0.5 * radius);
        bottomBoundary = Math.min(position.getY() + 0.5 * radius, position.getY() - 0.5 * radius);

        neverVisited = false;

    }

    public Node(Point position, double radius) {
        center = new Point(Math.round(position.getX()), Math.round(position.getY()));
        this.radius = radius;
        neighbours = new ArrayList<>();

        objectMap = new HashMap<>();

        nodeIdleness = 50; //Cannot be INTEGER.MAXVALUE because when updated the value will be negative

        leftBoundary = Math.min(position.getX() + 0.5 * radius, position.getX() - 0.5 * radius);
        rightBoundary = Math.max(position.getX() + 0.5 * radius, position.getX() - 0.5 * radius);
        topBoundary = Math.max(position.getY() + 0.5 * radius, position.getY() - 0.5 * radius);
        bottomBoundary = Math.min(position.getY() + 0.5 * radius, position.getY() - 0.5 * radius);

        neverVisited = true;

    }

    public static double getDistance(Point x, Point y) {
        return Math.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2));
    }

    /**
     * Creates a mapping from ObjectPerceptTypes to their position relative to the spawn point of the agent.
     * The position can be used for maneuvering inside an area as well as recognizing an objectpercept.
     *
     * @param percepts      The percepts found in the area
     * @param agentPosition The current position of the agent relative to its spawn location
     */
    private void createObjectMap(Percepts percepts, Point agentPosition) {
        objectMap = new HashMap<>();
        Set<ObjectPercept> objectPercepts = percepts.getVision().getObjects().getAll();
        for (ObjectPercept o : objectPercepts) {
            Point p = new Point(agentPosition.getX() + o.getPoint().getX(),
                    agentPosition.getY() + o.getPoint().getY());
            if (objectMap.keySet().contains(o.getType()))
                objectMap.get(o.getType()).add(p);
            else {
                objectMap.put(o.getType(), new ArrayList<>());
                objectMap.get(o.getType()).add(p);
            }
        }

    }

    public HashMap<ObjectPerceptType, List<Point>> getObjectMap() {
        return objectMap;
    }

    public void updateIdleness(HashMap<ObjectPerceptType, Integer> weightMap ) {
        for(ObjectPerceptType key: weightMap.keySet()) {
            if (this.getObjectMap().keySet().contains(key))
                nodeIdleness += weightMap.get(key);
        }
    }

    public int getNodeIdleness() {
        return nodeIdleness;
    }

    /***
     * Updates the object percepts contained in a node upon revisiting it.
     * @param percepts The world as perceived by the agent. These are the current objects in vision
     * @param agentPosition The current position of the agent relative to its spawn location
     */
    public void visitNodeAgain(Percepts percepts, Point agentPosition, double epsilon) {
        neverVisited = false;
        nodeIdleness = 0;
        Set<ObjectPercept> vision = percepts.getVision().getObjects().getAll();

        for (ObjectPercept o : vision) {
            Point objectPoint = new Point(agentPosition.getX() + o.getPoint().getX(),
                    agentPosition.getY() + o.getPoint().getY());
            if (objectMap.keySet().contains(o.getType())) {
                Set<Point> addThose = new HashSet<>();
                for (Point p : objectMap.get(o.getType())) {
                    if (new Distance(p, objectPoint).getValue() > epsilon) {
                        addThose.add(objectPoint);
                    }
                }
                objectMap.get(o.getType()).addAll(addThose);
            } else {
                objectMap.put(o.getType(), new ArrayList<>());
                objectMap.get(o.getType()).add(objectPoint);
            }
        }
    }

    public boolean agentInNode(Point agentPosition) {
        return (agentPosition.getY() > bottomBoundary) & (agentPosition.getY() < topBoundary) && (agentPosition.getX() > leftBoundary) & (agentPosition.getX() < rightBoundary);
    }

    public Point getCenter() {
        return center;
    }

    public double getLeftBoundary() {
        return leftBoundary;
    }

    public double getRightBoundary() {
        return rightBoundary;
    }

    public double getTopBoundary() {
        return topBoundary;
    }

    public double getBottomBoundary() {
        return bottomBoundary;
    }

    //checks if node was only generated as an adjacent node, or it was ever visited before
    public boolean isNeverVisited() {
        return neverVisited;
    }

    public void addNeighbour(Node newNeighbour) {
        neighbours.add(newNeighbour);
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }
}
