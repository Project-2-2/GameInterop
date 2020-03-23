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
import Interop.Geometry.Vector;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private Vector2 target;

    private final Graph<DataContainer> graph = new Graph<>();

    public ExplorerAgent() {}

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        if(!percepts.wasLastActionExecuted())
        {
            this.state = State.ROTATE;
            System.out.println(actionHistory.action);
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
        Vertex<DataContainer> closestVertex = getVertex(position, percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue())
                .orElseGet(() -> {
                    Vertex<DataContainer> vertex = new Vertex<>(new DataContainer(this, ExplorerAgent.this.position));
                    graph.add(vertex);
                    return vertex;
                });

        switch (state)
        {

            case ROTATE:
                if(currentRotation > 0)
                {
                    closestVertex.getContent().add(percepts);
                    double angle = percepts.getScenarioGuardPercepts().getScenarioPercepts().getMaxRotationAngle().getRadians();
                    currentRotation -= angle;

                    if(currentRotation <= 0 )
                    {
                        this.state = State.EXPLORE;
                        this.currentRotation = Math.PI * 2;
                    }

                    this.actionHistory = new ActionHistory<>(ActionHistory.Action.ROTATE, angle);
                    return new Rotate(Angle.fromRadians(angle));
                }
                break;

            case EXPLORE:
            {
                if(this.target == null)
                {
                    Optional<Vector2> target = closestVertex.getContent().getPointOfInterest(exploredTargets);
                    if(target.isPresent())
                    {
                        this.target = target.get();
                    }
                    else
                    {
                        System.out.println("no new target :(");
                        break;
                    }
                }

                Vector2 desiredDirection = target.sub(position).normalise();
                double rotationDiff = PiMath.getDistanceBetweenAngles(direction.getClockDirection(), desiredDirection.getClockDirection());

                if(Math.abs(rotationDiff) < 1E-14)
                {
                    double distance = Math.min(target.distance(position), percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard().getValue());
                    this.actionHistory = new ActionHistory<>(ActionHistory.Action.MOVE, distance);

                    if(target.distance(position) - distance < 1E-4)
                    {
                        exploredTargets.add(target);
                        target = null;
                        this.state = State.ROTATE;
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
        return graph.getVertices().stream().filter(e -> e.getContent().getPosition().distance(location) < maxDistance)
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
        private Map<ObjectPerceptType, List<Vector2>> objects = new HashMap<>();

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
                    .flatMap((Function<Map.Entry<ObjectPerceptType, List<Vector2>>, Stream<Vector2>>) objectPerceptTypeListEntry -> objectPerceptTypeListEntry.getValue().stream())
                    .filter(e -> !exclude.contains(e))
                    .filter(e -> !explorerAgent.getVertex(e, 1).isPresent()) //TODO use correct maxDistance
                    .max(Comparator.comparingDouble(o -> o.distance(position)));

        }

        public void add(GuardPercepts percepts)
        {
            this.percepts.add(percepts);
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
                    this.objects.put(e.getType(), new ArrayList<>() {{
                        this.add(coordinate);
                    }});
                }
            });
            System.out.println("angle-b: " + explorerAgent.direction.getClockDirection());
            System.out.println("B:\n" + objects.get(ObjectPerceptType.Wall).stream().map(new Function<Vector2, String>() {
                @Override
                public String apply(Vector2 objectPercept) {
                    return String.format("(%.2f,%.2f)", objectPercept.getX(), objectPercept.getY());
                }
            }).collect(Collectors.joining(",")));
        }

        public List<GuardPercepts> getPercepts() {
            return percepts;
        }
    }

}
