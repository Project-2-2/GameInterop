package Group9.map;

import Group9.agent.AgentContainer;
import Group9.map.area.EffectArea;
import Group9.map.dynamic.DynamicObject;
import Group9.map.objects.MapObject;
import Group9.math.Line;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Group9.tree.QuadTree;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameMap {

    private static List<ObjectPerceptType> _SOLID_PERCEPTS = Arrays.stream(ObjectPerceptType.values())
                .filter(ObjectPerceptType::isSolid).collect(Collectors.toList());

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

    private QuadTree<MapObject> quadTree;
    private List<MapObject> mapObjects;
    private List<EffectArea> mapEffects;


    private List<DynamicObject> dynamicObjects = new ArrayList<>();

    private int width, height;

    public GameMap(ScenarioPercepts scenarioPercepts, List<MapObject> mapObjects, List<EffectArea> effects,
                   int width, int height,
                   Distance guardMaxMoveDistance,
                   int turnsInTargetAreaToWin, Distance intruderMaxMoveDistance, Distance intruderMaxSprintDistance,
                   int sprintCooldown, int numGuards, int numIntruders, Distance intruderViewRangeNormal,
                   Distance intruderViewRangeShaded, Distance guardViewRangeNormal, Distance guardViewRangeShaded,
                   Distance[] sentryViewRange, Distance yellSoundRadius, Distance moveMaxSoundRadius,
                   Distance windowSoundRadius, Distance doorSoundRadius, Angle viewAngle, int viewRays)
    {
        this.scenarioPercepts = scenarioPercepts;

        this.guardMaxMoveDistance = guardMaxMoveDistance;
        this.turnsInTargetAreaToWin = turnsInTargetAreaToWin;
        this.intruderMaxMoveDistance = intruderMaxMoveDistance;
        this.intruderMaxSprintDistance = intruderMaxSprintDistance;

        this.sprintCooldown = sprintCooldown;

        this.mapObjects = mapObjects;
        this.mapEffects = effects;

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
            quadTree.add(a);
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

    public List<EffectArea> getMapEffects() {
        return mapEffects;
    }

    public List<DynamicObject> getDynamicObjects() {
        return dynamicObjects;
    }

    public <A, T extends MapObject> boolean isInArea(AgentContainer<A> agent, Class<T> clazz)
    {
        //TODO check whether this works or not...
        return this.mapObjects.stream()
                .filter(e -> clazz.isAssignableFrom(e.getClass()))
                .anyMatch(e -> PointContainer.intersect(agent.getShape(), e.getContainer()));
    }

    public boolean isRayIntersecting(Line line, List<ObjectPerceptType> objectPerceptTypes)
    {
        List<ObjectPerceptType> objects = objectPerceptTypes.stream().filter(MapObject::is).collect(Collectors.toList());
        List<ObjectPerceptType> effects = objectPerceptTypes.stream().filter(EffectArea::is).collect(Collectors.toList());
        return this.mapObjects.stream().filter(objects::contains).anyMatch(e -> PointContainer.intersect(e.getContainer(), line)) ||
                this.mapEffects.stream().filter(effects::contains).anyMatch(e -> PointContainer.intersect(e.getContainer(), line));
    }

    public boolean isRayIntersectingSolidObject(Line line)
    {
        return isRayIntersecting(line, _SOLID_PERCEPTS);
    }

    // returns a sorted list of map objects (from closer to start of line to furthest away). Objects after a solid object are not included
    public List<MapObject> objectsSeen(Line line) {
        // get stream of objects that intersect line
        Stream<MapObject> intersectingMapObjects = this.mapObjects.stream().filter(mo -> PointContainer.intersect(mo.getContainer(), line));

        // compares objects by how close they are to start of line
        Comparator<MapObject> closerComparator = new Comparator<MapObject>() {
            @Override
            public int compare(MapObject o1, MapObject o2) {
                // NOTE: checking with center probably not a good idea
                //       TODO: change to interesction point of object and line instead of center
                boolean compare = line.getStart().distance(o1.getContainer().getCenter()) > line.getStart().distance(o2.getContainer().getCenter());
                if (compare) {
                    // o1 is further away from start of line
                    return 1;
                } else {
                    // o2 is further away from start of line
                    return -1;
                }
                // TODO: might need to swap 1 and -1..
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

        private List<MapObject> objects = new ArrayList<>();
        private List<EffectArea> effects = new ArrayList<>();

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

        public Builder object(MapObject object)
        {
            this.objects.add(object);
            return this;
        }

        public Builder effect(EffectArea effect)
        {
            this.effects.add(effect);
            return this;
        }

        public GameMap build()
        {
            ScenarioPercepts scenarioPercepts = new ScenarioPercepts(gameMode, this.captureDistance, this.maxRotationAngle,
                    new SlowDownModifiers(this.windowSlowdownModifier, this.doorSlowdownModifier, this.sentrySlowdownModifier),
                    this.pheromoneRadius, this.pheromoneCooldown);

            return new GameMap(scenarioPercepts, this.objects, this.effects, this.width, this.height,
                        this.guardMaxMoveDistance, this.winRounds, this.intruderMaxMoveDistance, this.intruderMaxSprintDistance,
                        this.sprintCooldown, this.numGuards, this.numIntruders, this.intruderViewRangeNormal, this.intruderViewRangeShaded,
                        this.guardViewRangeNormal, this.guardViewRangeShaded, this.sentryViewRange, this.yellSoundRadius,
                        this.moveMaxSoundRadius, this.windowSoundRadius, this.doorSoundRadius, this.viewAngle, this.viewRays
                    );
        }


    }


}
