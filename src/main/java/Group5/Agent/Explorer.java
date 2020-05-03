package Group5.Agent;

import Group5.GameController.AgentController;
import Group5.GameController.Area;
import Group5.GameController.Vision;
import Interop.Action.*;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.*;

import static Interop.Percept.Vision.ObjectPerceptType.Wall;

/**
 * This class is executes a simple Breadth First Search to explore the graph representation of the map to explore.
 * The idea of the graph being explored on multiple levels is taken from Maio and Rizzi's "A MULTI-AGENT APPROACH TO
 * ENVIRONMENT EXPLORATION". The paper can be found here:
 * https://www.researchgate.net/publication/2294236_A_Multi-Agent_Approach_To_Environment_Exploration
 * Additionally, navigation is handled on an explicit representation of the map.
 */
public class Explorer implements Intruder {

    private Queue<Vertex> queue;
    private Graph g;
    private int level;
    private HashSet<Area> discoveredObjects;
    private Deque<Action> actionQueue;
    private Point previousRotationPoint;

    //TODO: all these methods now don't work because of the new game controller, the implementation was too heavy based on our specific controller
    //TODO: the last 3 methods in this class are used by the new controller, there is a new explore() method this one should be implemented

    /**
     * Use this constructor for an agent that has no information about his environment
     *
     * @param level This is the exploration level of the agent. See paper for details.
     */
    public Explorer(int level, AgentController agent) {


        this.level = level;
        this.g = new Graph(agent);
        this.queue = new LinkedList<>();
        this.actionQueue = new LinkedList<>();
        this.previousRotationPoint = new Point(0, 0);
    }



    /**
     * Use this constructor for an agent that has prior information about the environment it is exploring.
     *
     * @param level This is the exploration level of the agent. See paper for details.
     * @param g     Graph to be explored. It is assumed that this graph is partially known to the agent.
     */
    public Explorer(int level, Graph g, Queue<Vertex> queue, Vertex currentVertex) {
        this.level = level;
        this.queue = queue;
        this.g = g;
        this.g.addVertex(currentVertex);
    }

    public Graph getG() {
        return g;
    }

    public Graph BFSWrapper(Graph map, Vertex start) {
        //start.setVisited(true);
        this.queue.add(start);

        while (!queue.isEmpty()) {
            Vertex v = queue.remove();
            //List<Edge> adjacentEdges = map.getEdgesDirected(v.getId());
            //BFS(v, adjacentEdges);
        }

        return map;

    }

    public void BFS(Vertex v, List<Edge> adjacentEdges) {
        if (!g.getVertices().contains(v)) g.addVertex(v);
        for (Edge e : adjacentEdges) {
            //if (!g.getEdges().contains(e)) g.addEdge(e);
            //Vertex w = e.getDestination();
            //if (!w.isVisited()) {
              //  w.setVisited(true);
                //queue.add(w);
            //}
        }
    }

    /**
     * Note: When using this method, make sure to reset all Vertices to unvisited
     *
     * @param startingVertex Vertex from Exploration begins, should be an object close to agent
     * @param endVertex      Vertex that the agent wants to reach
     * @param map            Graph that is being explored
     * @return True if that vertex can be reached through a known path
     */
    private boolean pathExist(Vertex startingVertex, Vertex endVertex, Graph map) {
        //map = eBFSWrapper(map, startingVertex);
        //return endVertex.isVisited();
        return true;
    }

    //TODO: Method Find shortest path
    private ArrayList<Vertex> shortestPath(Vertex startingVertex, Vertex endVertex) {
        return null;
    }

    public Action getAction(AgentController agentController, ObjectPercepts objectsInVision){
        explore(agentController, objectsInVision);
        if (actionQueue.isEmpty()) return new Move(new Distance(1));
        else return actionQueue.pop();
    }



    private Rotate rotateTowards(Point point, AgentController agentController) {
        return new Rotate(agentController.getRelativeAngle(new Point(0, 0),
                agentController.getRelativePosition(point)));
    }

    private Move walkTowards(Point point, AgentController agentController) {
        return new Move(new Distance(new Point(0, 0), agentController.getRelativePosition(point)));
    }


    private void discover360(AgentController agentController) {
        for(int i=0; i<8; i++){
            actionQueue.add(new Rotate(Angle.fromDegrees(45)));
        }
    }



    // TODO: Implement together with Ionas
    private boolean checkInnerCorner(AgentController agentController, ArrayList<ObjectPercept> objectsInVision){
        boolean innerCornerDetected = false;

        return innerCornerDetected;
    }


    public void explore(AgentController agentController, ObjectPercepts objectsInVision) {

        // Discover all objects in vision for orientation
//        this.discover360(agentController);

        if (!objectsInVision.getAll().isEmpty()){

            ArrayList<ObjectPercept> visionObjects = new ArrayList<>();
            visionObjects.addAll(objectsInVision.getAll());
            Vision.bubbleSort(visionObjects, agentController);

            ArrayList<ObjectPercept> walls = new ArrayList<>();
            boolean seenObjectOfInterest = false;

            for (ObjectPercept objectPercept : visionObjects) {

                double distance = Math.sqrt(Math.pow(objectPercept.getPoint().getX(), 2) + Math.pow(objectPercept.getPoint().getY(), 2));

                if (objectPercept.getType() == ObjectPerceptType.SentryTower) {
                    seenObjectOfInterest = true;
                    actionQueue.clear();
                    Rotate r = rotateTowards(objectPercept.getPoint(), agentController);
                    Move m = walkTowards(objectPercept.getPoint(), agentController);
                    actionQueue.add(r);
                    actionQueue.add(m);
                    actionQueue.add(new Rotate(Angle.fromRadians(-r.getAngle().getRadians())));
                    actionQueue.add(m);
                    break;
                } else if (objectPercept.getType() == ObjectPerceptType.Door) { //TODO: Plan the way back after  moving through
                    seenObjectOfInterest = true;
                    Rotate r = rotateTowards(objectPercept.getPoint(), agentController);
                    Move m = walkTowards(objectPercept.getPoint(), agentController);
                    actionQueue.clear();
                    actionQueue.add(r);
                    actionQueue.add(m);
                    System.out.println("Found Door");
                }
                    else if (objectPercept.getType() == ObjectPerceptType.Window && distance < 10) { //TODO: Plan the way back after  moving through
                    seenObjectOfInterest = true;
                    Rotate r = rotateTowards(objectPercept.getPoint(), agentController);
                    Move m = walkTowards(objectPercept.getPoint(), agentController);
                    actionQueue.add(r);
//                    actionQueue.add(new Move(new Distance(1)));
                    System.out.println("Found Window");
                    break;
                } else if (objectPercept.getType() == ObjectPerceptType.Teleport) { //TODO: Plan the way back after  moving through
                    seenObjectOfInterest = true;
                    actionQueue.clear();
                    Rotate r = rotateTowards(objectPercept.getPoint(), agentController);
                    Move m = walkTowards(objectPercept.getPoint(), agentController);
                    actionQueue.add(r);
                    actionQueue.add(m);
                    break;
                } else if (objectPercept.getType() == Wall)
                    walls.add(objectPercept);
            }

            if (walls.size() >= 2 && !seenObjectOfInterest) {

//                actionQueue.clear();
                Point closestPointRelative = agentController.getRelativePosition(walls.get(0).getPoint());
                Point closestPoint = walls.get(0).getPoint();
                double closestDistance = Math.sqrt(Math.pow(closestPointRelative.getX(), 2) + Math.pow(closestPointRelative.getY(), 2));

                if (closestDistance < 12.001) {
                double sameXAverageYCoordinate = 0;
                double sameYAverageXCoordinate = 0;
                int sameXCount = 0;
                int sameYCount = 0;

                for (ObjectPercept w: walls){
                    if(w.getPoint().getX() == closestPoint.getX()) {
                        sameXAverageYCoordinate += Math.abs(w.getPoint().getY());
                        sameXCount++;
                    }
                    else if (w.getPoint().getY() == closestPoint.getY()) {
                        sameYAverageXCoordinate += Math.abs(w.getPoint().getX());
                        sameYCount++;
                    }
                }


                if (sameXCount > sameYCount){
                    System.out.println("Rotating towards X");
                    if(previousRotationPoint.getY() == -1) {
                        actionQueue.add(new Rotate(agentController.getRelativeAngle(new Point(0, 0), new Point(-1, 0))));
                        previousRotationPoint = new Point(-1, 0);
                    }
                    else {
                        actionQueue.add(new Rotate(agentController.getRelativeAngle(new Point(0, 0), new Point(1, 0))));
                        previousRotationPoint = new Point(1, 0);
                    }
                }

                else {
                    System.out.println("Rotating towards Y");
                    if(previousRotationPoint.getX() == 1) {
                        actionQueue.add(new Rotate(agentController.getRelativeAngle(new Point(0, 0), new Point(0, -1))));
                        previousRotationPoint = new Point(0, -1);
                    }
                    else {
                        actionQueue.add(new Rotate(agentController.getRelativeAngle(new Point(0, 0), new Point(0, 1))));
                        previousRotationPoint = new Point(0, 1);

                    }
                }






//                System.out.println("Start Point: (" + startPoint.getX() + ", " + startPoint.getY() + ")");
//                System.out.println("End Point: (" + endPoint.getX() + ", " + endPoint.getY() + ")");
//
//                System.out.println("Relative Angle: " + agentController.getRelativeAngle(closestPoint, endPoint).getDegrees());
//                    System.out.println("Start Point: (" + closestPoint.getX() + ", " + closestPoint.getY() + ")");

                }
                else {
                    actionQueue.clear();
                    actionQueue.add(new Move(new Distance(1)));
                    System.out.println("Have seen a wall. Moving on");
                }

//                if (checkInnerCorner(agentController, walls))
//                    actionQueue.add(new Rotate(Angle.fromRadians(Math.PI/2 + agentController.getRelativeAngle(startPoint, endPoint).getRadians())));
//                else {
//                    actionQueue.add(new Rotate(Angle.fromRadians(Math.PI/2)));
//                }

                for(Action a: actionQueue){
                    if (a instanceof Move) {
//                System.out.println("Move");
                    }
                    else if(a instanceof Rotate) {
//                     System.out.println("Rotate");
//                     System.out.println(((Rotate) a).getAngle().getDegrees());
                    }
                }

            }



        }

            else {
            actionQueue.add(new Move(new Distance(1)));
            System.out.println("Not seeing anything");
//                actionQueue.add(new Move(new Distance(1)));
        }
//            if (actionQueue.isEmpty()){
//            actionQueue.add(new Move(new Distance(1)));
//        }

        }




    /**
     * new constructor used for the new game controller
     */
    public Explorer(){
        this.queue = new LinkedList<>();
        this.actionQueue = new LinkedList<>();
        this.previousRotationPoint = new Point(0, 0);

    }


    /**
     *the new getAction method we should be using
     * @param percepts The precepts represent the world as perceived by that agent.
     * @return
     */
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        return explore(percepts);
    }

    /**
     * This is the new explore method we should be using
     * I didn't remove the old one since it still can be handy
     * The problem why we cant use the old one is that we use a method called getrelative angle but we have to use something different for that
     * @param percepts
     * @return
     */
    public IntruderAction explore(IntruderPercepts percepts){
        return new Move(new Distance(1));
    }
}



