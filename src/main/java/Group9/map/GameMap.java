package Group9.map;

import Group9.map.area.EffectArea;
import Group9.map.objects.MapObject;
import Group9.tree.QuadTree;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameMap {

    private final ScenarioPercepts scenarioPercepts;

    private Distance guardMaxMoveDistance;

    private int turnsInTargetAreaToWin;
    private Distance intruderMaxMoveDistance;
    private Distance intruderMaxSprintDistance;

    private QuadTree<MapObject> quadTree;
    private List<MapObject> mapObjects;
    private List<EffectArea> mapEffects;

    private int width, height;

    public GameMap(ScenarioPercepts scenarioPercepts, List<MapObject> mapObjects, List<EffectArea> effects,
                   int width, int height,
                   Distance guardMaxMoveDistance,
                   int turnsInTargetAreaToWin, Distance intruderMaxMoveDistance, Distance intruderMaxSprintDistance)
    {
        this.scenarioPercepts = scenarioPercepts;

        this.guardMaxMoveDistance = guardMaxMoveDistance;
        this.turnsInTargetAreaToWin = turnsInTargetAreaToWin;
        this.intruderMaxMoveDistance = intruderMaxMoveDistance;
        this.intruderMaxSprintDistance = intruderMaxSprintDistance;

        this.mapObjects = mapObjects;
        this.mapEffects = effects;

        this.width = width;
        this.height = height;

        //this.quadTree = new QuadTree<>(width, height, 3, MapObject::getContainer);
        //mapObjects.forEach(o -> quadTree.add(o));
        //System.out.print("");
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public static class Builder
    {

        //TODO: GET DATA FROM BUILDER TO MAP
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
                        this.guardMaxMoveDistance,
                        this.winRounds, this.intruderMaxMoveDistance, this.intruderMaxSprintDistance
                    );
        }

    }


}
