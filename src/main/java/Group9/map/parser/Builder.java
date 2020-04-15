package Group9.map.parser;

import Group9.map.GameMap;
import Group9.map.GameSettings;
import Group9.map.ViewRange;
import Group9.map.objects.*;
import Group9.tree.PointContainer;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.GameMode;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;

import java.util.ArrayList;
import java.util.List;

public class Builder
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
    private ViewRange sentryViewRange;
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
        this.sentryViewRange = new ViewRange(min, max);
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

    public Builder wall(PointContainer.Polygon quadrilateral){
        this.object(new Wall(quadrilateral));
        return this;
    }

    public Builder targetArea(PointContainer.Polygon quadrilateral){
        this.object(new TargetArea(quadrilateral));
        return this;
    }

    public Builder spawnAreaIntruders(PointContainer.Polygon quadrilateral){
        this.object(new Spawn.Intruder(quadrilateral));
        return this;
    }

    public Builder spawnAreaGuards(PointContainer.Polygon quadrilateral){
        this.object(new Spawn.Guard(quadrilateral));
        return this;
    }

    public Builder teleport(PointContainer.Polygon teleporterA, PointContainer.Polygon teleporterB){
        TeleportArea teleportAreaA = new TeleportArea(teleporterA, null);
        TeleportArea teleportAreaB = new TeleportArea(teleporterB, teleportAreaA);
        teleportAreaA.setConnected(teleportAreaB);
        this.object(teleportAreaA);
        this.object(teleportAreaB);
        return this;
    }

    public Builder shaded(PointContainer.Polygon quadrilateral){
        this.object(new ShadedArea(quadrilateral,guardViewRangeShaded.getValue()/guardViewRangeNormal.getValue(),
                intruderViewRangeShaded.getValue()/intruderViewRangeNormal.getValue()));
        return this;
    }

    public Builder door(PointContainer.Polygon quadrilateral){
        this.object(new Door(quadrilateral,
                guardViewRangeShaded.getValue()/guardViewRangeNormal.getValue(), intruderViewRangeShaded.getValue()/intruderViewRangeNormal.getValue(),
                this.doorSoundRadius.getValue(),
                this.doorSlowdownModifier, this.doorSlowdownModifier
        ));
        return this;
    }

    public Builder window(PointContainer.Polygon quadrilateral){
        this.object(new Window(quadrilateral,
                guardViewRangeShaded.getValue()/guardViewRangeNormal.getValue(), intruderViewRangeShaded.getValue()/intruderViewRangeNormal.getValue(),
                this.windowSoundRadius.getValue(),
                this.windowSlowdownModifier, this.windowSlowdownModifier
        ));
        return this;
    }
    public Builder sentry(PointContainer.Polygon outsideArea, PointContainer.Polygon insideArea){
        this.object(new SentryTower(insideArea, sentrySlowdownModifier, this.sentryViewRange));
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

        return new GameMap(new GameSettings(scenarioPercepts, this.width, this.height,
                this.guardMaxMoveDistance, this.winRounds, this.intruderMaxMoveDistance, this.intruderMaxSprintDistance,
                this.sprintCooldown, this.numGuards, this.numIntruders, this.intruderViewRangeNormal, this.intruderViewRangeShaded,
                this.guardViewRangeNormal, this.guardViewRangeShaded, this.sentryViewRange, this.yellSoundRadius,
                this.moveMaxSoundRadius, this.windowSoundRadius, this.doorSoundRadius, this.viewAngle, this.viewRays, this.pheromoneExpireRounds)
        , this.objects);
    }


}
