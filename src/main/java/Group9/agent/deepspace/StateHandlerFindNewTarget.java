package Group9.agent.deepspace;

import Group9.math.Vector2;
import Group9.math.graph.Edge;
import Group9.math.graph.Vertex;
import Group9.tree.PointContainer;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StateHandlerFindNewTarget implements StateHandler {

    private DeepSpace ds;
    private StateType nextState;
    private boolean active;

    private boolean backtrackedToKnownState = false;
    private boolean movingIntoTargetArea = false;

    private final Queue<ActionContainer<GuardAction>> actionsQueue = new LinkedList<>();

    private final int TELEPORT_PRIORITY_TURNS = 100;
    private int teleportPriorityChange = -1;

    private boolean initialRoundAfterTeleport = false;

    private boolean testSquare = false;

    @Override
    public ActionContainer<GuardAction> execute(GuardPercepts percepts, DeepSpace deepSpace) {
        ActionContainer<GuardAction> retAction = ActionContainer.of(this, new NoAction());

        this.ds = deepSpace;

        initialRoundAfterTeleport = percepts.getAreaPercepts().isJustTeleported() && !initialRoundAfterTeleport && teleportPriorityChange == -1;
        if(initialRoundAfterTeleport)
        {
            this.teleportPriorityChange = TELEPORT_PRIORITY_TURNS;
        }

        if (!active) {
            // add actions to queue to get to new best target
            if(testSquare)
            {
                Vector2 target = ds.getPosition().add(ds.getDirection().rotated(Math.PI / 2).mul(4));
                actionsQueue.addAll(
                        ds.moveTowardsPoint(percepts, ds.getDirection(), ds.getPosition(), target)
                );
            }
            else
            {
                findNewTarget(percepts);
                active = true;
            }
        }

        // if this is an ongoing state, execute queue
        if (!actionsQueue.isEmpty()) {
            retAction = actionsQueue.poll();

            // --- check for collision
            if(retAction.getAction() instanceof Move)
            {
                Move action = (Move) retAction.getAction();

                {
                    Vector2 position = new Vector2.Origin();
                    Vector2 direction = new Vector2(0, 1).normalise();

                    final double length = action.getDistance().getValue() + 0.5D;
                    final Vector2 end = direction.mul(length);
                    PointContainer.Line line = new PointContainer.Line(position, end);

                    final Vector2 move = direction.mul(length);

                    Vector2 pointA = position.add(line.getNormal());
                    Vector2 pointB = pointA.add(move);
                    Vector2 pointD = position.sub(line.getNormal());
                    Vector2 pointC = pointD.add(move);

                    PointContainer.Polygon quadrilateral = new PointContainer.Polygon(pointA, pointB, pointC, pointD);

                    boolean collision = percepts.getVision().getObjects()
                            .filter(e -> e.getType().isSolid())
                            .getAll().stream().anyMatch(e -> quadrilateral.isPointInside(Vector2.from(e.getPoint())));

                    if(collision)
                    {
                        retAction = ActionContainer.of(this, new NoAction(), ActionContainer.Input.create().i("collision", true));
                        actionsQueue.clear();
                    }
                }
            }

        }

        postExecute();
        return retAction;
    }

    @Override
    public StateType getNextState() {
        return nextState;
    }

    private void postExecute() {
        if(testSquare) {
            nextState = StateType.FIND_NEW_TARGET;
            return;
        }
        if (actionsQueue.isEmpty()) {
            if(this.backtrackedToKnownState)
            {
                nextState = StateType.FIND_NEW_TARGET;
            }
            else if(this.movingIntoTargetArea)
            {
                nextState = StateType.GUARD_TARGET_AREA;
            }
            else
            {
                nextState = StateType.EXPLORE_360;
            }
            active = false;
            this.movingIntoTargetArea = false;
            this.backtrackedToKnownState = false;
        } else {
            nextState = StateType.FIND_NEW_TARGET;
        }
    }

    public void resetState()  {
        this.actionsQueue.clear();
        this.active = false;
        this.movingIntoTargetArea = false;
        this.backtrackedToKnownState = false;
    }

    private void findNewTarget(GuardPercepts guardPercepts)
    {

        Map<ObjectPerceptType, HashSet<Vector2>> map = ds.getCurrentVertex().getContent().getObjects();
        ObjectPerceptType[] priority = new ObjectPerceptType[] {ObjectPerceptType.TargetArea, ObjectPerceptType.SentryTower,
                ObjectPerceptType.Door, ObjectPerceptType.Window, ObjectPerceptType.Teleport, ObjectPerceptType.EmptySpace,
                ObjectPerceptType.Wall};

        // TODO Luis -> Dynamic priority list so that we no longer get stuck somewhere :)
        if(teleportPriorityChange >= 0)
        {
            teleportPriorityChange--;
            priority = new ObjectPerceptType[] {ObjectPerceptType.TargetArea, ObjectPerceptType.SentryTower,
                    ObjectPerceptType.Door, ObjectPerceptType.Window, ObjectPerceptType.EmptySpace, ObjectPerceptType.Wall,
                    ObjectPerceptType.Teleport};
        }
        else
        {
            initialRoundAfterTeleport = false;
        }

        for(ObjectPerceptType type : priority)
        {
            Set<Vector2> positions = ds.currentVertex.getContent().getObjects().get(type);
            if(positions != null && !positions.isEmpty())
            {
                Optional<Vector2> target = positions.stream().filter(e -> !ds.isInsideOtherVertex(ds.getCurrentVertex(), e, 0.8)).findAny();
                if(target.isPresent())
                {
                    Vector2 targetPosition = target.get();
                    //--- if we move into a target area then we want to transition into the guarding state
                    if(type == ObjectPerceptType.TargetArea)
                    {
                        this.movingIntoTargetArea = true;
                    }
                    else if(type.isSolid())
                    {
                        targetPosition = targetPosition.sub(ds.getPosition().sub(targetPosition).normalise().mul(0.5));
                    }

                    actionsQueue.addAll(ds.moveTowardsPoint(guardPercepts, ds.getDirection(), ds.getPosition(), targetPosition));
                    // todo: Maybe Hoare might have a point this time
                    return;
                }
            }
        }

        ds.getCurrentVertex().getContent().setDeadEnd(true);

        // backtrack: get a list of actions to move us to a prev node/position that didn't lead to (or wasn't) a deadend
        Queue<ActionContainer<GuardAction>> actions = this.backtrack(guardPercepts);
        if(actions.isEmpty())
        {
            System.out.println("Count: " + Arrays.toString(ds.currentGraph.getVertices().stream().filter(e -> !e.getContent().isDeadEnd()).toArray()));
        }
        else
        {
            this.backtrackedToKnownState = true;
            actionsQueue.addAll(actions);
        }
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
                LinkedList<Vertex<DataContainer>> list = ds.currentGraph.getNeighbours(dataContainerVertex)
                        .stream()
                        .map(Edge::getEnd)
                        .filter(e -> !visited.contains(e))
                        .collect(Collectors.toCollection(LinkedList::new));
                return list;
            }
        };

        Vertex<DataContainer> vertex = null;
        Queue<Vertex<DataContainer>> vertices = new LinkedList<>();
        vertices.add(ds.currentVertex);
        do {
            vertex = vertices.poll();
            vertices.addAll(a.apply(vertex));
            visited.add(vertex);

            if(!vertex.getContent().isDeadEnd() && vertex != ds.currentVertex)
            {

                List<Vertex<DataContainer>> shortestPath = ds.currentGraph.shortestPath(ds.currentVertex, vertex);
                System.out.println("|path| = " + shortestPath.size());
                System.out.println(shortestPath.get(0).getContent().getCenter());
                System.out.println(shortestPath.get(shortestPath.size() - 1).getContent().getCenter());

                //--- walk to the center of the vertex it is currently exploring
                if(ds.getPosition().distance(ds.currentVertex.getContent().getCenter()) > 1E-8)
                {
                    retActionsQueue.addAll(ds.moveTowardsPoint(guardPercepts, ds.getDirection(), ds.getPosition(), vertex.getContent().getCenter()));
                }

                //--- if the path has only length 2 then we are only walking from the current vertex to a previous vertex
                if(shortestPath.size() == 2)
                {
                    retActionsQueue.addAll(ds.moveTowardsPoint(guardPercepts, ds.getDirection(),
                            ds.getPosition(), shortestPath.get(1).getContent().getCenter()));
                }
                else
                {
                    for (int i = 0; i < shortestPath.size() - 2; i++) {
                        Vector2 s = shortestPath.get(0 + i).getContent().getCenter();
                        Vector2 c = shortestPath.get(1 + i).getContent().getCenter();
                        Vector2 n = shortestPath.get(2 + i).getContent().getCenter();
                        retActionsQueue.addAll(ds.moveTowardsPoint(guardPercepts, c.sub(s).normalise(), c, n));
                    }
                }
                break;
            }
        } while (!vertices.isEmpty());

        return retActionsQueue;
    }
}
