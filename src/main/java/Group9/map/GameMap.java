package Group9.map;

import Group9.agent.container.AgentContainer;
import Group9.map.area.EffectArea;
import Group9.map.area.ModifyViewEffect;
import Group9.map.dynamic.DynamicObject;
import Group9.map.objects.*;
import Group9.map.objects.Window;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Group9.tree.QuadTree;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Geometry.Vector;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.VisionPrecepts;
import Interop.Utils.Utils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameMap {

    private final ScenarioPercepts scenarioPercepts;

    private Distance guardMaxMoveDistance;

    private int turnsInTargetAreaToWin;
    private Distance intruderMaxMoveDistance;
    private Distance intruderMaxSprintDistance;
    private int sprintCooldown;
    private int numGuards;
    private int numIntruders;

    private Distance intruderViewRangeNormal;
    private Distance intruderViewRangeShaded;
    private Distance guardViewRangeNormal;
    private Distance guardViewRangeShaded;
    private Distance[] sentryViewRange;

    private Distance yellSoundRadius;
    private Distance moveMaxSoundRadius;
    private Distance windowSoundRadius;
    private Distance doorSoundRadius;

    private Angle viewAngle;
    private int viewRays;

    private int pheromoneExpireRounds;

    private QuadTree<MapObject> quadTree;
    private List<MapObject> mapObjects;

    private List<DynamicObject> dynamicObjects = new ArrayList<>();

    private int width, height;

    public GameMap(ScenarioPercepts scenarioPercepts, List<MapObject> mapObjects,
                   int width, int height,
                   Distance guardMaxMoveDistance,
                   int turnsInTargetAreaToWin, Distance intruderMaxMoveDistance, Distance intruderMaxSprintDistance,
                   int sprintCooldown, int numGuards, int numIntruders, Distance intruderViewRangeNormal,
                   Distance intruderViewRangeShaded, Distance guardViewRangeNormal, Distance guardViewRangeShaded,
                   Distance[] sentryViewRange, Distance yellSoundRadius, Distance moveMaxSoundRadius,
                   Distance windowSoundRadius, Distance doorSoundRadius, Angle viewAngle, int viewRays, int pheromoneExpireRounds)
    {
        this.scenarioPercepts = scenarioPercepts;

        this.guardMaxMoveDistance = guardMaxMoveDistance;
        this.turnsInTargetAreaToWin = turnsInTargetAreaToWin;
        this.intruderMaxMoveDistance = intruderMaxMoveDistance;
        this.intruderMaxSprintDistance = intruderMaxSprintDistance;

        this.sprintCooldown = sprintCooldown;

        this.mapObjects = mapObjects;

        this.width = width;
        this.height = height;

        this.numGuards = numGuards;
        this.numIntruders = numIntruders;

        this.intruderViewRangeNormal = intruderViewRangeNormal;
        this.intruderViewRangeShaded = intruderViewRangeShaded;
        this.guardViewRangeNormal = guardViewRangeNormal;
        this.guardViewRangeShaded = guardViewRangeShaded;
        this.sentryViewRange = sentryViewRange;

        this.yellSoundRadius = yellSoundRadius;
        this.moveMaxSoundRadius = moveMaxSoundRadius;
        this.windowSoundRadius = windowSoundRadius;
        this.doorSoundRadius = doorSoundRadius;

        this.viewAngle = viewAngle;
        this.viewRays = viewRays;

        this.pheromoneExpireRounds = pheromoneExpireRounds;

        this.quadTree = new QuadTree<>(width, height, 10000, MapObject::getContainer);
        AtomicInteger index = new AtomicInteger();
        mapObjects.forEach(a -> {
            AtomicInteger c = new AtomicInteger();
            mapObjects.forEach(b -> {
                if(a != b)
                {
                    if(PointContainer.intersect(a.getContainer(), b.getContainer()))
                    {
                        c.getAndIncrement();
                    }
                }
            });
            System.out.println(index.getAndIncrement() + "." + c);
        });
        index.set(0);
        mapObjects.forEach(a -> {
            System.out.println(index.getAndIncrement());
            //quadTree.add(a);
        });
        System.out.print("");
    }

    public ScenarioPercepts getScenarioPercepts() {
        return scenarioPercepts;
    }

    public Distance getGuardMaxMoveDistance() {
        return guardMaxMoveDistance;
    }

    public Distance getIntruderMaxMoveDistance() {
        return intruderMaxMoveDistance;
    }

    public Distance getIntruderMaxSprintDistance() {
        return intruderMaxSprintDistance;
    }

    public int getTurnsInTargetAreaToWin() {
        return turnsInTargetAreaToWin;
    }

    public int getSprintCooldown() {
        return sprintCooldown;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumGuards() {
        return numGuards;
    }

    public int getNumIntruders() {
        return numIntruders;
    }

    public Distance getIntruderViewRangeNormal() {
        return intruderViewRangeNormal;
    }

    public Distance getIntruderViewRangeShaded() {
        return intruderViewRangeShaded;
    }

    public Distance getGuardViewRangeNormal() {
        return guardViewRangeNormal;
    }

    public Distance getGuardViewRangeShaded() {
        return guardViewRangeShaded;
    }

    public Distance[] getSentryViewRange() {
        return sentryViewRange;
    }

    public Distance getYellSoundRadius() {
        return yellSoundRadius;
    }

    public Distance getMoveMaxSoundRadius() {
        return moveMaxSoundRadius;
    }

    public Distance getWindowSoundRadius() {
        return windowSoundRadius;
    }

    public Distance getDoorSoundRadius() {
        return doorSoundRadius;
    }

    public Angle getViewAngle() {
        return viewAngle;
    }

    public int getViewRays() {
        return viewRays;
    }

    public int getPheromoneExpireRounds(){
        return  pheromoneExpireRounds;
    }

    public <T extends MapObject> List<T> getObjects(Class<T> clazz)
    {
        return this.mapObjects.stream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .map(object -> (T) object).collect(Collectors.toList());
    }

    public QuadTree<MapObject> getQuadTree() {
        return quadTree;
    }

    public List<MapObject> getMapObjects() {
        return mapObjects;
    }

    public List<DynamicObject> getDynamicObjects() {
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

    public boolean isMoveIntersecting(PointContainer.Quadrilateral agentMove, Predicate<ObjectPerceptType> filter){
        return this.mapObjects.stream()
                .filter(e -> filter.test(e.getType()))
                .anyMatch(e -> PointContainer.intersect(e.getContainer(), agentMove));
    }

    public boolean isRayIntersecting(PointContainer.Line line, Predicate<ObjectPerceptType> filter)
    {
        return this.mapObjects.stream()
                .filter(e -> filter.test(e.getType()))
                .anyMatch(e -> PointContainer.intersect(e.getContainer(), line));
    }

    public boolean isRayIntersecting(PointContainer.Line line, List<ObjectPerceptType> types)
    {
        return isRayIntersecting(line, types::contains);
    }

    public boolean isRayIntersectingSolidObject(PointContainer.Line line)
    {
        return isRayIntersecting(line, ObjectPerceptType::isSolid);
    }

    // Not in use
    // TODO-low-priority: delete me?
    // returns a sorted list of map objects (from closer to start of line to furthest away). Objects after a solid object are not included
    public List<MapObject> objectsSeen(PointContainer.Line line) {
        // get stream of objects that intersect line
        Stream<MapObject> intersectingMapObjects = this.mapObjects.stream().filter(mo -> PointContainer.intersect(mo.getContainer(), line));

        // compares objects by how close they are to start of line
        Comparator<MapObject> closerComparator = new Comparator<MapObject>() {
            @Override
            public int compare(MapObject o1, MapObject o2) {
                //TODO: change to interesction point of object and line instead of center
                // Note (Jan): This should sort ascending now which is what we want, right? otherwise just add a - in front of the method to
                // invert the results
                return Double.compare(
                        line.getStart().distance(o1.getContainer().getCenter()),
                        line.getStart().distance(o2.getContainer().getCenter())
                );
            }
        };

        // chomp off objects after opaque object is found
        List<MapObject> retList = new ArrayList<MapObject>();
        for (MapObject o : intersectingMapObjects.sorted(closerComparator).collect(Collectors.toList())) {
            retList.add(o);
            if (o.getType().isOpaque() || o.getType().isAgent()) {
                break;
            }
        }

        return retList;
    }

    /**
     * returns a set of ObjectPercepts that are found in a line (ObjectPercepts after opaque objects are excluded)
     * @param line
     * @return
     */
    public Set<ObjectPercept> getObjectPerceptsInLine(PointContainer.Line line) {
        // get list of objects that intersect line
        List<MapObject> intersectingMapObjects = this.mapObjects.stream()
                .filter(mo -> PointContainer.intersect(mo.getContainer(), line))
                .collect(Collectors.toList());

        // all points where line and objects intersect sorted by proximity to start of line
        Map<Vector2, MapObject> objectPoints = new TreeMap<Vector2, MapObject>(new Comparator<Vector2>() {
            @Override
            public int compare(Vector2 v1, Vector2 v2) {
                return Double.compare(
                        line.getStart().distance(v1),
                        line.getStart().distance(v2));
            }
        });

        for (MapObject mo : intersectingMapObjects) {
            for (Vector2 point : PointContainer.intersectionPoints(mo.getContainer(), line)) {
                objectPoints.put(point, mo);
            }
        }

        // we build Set<ObjectPercepts> removing ObjectPercepts that come after opaque ObjectPercepts
        Set<ObjectPercept> retSet = new HashSet<>();

        for (Map.Entry<Vector2, MapObject> entry : objectPoints.entrySet()) {
            retSet.add(new ObjectPercept(entry.getValue().getType(), entry.getKey().toVexing()));
            if (entry.getValue().getType().isOpaque())
            {
                break;
            }
        }

        return retSet;
    }


    /**
     * Returns the set of all objects visible by an agent
     * It casts rays starting from the clockwise-most to the anticlock-wise most.
     * Assuming Direction of an agent points to the middle of the this field of view
     * (so Direction divides the field of view exactly into two equal sections)
     * @param agentContainer
     * @param <T>
     * @return
     * @see Interop.Percept.Vision.FieldOfView
     */
    public <T> Set<ObjectPercept> getObjectPerceptsForAgent(AgentContainer<T> agentContainer, FieldOfView fov) {
        // sane default (TODO: Is naive java-fu below working?)
        final double range = fov.getRange().getValue();
        final double viewAngle = fov.getViewAngle().getRadians();

        Vector2 ray = agentContainer.getDirection().normalise().mul(range).rotated(-viewAngle/2);
        Vector2 startOfRay = agentContainer.getPosition();

        double stepAngle = viewAngle / viewRays ;
        Set<ObjectPercept> objectsInSight = new HashSet<>();
        for (int rayNum = 0; rayNum < viewRays; rayNum++) {
            Vector2 endOfRay = startOfRay.add(ray.rotated((stepAngle * rayNum)));
            objectsInSight.addAll(
                    getObjectPerceptsInLine(new PointContainer.Line(startOfRay, endOfRay))
                            .stream()
                            .map(e -> {
                                Vector point =  Vector2.from(e.getPoint())
                                        .sub(agentContainer.getPosition()) // move relative to agent
                                        .rotated(agentContainer.getDirection().getClockDirection()) // rotate back
                                        .toVexing();
                                return new ObjectPercept(e.getType(), point);
                            })
                            .filter(e -> fov.isInView(e.getPoint()))
                            .collect(Collectors.toList())
            );
        }

        return objectsInSight;
    }


    public static class Builder
    {
        private int height;
        private int width;

        private GameMode gameMode;
        private int winRounds;
        private int numGuards;
        private int numIntruders;

        private Distance intruderMaxMoveDistance;
        private Distance intruderMaxSprintDistance;
        private Distance guardMaxMoveDistance;

        private Distance intruderViewRangeNormal;
        private Distance intruderViewRangeShaded;
        private Distance guardViewRangeNormal;
        private Distance guardViewRangeShaded;
        private Distance[] sentryViewRange = new Distance[2];
        private Angle viewAngle;
        private int viewRays;

        private Distance yellSoundRadius;
        private Distance moveMaxSoundRadius;
        private Distance windowSoundRadius;
        private Distance doorSoundRadius;

        private Distance captureDistance;
        private Angle maxRotationAngle;

        private double windowSlowdownModifier;
        private double doorSlowdownModifier;
        private double sentrySlowdownModifier;

        private Distance pheromoneRadius;
        private int pheromoneCooldown;
        private int sprintCooldown;
        private int pheromoneExpireRounds;

        private List<MapObject> objects = new ArrayList<>();

        public Builder() {}

        public Builder height(int height)
        {
            this.height = height;
            return this;
        }

        public Builder width(int width)
        {
            this.width = width;
            return this;
        }

        public Builder numGuards(int amount)
        {
            this.numGuards = amount;
            return this;
        }

        public Builder numIntruders(int amount)
        {
            this.numIntruders = amount;
            return this;
        }

        public Builder intruderMaxMoveDistance(double max)
        {
            this.intruderMaxMoveDistance = new Distance(max);
            return this;
        }

        public Builder intruderMaxSprintDistance(double max)
        {
            this.intruderMaxSprintDistance = new Distance(max);
            return this;
        }

        public Builder intruderViewRangeNormal(double range)
        {
            this.intruderViewRangeNormal = new Distance(range);
            return this;
        }

        public Builder intruderViewRangeShaded(double range)
        {
            this.intruderViewRangeShaded = new Distance(range);
            return this;
        }

        public Builder guardMaxMoveDistance(double max)
        {
            this.guardMaxMoveDistance = new Distance(max);
            return this;
        }

        public Builder guardViewRangeNormal(double range)
        {
            this.guardViewRangeNormal = new Distance(range);
            return this;
        }

        public Builder guardViewRangeShaded(double range)
        {
            this.guardViewRangeShaded = new Distance(range);
            return this;
        }

        public Builder sentryViewRange(double min, double max)
        {
            this.sentryViewRange[0] = new Distance(min);
            this.sentryViewRange[1] = new Distance(max);
            return this;
        }

        public Builder viewAngle(double angle)
        {
            this.viewAngle = Angle.fromDegrees(angle);
            return this;
        }

        public Builder viewRays(int rays)
        {
            this.viewRays = rays;
            return this;
        }
        public Builder yellSoundRadius(double radius)
        {
            this.yellSoundRadius = new Distance(radius);
            return this;
        }

        public Builder moveMaxSoundRadius(double radius)
        {
            this.moveMaxSoundRadius = new Distance(radius);
            return this;
        }

        public Builder windowSoundRadius(double radius)
        {
            this.windowSoundRadius = new Distance(radius);
            return this;
        }

        public Builder doorSoundRadius(double radius)
        {
            this.doorSoundRadius = new Distance(radius);
            return this;
        }

        public Builder sprintCooldown(int cooldown)
        {
            this.sprintCooldown = cooldown;
            return this;
        }


        public Builder gameMode(GameMode gameMode)
        {
            this.gameMode = gameMode;
            return this;
        }

        public Builder winConditionIntruderRounds(int rounds)
        {
            this.winRounds = rounds;
            return this;
        }

        public Builder captureDistance(double captureDistance)
        {
            this.captureDistance = new Distance(captureDistance);
            return this;
        }

        public Builder maxRotationAngle(double maxRotationAngle)
        {
            this.maxRotationAngle = Angle.fromDegrees(maxRotationAngle);
            return this;
        }

        public Builder windowSlowdownModifier(double modifier)
        {
            this.windowSlowdownModifier = modifier;
            return this;
        }

        public Builder doorSlowdownModifier(double modifier)
        {
            this.doorSlowdownModifier = modifier;
            return this;
        }

        public Builder sentrySlowdownModifier(double modifier)
        {
            this.sentrySlowdownModifier = modifier;
            return this;
        }

        public Builder pheromoneRadius(double radius)
        {
            this.pheromoneRadius = new Distance(radius);
            return this;
        }

        public Builder pheromoneCooldown(int cooldown)
        {
            this.pheromoneCooldown = cooldown;
            return this;
        }

        public Builder pheromoneExpireRounds(int expiration){
            this.pheromoneExpireRounds = expiration;
            return this;
        }

        public Builder wall(PointContainer.Quadrilateral quadrilateral){
            this.object(new Wall(quadrilateral));
            return this;
        }

        public Builder targetArea(PointContainer.Quadrilateral quadrilateral){
            this.object(new TargetArea(quadrilateral));
            return this;
        }

        public Builder spawnAreaIntruders(PointContainer.Quadrilateral quadrilateral){
            this.object(new Spawn.Intruder(quadrilateral));
            return this;
        }

        public Builder spawnAreaGuards(PointContainer.Quadrilateral quadrilateral){
            this.object(new Spawn.Guard(quadrilateral));
            return this;
        }

        public Builder teleport(PointContainer.Quadrilateral quadrilateral){
            this.object(new TeleportArea(quadrilateral));
            return this;
        }

        public Builder shaded(PointContainer.Quadrilateral quadrilateral){
            this.object(new ShadedArea(quadrilateral,guardViewRangeShaded.getValue()/guardViewRangeNormal.getValue(),
                    intruderViewRangeShaded.getValue()/intruderViewRangeNormal.getValue()));
            return this;
        }

        public Builder door(PointContainer.Quadrilateral quadrilateral){
            this.object(new Door(quadrilateral));
            return this;
        }

        public Builder window(PointContainer.Quadrilateral quadrilateral){
            this.object(new Window(quadrilateral));
            return this;
        }

        public Builder sentry(PointContainer.Quadrilateral quadrilateral){
            this.object(new SentryTower(quadrilateral, sentrySlowdownModifier));
            return this;
        }

        private Builder object(MapObject object)
        {
            this.objects.add(object);
            return this;
        }


        public GameMap build()
        {
            ScenarioPercepts scenarioPercepts = new ScenarioPercepts(gameMode, this.captureDistance, this.maxRotationAngle,
                    new SlowDownModifiers(this.windowSlowdownModifier, this.doorSlowdownModifier, this.sentrySlowdownModifier),
                    this.pheromoneRadius, this.pheromoneCooldown);

            return new GameMap(scenarioPercepts, this.objects, this.width, this.height,
                        this.guardMaxMoveDistance, this.winRounds, this.intruderMaxMoveDistance, this.intruderMaxSprintDistance,
                        this.sprintCooldown, this.numGuards, this.numIntruders, this.intruderViewRangeNormal, this.intruderViewRangeShaded,
                        this.guardViewRangeNormal, this.guardViewRangeShaded, this.sentryViewRange, this.yellSoundRadius,
                        this.moveMaxSoundRadius, this.windowSoundRadius, this.doorSoundRadius, this.viewAngle, this.viewRays, this.pheromoneExpireRounds
                    );
        }


    }


}
