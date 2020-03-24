package Group5.Agent;

import Group5.GameController.AgentController;
import Group5.GameController.Area;
import Group5.GameController.MapInfo;
import Group5.GameController.Vision;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.security.AccessController;
import java.util.*;

import static Interop.Percept.Vision.ObjectPerceptType.Wall;

/**
 * This class is executes a simple Breadth First Search to explore the graph representation of the map to explore.
 * The idea of the graph being explored on multiple levels is taken from Maio and Rizzi's "A MULTI-AGENT APPROACH TO
 * ENVIRONMENT EXPLORATION". The paper can be found here:
 * https://www.researchgate.net/publication/2294236_A_Multi-Agent_Approach_To_Environment_Exploration
 * Additionally, navigation is handled on an explicit representation of the map.
 */
public class Explorer {

    private Queue<Vertex> queue;
    private Graph g;
    private int level;
    private HashSet<Area> discoveredObjects;
    private Deque<Action> actionQueue;


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
        start.setVisited(true);
        this.queue.add(start);

        while (!queue.isEmpty()) {
            Vertex v = queue.remove();
            //List<Edge> adjacentEdges = map.getEdgesDirected(v.getId());
            //BFS(v, adjacentEdges);
        }

        return map;

    }

    public void BFS(Vertex v, List<Edge> adjacentEdges) {
        if (!g.getVertexes().contains(v)) g.addVertex(v);
        for (Edge e : adjacentEdges) {
            if (!g.getEdges().contains(e)) g.addEdge(e);
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
        map = BFSWrapper(map, startingVertex);
        return endVertex.isVisited();
    }

    //TODO: Method Find shortest path
    private ArrayList<Vertex> shortestPath(Vertex startingVertex, Vertex endVertex) {
        return null;
    }

    public Action getAction(AgentController agentController, ObjectPercepts objectsInVision){
        explore(agentController, objectsInVision);
        return actionQueue.pop();
    }



    private Rotate rotateTowards(Point point, AgentController agentController) {
        return new Rotate(agentController.getRelativePosition(point).getClockDirection());
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

            for (ObjectPercept objectPercept : visionObjects) {

                if (objectPercept.getType() == ObjectPerceptType.SentryTower) {
                    Rotate r = rotateTowards(objectPercept.getPoint(), agentController);
                    Move m = walkTowards(objectPercept.getPoint(), agentController);
                    actionQueue.add(r);
                    actionQueue.add(m);
                    actionQueue.add(new Rotate(Angle.fromRadians(-r.getAngle().getRadians())));
                    actionQueue.add(m);
                    break;
                } else if (objectPercept.getType() == ObjectPerceptType.Door) { //TODO: Plan the way back after  moving through
                    Rotate r = rotateTowards(objectPercept.getPoint(), agentController);
                    Move m = walkTowards(objectPercept.getPoint(), agentController);
                    actionQueue.add(r);
                    actionQueue.add(m);
                    break;
                } else if (objectPercept.getType() == ObjectPerceptType.Teleport) { //TODO: Plan the way back after  moving through
                    Rotate r = rotateTowards(objectPercept.getPoint(), agentController);
                    Move m = walkTowards(objectPercept.getPoint(), agentController);
                    actionQueue.add(r);
                    actionQueue.add(m);
                    break;
                } else if (objectPercept.getType() == Wall)
                    walls.add(objectPercept);
            }

            if (walls.size() >= 7) {
                Point startPoint = walls.get(walls.size()-7).getPoint();
                Point endPoint = walls.get(walls.size()-1).getPoint();
//                System.out.println("Start Point: (" + startPoint.getX() + ", " + startPoint.getY() + ")");
//                System.out.println("End Point: (" + endPoint.getX() + ", " + endPoint.getY() + ")");

                System.out.println("Relative Angle: " + agentController.getRelativeAngle(startPoint, endPoint).getDegrees());



                if (checkInnerCorner(agentController, walls))
                    actionQueue.add(new Rotate(Angle.fromDegrees(90 + agentController.getRelativeAngle(startPoint, endPoint).getDegrees())));
                else {
                    actionQueue.add(new Rotate(agentController.getRelativeAngle(startPoint, endPoint)));
//                    actionQueue.add(new Rotate(Angle.fromDegrees(90)));
                }

                for(Action a: actionQueue){
                    if (a instanceof Move) {
//                System.out.println("Move");
                    }
                    else if(a instanceof Rotate) {
//                     System.out.println("Rotate");
//                     System.out.println(((Rotate) a).getAngle().getDegrees());
                    }
                }
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
                actionQueue.add(new Move(new Distance(1)));
            }



        }

            else
                actionQueue.add(new Move(new Distance(agentController.getViewRange().getValue()/2)));
//                actionQueue.add(new Move(new Distance(1)));

        }


    }



