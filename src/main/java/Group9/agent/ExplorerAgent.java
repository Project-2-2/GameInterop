package Group9.agent;

import Group9.PiMath;
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
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExplorerAgent implements Guard {

    //--- Notes: @jan
    // 1. If I am not mistaken, we only have to move the agent twice to calculate the exact point where the target area
    // is. By simply calculating the intersection points of the general direction.

    private State state = State.ROTATE;
    private Vector2 position = new Vector2(0, 0);
    private Vector2 direction = new Vector2(0, 1).normalise();
    private ActionHistory<?> actionHistory = null;

    private double currentRotation = Math.PI * 2;

    private Set<Vector2> exploredTargets = new HashSet<>();
    private Vertex<DataContainer> currentVertex;

    private final Graph<DataContainer> graph = new Graph<>();

    private Stack<Vector2> targets = new Stack<>();
    private boolean findNewTarget = true;

    public ExplorerAgent() {}

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        if(!percepts.wasLastActionExecuted())
        {
            System.out.println(actionHistory.action);
            return new NoAction();
        }


        if(percepts.wasLastActionExecuted() && actionHistory != null)
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

        //----
        if(graph.getVertices().isEmpty())
        {
            graph.add(this.currentVertex = new Vertex<>(new DataContainer(this, ExplorerAgent.this.position)));
        }

        switch (state)
        {

            case ROTATE:
                if(currentRotation > 0)
                {
                    currentVertex.getContent().add(percepts);
                    double angle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
                    currentRotation -= angle;

                    if(currentRotation <= 0)
                    {
                        for(Vertex<DataContainer> a : graph.getVertices())
                        {
                            Set<Vector2> a_hash = a.getContent().objects
                                    .values().stream()
                                    .flatMap((Function<HashSet<Vector2>, Stream<Vector2>>) Collection::stream)
                                    .collect(Collectors.toCollection(HashSet::new));
                            for(Vertex<DataContainer> b : graph.getVertices())
                            {
                                if(a == b) continue;
                                Set<Vector2> b_hash = b.getContent().objects
                                        .values().stream()
                                        .flatMap((Function<HashSet<Vector2>, Stream<Vector2>>) Collection::stream)
                                        .collect(Collectors.toCollection(HashSet::new));

                                Set<Vector2> intersection = a_hash.stream().distinct()
                                        .filter(e -> b_hash.contains(e) || b_hash.stream().anyMatch(c -> c.distance(e) < 0.1))
                                        .filter(e -> !exploredTargets.contains(e))
                                        .collect(Collectors.toSet());
                                System.out.println("intersection: " + intersection.size());
                                exploredTargets.addAll(intersection);
                            }
                        }
                        this.state = State.EXPLORE;
                        this.currentRotation = Math.PI * 2;
                    }

                    this.actionHistory = new ActionHistory<>(ActionHistory.Action.ROTATE, angle);
                    return new Rotate(Angle.fromRadians(angle));
                }
                break;

            case EXPLORE:
            {
                if(findNewTarget)
                {
                    Optional<Vector2> target = currentVertex.getContent().getPointOfInterest(exploredTargets);
                    if(target.isPresent())
                    {
                        this.targets.push(target.get());
                        this.currentVertex = new Vertex<>(new DataContainer(this, this.targets.peek()));
                        graph.add(this.currentVertex);
                        findNewTarget = false;
                    }
                    else
                    {
                        System.out.println("no new target :(");
                        break;
                    }
                }

                Vector2 desiredDirection = targets.peek().sub(position).normalise();
                double rotationDiff = PiMath.getDistanceBetweenAngles(direction.getClockDirection(), desiredDirection.getClockDirection());

                if(Math.abs(rotationDiff) < 1E-14)
                {
                    double distance = Math.min(targets.peek().distance(position), percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue());
                    this.actionHistory = new ActionHistory<>(ActionHistory.Action.MOVE, distance);

                    if(targets.peek().distance(position) - distance < 1E-4)
                    {
                        this.state = State.ROTATE;
                        findNewTarget = true;
                    }
                    return new Move(new Distance(distance));
                }
                else
                {
                    double angle = PiMath.clamp(-rotationDiff,
                            -percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians(),
                            percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians());
                    this.actionHistory = new ActionHistory<>(ActionHistory.Action.ROTATE, angle);
                    return new Rotate(Angle.fromRadians(angle));
                }
            }

        }


        return new NoAction();
    }

    private void move(double distance)
    {
        this.position = this.position.add(this.direction.mul(distance, distance));
    }

    private void rotate(double theta)
    {
        this.direction = direction.rotated(theta);
    }

    private Optional<Vertex<DataContainer>> getVertex(Vector2 location, double maxDistance)
    {
        return graph.getVertices().stream()
                .filter(e -> e.getContent().getPosition().distance(location) < maxDistance)
                .min(Comparator.comparingDouble(o -> o.getContent().getPosition().distance(location)));
    }

    private enum State
    {
        ROTATE,
        EXPLORE
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
            MOVE,
            ROTATE
        }

    }

    public static class DataContainer {

        private ExplorerAgent explorerAgent;
        private Vector2 position;

        private List<GuardPercepts> percepts = new ArrayList<>();
        private Map<ObjectPerceptType, HashSet<Vector2>> objects = new HashMap<>();

        private boolean explored = false;

        public DataContainer(ExplorerAgent explorerAgent, Vector2 position) {
            this.explorerAgent = explorerAgent;
            this.position = position.clone();
        }

        public Vector2 getPosition() {
            return position;
        }

        public Optional<Vector2> getPointOfInterest(Set<Vector2> exclude)
        {
            return objects.entrySet().stream()
                    .filter(e -> !e.getKey().isSolid())
                    .flatMap((Function<Map.Entry<ObjectPerceptType, HashSet<Vector2>>, Stream<Vector2>>) objectPerceptTypeListEntry -> objectPerceptTypeListEntry.getValue().stream())
                    .filter(e -> !exclude.contains(e))
                    .max(Comparator.comparingDouble(o -> o.distance(position)));

        }


        public boolean isExplored() {
            return explored;
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
            if(false && objects.containsKey(ObjectPerceptType.EmptySpace))
            {
                System.out.println("angle-b: " + explorerAgent.direction.getClockDirection());
                System.out.println("B:\n" + objects.get(ObjectPerceptType.EmptySpace).stream().map(new Function<Vector2, String>() {
                    @Override
                    public String apply(Vector2 objectPercept) {
                        return String.format("(%.2f,%.2f)", objectPercept.getX(), objectPercept.getY());
                    }
                }).collect(Collectors.joining(",")));
            }

        }

        public List<GuardPercepts> getPercepts() {
            return percepts;
        }
    }

}
