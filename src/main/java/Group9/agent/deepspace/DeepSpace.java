package Group9.agent.deepspace;

import Group9.Game;
import Group9.math.Vector2;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.VisionPrecepts;
import Interop.Geometry.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DeepSpace implements Guard {

    //--- Notes: @jan
    // 1. If I am not mistaken, we only have to move the agent twice to calculate the exact point where the target area
    // is. By simply calculating the intersection points of the general direction.
    // 2. to add paths to the graph, simply go through all vertices and see if they can be connected to another
    // by checking for collisions a long a straight path between the two vertices. might be expensive to run, and should
    // simply be but on a different thread, so it can be done while other agents perform their moves.

    private Vector2 position = new Vector2(0, 0);
    private Vector2 direction = new Vector2(0, 1).normalise();

    private boolean debug = true;

    protected Vertex<DataContainer> currentVertex;

    private List<Graph<DataContainer>> graphs = new ArrayList<>();
    private List<TeleportPosition> teleports = new ArrayList<>();
    protected Graph<DataContainer> currentGraph = new Graph<>();

    private StateType curState;
    private ActionContainer<GuardAction> lastAction = null;

    private final EnumMap<StateType, StateHandler> stateHandlers;
    private Angle rotation; //0 rotation -> positive Y, neutral X
    private boolean foundTargetArea = false;
    private boolean insideTeleportArea = false;
    protected boolean firstActionAfterTeleport = false;

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
        ActionContainer<GuardAction> actionToDo = ActionContainer.of(this, new NoAction());
        VisionPrecepts vision = percepts.getVision();
        Set<ObjectPercept> objectPercepts = vision.getObjects().getAll();
        if (foundTargetArea) {
            return doTargetAreaAction(percepts);
        }
        else {
            for (ObjectPercept object : objectPercepts) {
                if (object.getType().equals(ObjectPerceptType.TargetArea)) {
                    foundTargetArea = true;
                    break;
                }
            }
        }
        if(!percepts.wasLastActionExecuted() && debug)
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

        this.handlePossibleTeleport(percepts);

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

        this.insideTeleportArea =  percepts.getAreaPercepts().isJustTeleported();

        if(this.firstActionAfterTeleport)
        {
            Graph<DataContainer> newGraph = new Graph<>();
            this.teleports.add(new TeleportPosition(
                    this.position.clone(), this.currentGraph,
                    new Vector2.Origin(), newGraph
            ));
            this.graphs.add(newGraph);
            this.currentGraph = newGraph;
            this.currentVertex = null;
            this.position = new Vector2.Origin();
            this.stateHandlers.get(this.curState).resetState();
            this.curState = StateType.INITIAL;
        }

    }

    double calculateCost(Vertex<DataContainer> currentVertex, Vertex<DataContainer> newVertex) {
        //TODO it would be great if we had a more sophisticated cost function e.g. make it more expensive to not
        // go through a sentry tower, or make it more expensive to walk towards a target which we can not pass through, etc.
        return currentVertex.getContent().getCenter().distance(newVertex.getContent().getCenter());
    }

    protected boolean isInsideOtherVertex(Vertex<?> own, Vector2 position, final double radiusModifier)
    {
        return currentGraph.getVertices().stream()
                .anyMatch(e -> e != own && !e.getContent().isDeadEnd() &&
                        e.getContent().getCenter().distance(position) < radiusModifier * e.getContent().getRadius());
    }


    protected Queue<ActionContainer<GuardAction>> moveTowardsPoint(GuardPercepts percepts, Vector2 direction, Vector2 source,
                                                                   Vector2 target)
    {
        Queue<ActionContainer<GuardAction>> retActionsQueue = new LinkedList<>();

        Vector2 desiredDirection = target.sub(source);
        double rotationDiff = direction.angledSigned(desiredDirection);
        if(Math.abs(rotationDiff) > 1E-1)
        {
            retActionsQueue.addAll(planRotation(percepts, rotationDiff));
        }

        final double maxAllowedMove = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue() * getSpeedModifier(percepts);
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

    protected GuardAction doTargetAreaAction(GuardPercepts percepts) {
            Set<ObjectPercept> objects = percepts.getVision().getObjects().getAll();
            for (ObjectPercept object :objects) {
                if (object.getType().equals(ObjectPerceptType.TargetArea)) {
                    Move move = new Move(new Distance(object.getPoint(), new Point(0, 0)));
                    if (move.getDistance().getValue() >= percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue()) {
                        move = new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                    }
                    return move;
                }
            }
        Angle newRotation = Angle.fromRadians(percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians() * Game._RANDOM.nextDouble());
               if(newRotation.getDegrees() > 360){
            newRotation = Angle.fromDegrees(rotation.getDegrees() - 360);
        } else if(newRotation.getDegrees() < 0){
            rotation = Angle.fromDegrees(rotation.getDegrees() + 360);
        }
        return new Rotate(newRotation);
    }

    protected Queue<ActionContainer<GuardAction>> planRotation(GuardPercepts percepts, double alpha)
    {
        // TODO kinda cheating; fix
        //final double sign = Math.signum(alpha);
        //alpha = Math.abs(alpha);

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
