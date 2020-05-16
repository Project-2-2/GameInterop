package Group9.map;

import Group9.Game;
import Group9.PiMath;
import Group9.agent.container.AgentContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.area.EffectArea;
import Group9.map.dynamic.DynamicObject;
import Group9.map.objects.MapObject;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Group9.tree.QuadTree;
import Interop.Agent.Guard;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameMap {

    private final static boolean _OPTIMISE_RAYS = true;
    private final static boolean _OPTIMISED_FILTERED_OBJECTS = true;

    private final GameSettings gameSettings;

    private final double rayConstant;
    private QuadTree<MapObject> quadTree;
    private List<MapObject> mapObjects;

    private List<DynamicObject<?>> dynamicObjects = new ArrayList<>();

    private Game game;

    public GameMap(GameSettings gameSettings, List<MapObject> mapObjects)
    {
        this.gameSettings = gameSettings;
        this.mapObjects = mapObjects;

        this.rayConstant = this.calculateRayConstant();
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    /**
     * Calculates the amount of required rays based on the field of view. {@link GameMap#calculateRayConstant()}
     * @param fov
     * @return
     */
    public int calculateRequiredRays(FieldOfView fov)
    {
        return (int) Math.ceil(this.rayConstant * fov.getRange().getValue() * fov.getViewAngle().getRadians());
    }

    /**
     * Note: The specifications let the user specify the amount of rays that should be casted every time when we generate
     * the vision percepts for the agents, this is wasteful. There is a very efficient way to reduce the amount of rays,
     * and this is simply achieved by doing the following:
     *
     *      ceil((2 * a * r) / d) = n
     *          a -> view angle (rad)
     *          r -> view distance
     *          d -> width of smallest object divided by two
     *          n -> amount of rays required
     *
     *  The code below figures out what the smallest object is, and generates a constant that can simply be multiplied
     *  by a * r to get n. {@link GameMap#calculateRequiredRays(FieldOfView)}.
     *  Performing tests by letting play the same agent 100k turns, the newer method yielded a gain of 51% faster simulation
     *  computation.
     *
     * @return
     */
    private double calculateRayConstant()
    {

        double min = Math.min(AgentContainer._RADIUS,  // radius of agent
                gameSettings.getScenarioPercepts().getRadiusPheromone().getValue() / gameSettings.getPheromoneExpireRounds());

        Queue<PointContainer> containers = this.mapObjects.stream()
                .map(e -> {
                    List<PointContainer> pointContainers = new ArrayList<>();
                    pointContainers.add(e.getContainer());
                    pointContainers.addAll(e.getEffects().stream().map(EffectArea::getContainer).collect(Collectors.toList()));
                    return pointContainers;
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(LinkedList::new));

        for(PointContainer container : containers)
        {
            if(container instanceof PointContainer.Circle)
            {
                min = Math.min(min, container.getAsCircle().getRadius());
            }
            else if(container instanceof PointContainer.Polygon)
            {
                for(PointContainer.Line line : container.getAsPolygon().getLines())
                {
                    min = Math.min(min, line.getStart().distance(line.getEnd()) / 2);
                }
            }
            else
            {
                throw new IllegalArgumentException(String.format("Unsupported PointContainer: %s", container.getClass().getName()));
            }
        }

        return 2D / min;
    }

    /**
     * This function returns all map objects that have a chance of being seen by the agent. This is done by drawing a
     * line that is the normal to the agent's direction vector. Anything that lies on the wrong side of line gets culled
     * since it is impossible to see for the agent.
     *
     * @param agentContainer
     * @return
     */
    public List<MapObject> getFilteredObjects(AgentContainer<?> agentContainer, Predicate<MapObject> filter)
    {

        // --- If the field of view is greater than 180° (-> Pi) then this method does not work.
        if(gameSettings.getViewAngle().getRadians() >= Math.PI || !_OPTIMISED_FILTERED_OBJECTS) {
            if(filter == null)
            {
                return this.mapObjects;
            }
            return this.mapObjects.stream().filter(filter).collect(Collectors.toList());
        }

        // --- Create a line that is perpendicular to the direction vector
        Vector2 end = agentContainer.getPosition().add(agentContainer.getDirection());
        PointContainer.Line line = new PointContainer.Line(agentContainer.getPosition(), end);
        Vector2 normal = line.getNormal();
        Vector2 a = agentContainer.getPosition().add(normal);
        Vector2 b = agentContainer.getPosition().sub(normal);

        // --- Determine the sign of the end point. This tells us on which side of the line the objects are that we can see.
        final double sign = Math.signum((b.getX() - a.getX()) * (end.getY() - a.getY()) - (b.getY() - a.getY()) * (end.getX() - a.getX()));

        assert sign != 0;

        Function<Vector2, Boolean> keep = c -> {
            double r = (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());

            if(sign > 0)
            {
                return PiMath.geq(r, 0);
            }
            else if(sign < 0)
            {
                return PiMath.leq(r, 0);
            }
            return true;
        };

        Stream<MapObject> stream = this.mapObjects
                .stream();

        if(filter != null)
        {
            stream = stream.filter(filter);
        }

        return stream
                .filter(e -> {

                    // --- Note: This only supports polygons as of now.
                    if(!(e.getContainer() instanceof PointContainer.Polygon)) return true;

                    for(Vector2 c : e.getArea().getAsPolygon().getPoints())
                    {
                        if(keep.apply(c)) return true;
                    }

                    return false;
                })
                .collect(Collectors.toList());
    }

    public <T extends MapObject> List<T> getObjects(Class<T> clazz)
    {
        return this.mapObjects.stream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .map(object -> (T) object).collect(Collectors.toList());
    }

    public List<MapObject> getObjects()
    {
        return this.mapObjects;
    }

    public List<DynamicObject<?>> getDynamicObjects() {
        return dynamicObjects;
    }

    public <A extends DynamicObject<?>> List<DynamicObject> getDynamicObjects(Class<A> clazz) {
        return getDynamicObjects().stream().filter(e -> clazz.isAssignableFrom(e.getClass())).collect(Collectors.toList());
    }

    public <T, A extends MapObject> boolean isInMapObject(AgentContainer<T> agentContainer, Class<A> clazz) {
        return this.mapObjects.stream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .anyMatch(e -> PointContainer.intersect(agentContainer.getShape(), e.getContainer()));
    }

    public Set<EffectArea> getEffectAreas(AgentContainer<?> agent)
    {
        return this.mapObjects.stream()
                .filter(e -> !e.getEffects().isEmpty())
                .filter(e -> PointContainer.intersect(agent.getShape(), e.getContainer()))
                .flatMap((Function<MapObject, Stream<EffectArea>>) object -> object.getEffects().stream())

                .collect(Collectors.toUnmodifiableSet());
    }

    public boolean isMoveIntersecting(AgentContainer<?> agentContainer, PointContainer.Polygon agentMove){
        for (MapObject e : getFilteredObjects(agentContainer, e -> e.getType().isSolid())) {
            if (PointContainer.intersect(e.getContainer(), agentMove)) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns a set of ObjectPercepts that are found in a line (ObjectPercepts after opaque objects are excluded)
     * @param line
     * @return
     */
    public Set<ObjectPercept> getObjectPerceptsInLine(List<MapObject> filteredObjects, AgentContainer agentContainer, FieldOfView fov, PointContainer.Line line) {
        // --- all points where line and objects intersect sorted by proximity to start of line
        Map<Vector2, ObjectPerceptType> objectPoints = new HashMap<>();

        // --- perceive map objects
        for (MapObject mo : filteredObjects) {
            for (Vector2 point : PointContainer.intersectionPoints(mo.getContainer(), line)) {
                Vector2 relative = point
                        .sub(agentContainer.getPosition()) // move relative to agent
                        .rotated(agentContainer.getDirection().getClockDirection()); //rotated back
                if(relative.length() > 0 && fov.isInView(relative.toVexing()))
                {
                    objectPoints.put(relative, mo.getType());
                }
            }
        }

        // --- perceive intruders
        for (IntruderContainer intruder : this.game.getIntruders()) {
            if(intruder == agentContainer || intruder.isCaptured()) continue;
            for (Vector2 point : PointContainer.intersectionPoints(intruder.getShape(), line)) {
                Vector2 relative = point
                        .sub(agentContainer.getPosition()) // move relative to agent
                        .rotated(agentContainer.getDirection().getClockDirection()); //rotated back
                if(relative.length() > 0 && fov.isInView(relative.toVexing()))
                {
                    objectPoints.put(relative, ObjectPerceptType.Intruder);
                }
            }
        }

        // --- perceive guards
        for (AgentContainer<Guard> guard : this.game.getGuards()) {
            if(guard == agentContainer) continue;
            for (Vector2 point : PointContainer.intersectionPoints(guard.getShape(), line)) {
                Vector2 relative = point
                        .sub(agentContainer.getPosition()) // move relative to agent
                        .rotated(agentContainer.getDirection().getClockDirection()); //rotated bac
                if(relative.length() > 0 && fov.isInView(relative.toVexing()))
                {
                    objectPoints.put(relative, ObjectPerceptType.Guard);
                }
            }
        }

        // --- sort by distance
        List<Map.Entry<Vector2, ObjectPerceptType>> entries = objectPoints.entrySet()
                .stream()
                .sorted(Comparator.comparingDouble(e -> e.getKey().length()))
                .filter(e -> e.getKey().distance(agentContainer.getPosition()) > 0)
                .collect(Collectors.toList());

        Set<ObjectPercept> retSet = new HashSet<>();

        for (Map.Entry<Vector2, ObjectPerceptType> entry : entries) {
            retSet.add(new ObjectPercept(entry.getValue(), entry.getKey().toVexing()));
            if (entry.getValue().isOpaque())
            {
                break;
            }
        }

        if(retSet.isEmpty())
        {
            retSet.add(new ObjectPercept(ObjectPerceptType.EmptySpace, line.getEnd()
                    .sub(agentContainer.getPosition()) // move relative to agent
                    .rotated(agentContainer.getDirection().getClockDirection()) //rotated back
                    .toVexing()));
        }

        return retSet;
    }


    /**
     * Returns the set of all objects visible by an agent relative to the agent.
     * It casts rays starting from the clockwise-most to the anticlock-wise most.
     * Assuming Direction of an agent points to the middle of the this field of view
     * (so Direction divides the field of view exactly into two equal sections)
     * @param agentContainer
     * @param <T>
     * @return
     * @see Interop.Percept.Vision.FieldOfView
     */
    public <T> Set<ObjectPercept> getObjectPerceptsForAgent(AgentContainer<T> agentContainer, FieldOfView fov, ViewRange viewRange) {
        Set<ObjectPercept> objectsInSight = new HashSet<>();
        //System.out.println("angle-a: " + agentContainer.getDirection().getClockDirection());
        List<MapObject> filteredObjects = getFilteredObjects(agentContainer, null);
        for (Vector2[] ray : getAgentVisionCone(agentContainer, fov, viewRange)) {
            Set<ObjectPercept> objectPercepts = getObjectPerceptsInLine(filteredObjects, agentContainer, fov, new PointContainer.Line(ray[0], ray[1], false));
            objectsInSight.addAll(
                    objectPercepts.stream()
                            .filter(e -> fov.isInView(e.getPoint()))
                            .collect(Collectors.toList())
            );
        }

        return objectsInSight;
    }

    public <T> Set<Vector2[]> getAgentVisionCone(AgentContainer<T> agentContainer, FieldOfView fov, ViewRange viewRange) {
        double range = fov.getRange().getValue();
        final double viewAngle = fov.getViewAngle().getRadians();

        Vector2 direction = agentContainer.getDirection().normalise();
        Vector2 startOfRay = agentContainer.getPosition();

        //--- modify line to conform to modified view range (i.e. sentry tower)
        if(viewRange != null)
        {
            startOfRay = startOfRay.add(direction.mul(viewRange.getMin()));
            range = viewRange.getMax();
        }

        Vector2 ray = direction.mul(range).rotated(-viewAngle/2);

        int viewRays = _OPTIMISE_RAYS ? this.calculateRequiredRays(fov) : gameSettings.get___viewRays();
        double stepAngle = viewAngle / viewRays;
        Set<Vector2[]> objectsInSight = new HashSet<>();
        for (int rayNum = 0; rayNum < viewRays; rayNum++) {
            Vector2 endOfRay = startOfRay.add(ray.rotated((stepAngle * rayNum)));
            objectsInSight.add(new Vector2[]{ startOfRay, endOfRay });
        }
        return objectsInSight;
    }

}
