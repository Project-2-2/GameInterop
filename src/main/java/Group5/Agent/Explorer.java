package Group5.Agent;

import Group5.GameController.AgentController;
import Group5.GameController.Area;
import Group5.GameController.MapInfo;
import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

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

    private ArrayList<Point> computeMidpoints(Point leftBottom, Point rightBottom, Point leftTop, Point rightTop) {

        ArrayList<Point> points = new ArrayList<>();
        points.set(0, new Point((leftBottom.getX() + rightBottom.getX()) / 2, (leftBottom.getY() + rightBottom.getY()) / 2));
        points.set(1, new Point((leftBottom.getX() + leftTop.getX()) / 2, (leftBottom.getY() + leftTop.getY()) / 2));
        points.set(2, new Point((rightBottom.getX() + rightTop.getX()) / 2, (rightBottom.getY() + rightTop.getY()) / 2));
        points.set(3, new Point((leftTop.getX() + rightTop.getX()) / 2, (leftTop.getY() + rightTop.getY()) / 2));

        return points;
    }


    /**
     * @param area
     * @param agentController
     * @return The point closest to the agent
     */
    private Point getClosestPoint(Area area, AgentController agentController, List<Point> points) { // TODO: use midpoints

        // determine closest midpoint
        Point closestPoint = points.get(0);
        Distance closestDistance = new Distance(new Point(0, 0), agentController.getRelativePosition(closestPoint));
        for (Point point : points) {
            Distance distance = new Distance(new Point(0, 0), agentController.getRelativePosition(point));
            if (closestDistance.getValue() > distance.getValue()) {
                closestDistance = distance;
                closestPoint = point;
            }

        }
        return closestPoint;
    }

    // TODO: Implement together with Ionas
    private boolean checkInnerCorner(AgentController agentController, ArrayList<Area> objectsInVision, Area wall, Point targetPoint){
        boolean innerCornerDetected = false;
        for(Area area: objectsInVision){

        }
        return innerCornerDetected;
    }


    public void explore(AgentController agentController, ArrayList<Area> objectsInVision) {

        discoveredObjects.addAll(objectsInVision);

        // Discover all objects in vision for orientation
        this.discover360(agentController);

        for (Area area : objectsInVision) {

            // assign variables as a defense copy
            ArrayList<Point> points = area.getAreaVectors();

            // compute midpoints
            points = computeMidpoints(agentController.getRelativePosition(points.get(0)),
                    agentController.getRelativePosition(points.get(1)),
                    agentController.getRelativePosition(points.get(2)),
                    agentController.getRelativePosition(points.get(3)));

            if (area.getObjectsPerceptType() == ObjectPerceptType.SentryTower) {
                Rotate r = rotateTowards(getClosestPoint(area, agentController, points), agentController);
                Move m = walkTowards(getClosestPoint(area, agentController, points), agentController);
                actionQueue.add(r);
                actionQueue.add(m);
                actionQueue.add(new Rotate(Angle.fromRadians(-r.getAngle().getRadians())));
                actionQueue.add(m);
                break;
            } else if (area.getObjectsPerceptType() == ObjectPerceptType.Door) { //TODO: Plan the way back after  moving through
                Rotate r = rotateTowards(getClosestPoint(area, agentController, points), agentController);
                Move m = walkTowards(getClosestPoint(area, agentController, points), agentController);
                actionQueue.add(r);
                actionQueue.add(m);
                actionQueue.add(new Rotate(Angle.fromRadians(-r.getAngle().getRadians())));
                actionQueue.add(m);
                break;
            } else if (area.getObjectsPerceptType() == ObjectPerceptType.Teleport) { //TODO: Plan the way back after  moving through
                Rotate r = rotateTowards(getClosestPoint(area, agentController, points), agentController);
                Move m = walkTowards(getClosestPoint(area, agentController, points), agentController);
                actionQueue.add(r);
                actionQueue.add(m);
                actionQueue.add(new Rotate(Angle.fromRadians(-r.getAngle().getRadians())));
                actionQueue.add(m);
                break;
            } else if (area.getObjectsPerceptType() == Wall) {

                // Determine side that is closest to the agent
                Point closestPoint = getClosestPoint(area, agentController, points);
                Point startPoint;
                Point endPoint;

                if (closestPoint.getX() == points.get(0).getX() &&
                        closestPoint.getY() == points.get(0).getY()) {
                    startPoint = area.getAreaVectors().get(0);
                    endPoint = area.getAreaVectors().get(1);
                }
                else if (closestPoint.getX() == points.get(1).getX() &&
                        closestPoint.getY() == points.get(1).getY()) {
                    startPoint = area.getAreaVectors().get(0);
                    endPoint = area.getAreaVectors().get(3);
                }
                else if (closestPoint.getX() == points.get(2).getX() &&
                        closestPoint.getY() == points.get(2).getY()) {
                    startPoint = area.getAreaVectors().get(1);
                    endPoint = area.getAreaVectors().get(4);
                }

                else{
                startPoint = area.getAreaVectors().get(3);
                endPoint = area.getAreaVectors().get(4);
                }

                // Determine if we want to rotate towards start or endpoint
                Point targetPoint;
                if (new Distance(new Point(0, 0),
                        agentController.getRelativePosition(startPoint)).getValue() <
                        new Distance(new Point(0, 0), agentController.getRelativePosition(startPoint)).getValue())
                        targetPoint = startPoint;
                else
                    targetPoint = endPoint;

                if (checkInnerCorner(agentController, objectsInVision, area, targetPoint))
                    actionQueue.add(new Rotate(Angle.fromDegrees(90+agentController.getRelativeAngle(startPoint, endPoint).getDegrees())));
                else
                    actionQueue.add(new Rotate(agentController.getRelativeAngle(startPoint, endPoint)));

                actionQueue.add(new Move(agentController.getViewRange()));
            }

            else actionQueue.add(new Move(agentController.getViewRange()));

        }


    }


}
