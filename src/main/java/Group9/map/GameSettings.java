package Group9.map;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.ScenarioPercepts;

public class GameSettings {

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
    private ViewRange sentryViewRange;

    private Distance yellSoundRadius;
    private Distance moveMaxSoundRadius;
    private Distance windowSoundRadius;
    private Distance doorSoundRadius;

    private Angle viewAngle;
    private int ___viewRays; //Note: Do not use this variable use GameMap#calculateRequiredRays instead.

    private int pheromoneExpireRounds;

    private int width, height;

    public GameSettings(ScenarioPercepts scenarioPercepts,
                int width, int height,
                Distance guardMaxMoveDistance,
                int turnsInTargetAreaToWin, Distance intruderMaxMoveDistance, Distance intruderMaxSprintDistance,
                int sprintCooldown, int numGuards, int numIntruders, Distance intruderViewRangeNormal,
                Distance intruderViewRangeShaded, Distance guardViewRangeNormal, Distance guardViewRangeShaded,
                ViewRange sentryViewRange, Distance yellSoundRadius, Distance moveMaxSoundRadius,
                Distance windowSoundRadius, Distance doorSoundRadius, Angle viewAngle, int ___viewRays, int pheromoneExpireRounds
    ) {
        this.scenarioPercepts = scenarioPercepts;

        this.guardMaxMoveDistance = guardMaxMoveDistance;
        this.turnsInTargetAreaToWin = turnsInTargetAreaToWin;
        this.intruderMaxMoveDistance = intruderMaxMoveDistance;
        this.intruderMaxSprintDistance = intruderMaxSprintDistance;

        this.sprintCooldown = sprintCooldown;

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
        this.___viewRays = ___viewRays;

        this.pheromoneExpireRounds = pheromoneExpireRounds;
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

    public ViewRange getSentryViewRange() {
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

    public int get___viewRays() {
        return ___viewRays;
    }

    public int getPheromoneExpireRounds(){
        return  pheromoneExpireRounds;
    }

}
