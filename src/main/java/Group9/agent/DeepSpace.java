package Group9.agent;

import Group9.PiMath;
import Group9.math.Vector2;
import Group9.math.graph.Edge;
import Group9.math.graph.Graph;
import Group9.math.graph.Vertex;
import Group9.tree.PointContainer;
import Interop.Action.GuardAction;
import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPerceptType;

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

    private State state = State.INITIAL;
    private Vector2 position = new Vector2(0, 0);
    private Vector2 direction = new Vector2(0, 1).normalise();

    private Vertex<DataContainer> currentVertex;

    private final Graph<DataContainer> graph = new Graph<>();

    private ActionHistory<?> actionHistory = null;
    private Queue<ActionHistory> planning = new LinkedList<>();

    public DeepSpace() { }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        assert percepts.wasLastActionExecuted();
        if(percepts.wasLastActionExecuted() && this.actionHistory != null)
        {
            switch (actionHistory.getAction())
            {
                case MOVE:
                    move(actionHistory.<Double>getValue());
                    break;
                case ROTATE:
                    rotate(actionHistory.<Double>getValue());
                    break;
            }
        }

        if(!planning.isEmpty())
        {
            ActionHistory<?> actionHistory = planning.poll();
            this.actionHistory = actionHistory;
            switch (actionHistory.action)
            {
                case SWITCH_STATE: this.state = actionHistory.getValue(); break;
                case MOVE: return new Move(new Distance(actionHistory.<Double>getValue()));
                case ROTATE: return new Rotate(Angle.fromRadians(actionHistory.<Double>getValue()));

                default: throw new IllegalArgumentException(String.format("%s is not supported.", actionHistory.action));
            }
        }

        switch (state)
        {
            case EXPLORE_360_ADD_VERTEX:
                System.out.println("add vertex");
                Vertex<DataContainer> newVertex = new Vertex<>(new DataContainer(this, this.position.clone(),
                        percepts.getVision().getFieldOfView().getRange().getValue()));
                if(currentVertex != null)
                {
                    graph.addEdge(this.currentVertex, newVertex, calculateCost(currentVertex, newVertex), true);
                }
                graph.add(this.currentVertex = newVertex);
                this.planning.offer(new ActionHistory<>(ActionHistory.Action.SWITCH_STATE, State.EXPLORE_360));
                break;
            case EXPLORE_360:
                this.currentVertex.getContent().add(percepts);
            break;

            case FIND_NEW_TARGET:
                findNewTarget(percepts);
                break;

            default: this.explorePoint(percepts); break;
        }


        return new NoAction();
    }

    private double calculateCost(Vertex<DataContainer> currentVertex, Vertex<DataContainer> newVertex) {
        //TODO it would be great if we had a more sophisticated cost function e.g. make it more expensive to not
        // go through a sentry tower, or make it more expensive to walk towards a target which we can not pass through, etc.
        return currentVertex.getContent().getCenter().distance(newVertex.getContent().getCenter());
    }

    private void findNewTarget(GuardPercepts guardPercepts)
    {
        Map<ObjectPerceptType, HashSet<Vector2>> map = currentVertex.getContent().getObjects();
        ObjectPerceptType[] priority = new ObjectPerceptType[] {ObjectPerceptType.SentryTower, ObjectPerceptType.Door,
                                            ObjectPerceptType.Window, ObjectPerceptType.Teleport, ObjectPerceptType.EmptySpace};

        for(ObjectPerceptType type : priority)
        {
            Set<Vector2> positions = map.get(type);
            if(positions != null && !positions.isEmpty())
            {
                Optional<Vector2> target = positions.stream().filter(e -> !isInsideOtherVertex(currentVertex, e)).findAny();
                if(target.isPresent())
                {
                    this.moveTowardsPoint(guardPercepts, this.direction, target.get());
                    this.explorePoint(guardPercepts);
                    return;
                }
            }
        }

        this.currentVertex.getContent().setDeadend(true);
        this.backtrack(guardPercepts);
        System.out.println("well, we couldn't find a new target here");
    }


    public void backtrack(GuardPercepts guardPercepts)
    {
        Function<Vertex<DataContainer>, LinkedList<Vertex<DataContainer>>> a = new Function<>() {

            private Set<Vertex<DataContainer>> visited = new HashSet<>();

            @Override
            public LinkedList<Vertex<DataContainer>> apply(Vertex<DataContainer> dataContainerVertex) {
                LinkedList<Vertex<DataContainer>> list = graph.getNeighbours(dataContainerVertex)
                        .stream()
                        .map(Edge::getEnd)
                        .filter(e -> !visited.contains(e))
                        .collect(Collectors.toCollection(LinkedList::new));
                visited.addAll(list);
                return list;
            }
        };

        Vertex<DataContainer> vertex = currentVertex;
        Queue<Vertex<DataContainer>> vertices = new LinkedList<>();
        do {
            vertices.addAll(a.apply(vertex));
            vertex = vertices.poll();

            if(!vertex.getContent().isDeadend())
            {

                List<Vertex<DataContainer>> shortestPath = this.graph.shortestPath(this.currentVertex, vertex);

                if(currentVertex.getContent().getCenter().distance(position) > 1E-9)
                {
                    moveTowardsPoint(guardPercepts, this.direction, currentVertex.getContent().getCenter());
                }

                if(shortestPath.size() == 2)
                {
                    moveTowardsPoint(guardPercepts, this.direction, shortestPath.get(1).getContent().getCenter());
                }
                else
                {
                    for (int i = 0; i < shortestPath.size() - 2; i++) {
                        Vector2 s = shortestPath.get(0 + i).getContent().getCenter();
                        Vector2 c = shortestPath.get(1 + i).getContent().getCenter();
                        Vector2 n = shortestPath.get(2 + i).getContent().getCenter();
                        moveTowardsPoint(guardPercepts, c.sub(s).normalise(), n);
                    }
                }

                planning.add(new ActionHistory<State>(ActionHistory.Action.SWITCH_STATE, State.FIND_NEW_TARGET));

                return;
            }
        } while (!vertices.isEmpty());
    }

    private boolean isInsideOtherVertex(Vertex<?> own, Vector2 position)
    {
        return graph.getVertices().stream()
                .anyMatch(e -> e != own && !e.getContent().isDeadend() && e.getContent().getAsCircle().isInside(position));
    }

    private void moveTowardsPoint(GuardPercepts percepts, Vector2 direction, Vector2 target)
    {
        Vector2 desiredDirection = target.sub(this.position).normalise();
        double rotationDiff = PiMath.getDistanceBetweenAngles(direction.getClockDirection(), desiredDirection.getClockDirection());

        if(Math.abs(rotationDiff) > 1E-10)
        {
            this.planRotation(percepts, rotationDiff);
        }

        final double maxAllowedMove = percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();

        double distance = target.distance(position);
        int fullMoves = (int) (distance / maxAllowedMove);
        double remainder = distance % percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue();
        for(int i = 0; i < fullMoves; i++)
        {
            planning.add(new ActionHistory<>(ActionHistory.Action.MOVE, maxAllowedMove));
        }
        if(remainder > 0)
        {
            planning.add(new ActionHistory<>(ActionHistory.Action.MOVE, remainder));
        }
    }

    private void planRotation(GuardPercepts percepts, double alpha)
    {
        double maxRotation = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
        int fullRotations = (int) (alpha / maxRotation);
        double restRotation = alpha % maxRotation;
        for(int i = 0; i < fullRotations; i++)
        {
            planning.offer(new ActionHistory<>(ActionHistory.Action.ROTATE, maxRotation));
        }
        if(restRotation > 0)
        {
            planning.offer(new ActionHistory<>(ActionHistory.Action.ROTATE, restRotation));
        }
    }

    private void explorePoint(GuardPercepts guardPercepts)
    {
        this.planning.offer(new ActionHistory<>(ActionHistory.Action.SWITCH_STATE, State.EXPLORE_360_ADD_VERTEX));
        this.planning.offer(new ActionHistory<>(ActionHistory.Action.SWITCH_STATE, State.EXPLORE_360));
        this.planRotation(guardPercepts, Math.PI * 2);
        planning.offer(new ActionHistory<>(ActionHistory.Action.SWITCH_STATE, State.FIND_NEW_TARGET));
    }

    private void move(double distance)
    {
        this.position = this.position.add(this.direction.mul(distance, distance));
    }

    private void rotate(double theta)
    {
        this.direction = direction.rotated(theta);
    }

    private enum State
    {
        INITIAL,
        EXPLORE_360,
        EXPLORE_360_ADD_VERTEX,
        FIND_NEW_TARGET
    }

    private static class ActionHistory<T> {

        private Action action;
        private T value;

        public ActionHistory(Action action, T value)
        {
            this.action = action;
            this.value = value;
        }

        public Action getAction() {
            return action;
        }

        public <T> T getValue() {
            return (T) value;
        }

        public static enum Action {
            SWITCH_STATE,

            MOVE,
            ROTATE
        }

    }

    public static class DataContainer extends PointContainer.Circle {

        private DeepSpace explorerAgent;

        private List<GuardPercepts> percepts = new ArrayList<>();
        private Map<ObjectPerceptType, HashSet<Vector2>> objects = new HashMap<>();

        private boolean deadend;

        public DataContainer(DeepSpace explorerAgent, Vector2 position, double radius) {
            super(position.clone(), radius);
            this.explorerAgent = explorerAgent;
        }

        public Map<ObjectPerceptType, HashSet<Vector2>> getObjects() {
            return objects;
        }

        public boolean isDeadend() {
            return deadend;
        }

        public void setDeadend(boolean deadend) {
            this.deadend = deadend;
        }

        public void add(GuardPercepts percepts)
        {
            percepts.getVision().getObjects().getAll().forEach(e -> {
                Vector2 coordinate = Vector2.from(e.getPoint())
                        .rotated(-explorerAgent.direction.getClockDirection())
                        .add(explorerAgent.position);
                if(this.objects.containsKey(e.getType()))
                {
                    this.objects.get(e.getType()).add(coordinate);
                }
                else
                {
                    this.objects.put(e.getType(), new HashSet<>() {{
                        this.add(coordinate);
                    }});
                }
            });
        }

        public List<GuardPercepts> getPercepts() {
            return percepts;
        }
    }

}
