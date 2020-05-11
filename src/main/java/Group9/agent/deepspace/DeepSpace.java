package Group9.agent.deepspace;

import Group9.PiMath;
import Group9.math.Vector2;
import Group9.math.graph.Edge;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.AreaPercepts;
import Interop.Percept.GuardPercepts;

import javax.xml.crypto.Data;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeepSpace implements Guard {

    //--- Notes: @jan
    // 1. If I am not mistaken, we only have to move the agent twice to calculate the exact point where the target area
    // is. By simply calculating the intersection points of the general direction.
    // 2. to add paths to the graph, simply go through all vertices and see if they can be connected to another
    // by checking for collisions a long a straight path between the two vertices. might be expensive to run, and should
    // simply be but on a different thread, so it can be done while other agents perform their moves.

    private Vector2 position = new Vector2(0, 0);
    private Vector2 direction = new Vector2(0, 1).normalise();

    private Vertex<DataContainer> currentVertex;

    private List<Graph<DataContainer>> graphs = new ArrayList<>();
    private List<TeleportPosition> teleports = new ArrayList<>();
    protected Graph<DataContainer> currentGraph = new Graph<>();

    private StateType curState;
    private ActionContainer<GuardAction> lastAction = null;

    private final EnumMap<StateType, StateHandler> stateHandlers;

    private boolean insideTeleportArea = false;
    private boolean firstActionAfterTeleport = false;

    public DeepSpace() {
        this.graphs.add(currentGraph);

        curState = StateType.INITIAL;
        stateHandlers = new EnumMap<>(StateType.class);

        // Maps 'StateType' to 'instance of StateType state handler class'
        EnumSet.allOf(StateType.class).forEach(t -> {
            try {
                stateHandlers.put(t, t.getStateHandlerClass().getConstructor().newInstance());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        this.handlePossibleTeleport(percepts);

        ActionContainer<GuardAction> actionToDo = ActionContainer.of(this, new NoAction());

        if(!percepts.wasLastActionExecuted())
        {
            System.out.println("well");
        }
        assert percepts.wasLastActionExecuted() : "Last action executed wasn't valid!";

        // if we moved/rotated, we update our current status
        if (percepts.wasLastActionExecuted() && lastAction != null) {
            if (lastAction.getAction() instanceof Move) {
                move(((Move) lastAction.getAction()).getDistance().getValue());
            } else if (lastAction.getAction() instanceof Rotate) {
                rotate(((Rotate) lastAction.getAction()).getAngle().getRadians());
            }
        }

        assert curState != null : "Current state is null. Not initialized?";

        do {
            actionToDo = stateHandlers.get(curState).execute(percepts, this);
            curState = stateHandlers.get(curState).getNextState();
        } while (actionToDo.getAction() instanceof Inaction || actionToDo.getAction() == null);

        lastAction = actionToDo;
        return actionToDo.getAction();
    }

    private void handlePossibleTeleport(GuardPercepts percepts)
    {
        if(!this.insideTeleportArea && percepts.getAreaPercepts().isJustTeleported())
        {
            this.insideTeleportArea = percepts.getAreaPercepts().isJustTeleported();
            this.firstActionAfterTeleport = true;
        }
        else if(insideTeleportArea && percepts.getAreaPercepts().isJustTeleported())
        {
            this.firstActionAfterTeleport = false;
        }
        else {
            this.insideTeleportArea = false;
        }

        if(this.firstActionAfterTeleport)
        {
            Graph<DataContainer> newGraph = new Graph<>();
            this.teleports.add(new TeleportPosition(
                    this.position.clone(), this.currentGraph,
                    new Vector2.Origin(), newGraph
            ));
            this.graphs.add(newGraph);
            this.currentGraph = newGraph;
            this.position = new Vector2.Origin();
            this.direction = new Vector2(0, 1).normalise();
        }

    }

    double calculateCost(Vertex<DataContainer> currentVertex, Vertex<DataContainer> newVertex) {
        //TODO it would be great if we had a more sophisticated cost function e.g. make it more expensive to not
        // go through a sentry tower, or make it more expensive to walk towards a target which we can not pass through, etc.
        return currentVertex.getContent().getCenter().distance(newVertex.getContent().getCenter());
    }

    /**
     * TODO I think the current issue is within this method. It seems to generate an invalid move pattern for the agent
     *      causing it to get stuck.
     * @param guardPercepts
     */
    public Queue<ActionContainer<GuardAction>> backtrack(GuardPercepts guardPercepts)
    {
        Queue<ActionContainer<GuardAction>> retActionsQueue = new LinkedList<>();

        Set<Vertex<DataContainer>> visited = new HashSet<>();

        Function<Vertex<DataContainer>, LinkedList<Vertex<DataContainer>>> a = new Function<>() {


            @Override
            public LinkedList<Vertex<DataContainer>> apply(Vertex<DataContainer> dataContainerVertex) {
                LinkedList<Vertex<DataContainer>> list = currentGraph.getNeighbours(dataContainerVertex)
                        .stream()
                        .map(Edge::getEnd)
                        .filter(e -> !visited.contains(e))
                        .collect(Collectors.toCollection(LinkedList::new));
                return list;
            }
        };

        Vertex<DataContainer> vertex = null;
        Queue<Vertex<DataContainer>> vertices = new LinkedList<>();
        vertices.add(currentVertex);
        do {
            vertex = vertices.poll();
            vertices.addAll(a.apply(vertex));
            visited.add(vertex);

            if(!vertex.getContent().isDeadend())
            {

                List<Vertex<DataContainer>> shortestPath = this.currentGraph.shortestPath(this.currentVertex, vertex);
                System.out.println("|path| = " + shortestPath.size());
                System.out.println(shortestPath.get(0).getContent().getCenter());
                System.out.println(shortestPath.get(shortestPath.size() - 1).getContent().getCenter());

                //--- walk to the center of the vertex it is currently exploring
                if(this.position.distance(currentVertex.getContent().getCenter()) > 1E-8)
                {
                    retActionsQueue.addAll(moveTowardsPoint(guardPercepts, this.direction, this.position, vertex.getContent().getCenter()));
                }

                //--- if the path has only length 2 then we are only walking from the current vertex to a previous vertex
                if(shortestPath.size() == 2)
                {
                    retActionsQueue.addAll(moveTowardsPoint(guardPercepts, this.direction,
                            this.position, shortestPath.get(1).getContent().getCenter()));
                }
                else
                {
                    for (int i = 0; i < shortestPath.size() - 2; i++) {
                        Vector2 s = shortestPath.get(0 + i).getContent().getCenter();
                        Vector2 c = shortestPath.get(1 + i).getContent().getCenter();
                        Vector2 n = shortestPath.get(2 + i).getContent().getCenter();
                        retActionsQueue.addAll(moveTowardsPoint(guardPercepts, c.sub(s).normalise(), c, n));
                    }
                }

            }
        } while (!vertices.isEmpty());

        return retActionsQueue;
    }

    protected boolean isInsideOtherVertex(Vertex<?> own, Vector2 position)
    {
        return currentGraph.getVertices().stream()
                .anyMatch(e -> e != own && !e.getContent().isDeadend() && e.getContent().getAsCircle().isInside(position));
    }

    protected Queue<ActionContainer<GuardAction>> moveTowardsPoint(GuardPercepts percepts, Vector2 direction, Vector2 source, Vector2 target)
    {
        Queue<ActionContainer<GuardAction>> retActionsQueue = new LinkedList<>();

        Vector2 desiredDirection = target.sub(source).normalise();
        double rotationDiff = PiMath.getDistanceBetweenAngles(direction.getClockDirection(), desiredDirection.getClockDirection());

        if(rotationDiff < 0)
        {
            rotationDiff += Math.PI * 2;
        }
        if(Math.abs(rotationDiff) > 1E-10)
        {
            retActionsQueue.addAll(this.planRotation(percepts, rotationDiff));
        }

        final double maxAllowedMove = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
        final double distance = target.distance(source);
        final int fullMoves = (int) (distance / maxAllowedMove);
        final double remainder = distance % percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();

        ActionContainer.Input input = ActionContainer.Input.create()
                .i("direction", direction).i("source", source).i("target", target).i("maxAllowedMove", maxAllowedMove)
                .i("distance", distance).i("fullMoves", fullMoves).i("remainder", remainder);
        for(int i = 0; i < fullMoves; i++)
        {
            retActionsQueue.add(
                ActionContainer.of(this, new Move(new Distance(maxAllowedMove)), input.clone().i("#fullMoves-i", i))
            );
        }
        if(remainder > 0)
        {
            retActionsQueue.add(
                ActionContainer.of(this, new Move(new Distance(remainder)), input.clone().i("#remainder", remainder))
            );
        }

        return retActionsQueue;
    }

    protected Queue<ActionContainer<GuardAction>> planRotation(GuardPercepts percepts, double alpha)
    {
        Queue<ActionContainer<GuardAction>> retActionsQueue = new LinkedList<>();

        double maxRotation = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
        int fullRotations = (int) (alpha / maxRotation);
        double restRotation = alpha % maxRotation;

        for (int i = 0; i < fullRotations; i++)  {
            retActionsQueue.offer(
                ActionContainer.of(this, new Rotate(Angle.fromRadians(maxRotation)))
            );
        }

        if (restRotation > 0) {
            retActionsQueue.offer(
                ActionContainer.of(this, new Rotate(Angle.fromRadians(restRotation)))
            );
        }

        return retActionsQueue;
    }

    private void move(double distance)
    {
        this.position = this.position.add(this.direction.mul(distance, distance));
    }

    private void rotate(double theta)
    {
        this.direction = direction.rotated(theta);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Vertex<DataContainer> getCurrentVertex() {
        return currentVertex;
    }

    public Vertex<DataContainer> setCurrentVertex(Vertex<DataContainer> currentVertex) {
        this.currentVertex = currentVertex;
        return currentVertex;
    }

    private class TeleportPosition {

        private final Vector2 fromPosition;
        private final Graph<DataContainer> fromGraph;

        private final Vector2 toPosition;
        private final Graph<DataContainer> toGraph;

        public TeleportPosition(Vector2 fromPosition, Graph<DataContainer> fromGraph, Vector2 toPosition, Graph<DataContainer> toGraph)
        {
            this.fromPosition = fromPosition;
            this.fromGraph = fromGraph;
            this.toPosition = toPosition;
            this.toGraph = toGraph;
        }

        public Graph<DataContainer> fromGraph() {
            return fromGraph;
        }

        public Graph<DataContainer> toGraph() {
            return toGraph;
        }

        public Vector2 fromPosition() {
            return fromPosition;
        }

        public Vector2 toPosition() {
            return toPosition;
        }
    }
}
