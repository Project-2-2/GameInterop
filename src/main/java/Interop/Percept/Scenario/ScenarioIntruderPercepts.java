package Interop.Percept.Scenario;

import Interop.Geometry.Distance;
import Interop.Utils.Require;

/**
 * Represents scenario percepts extended with intruder specific scenario percepts.
 */
public final class ScenarioIntruderPercepts {

    private ScenarioPercepts scenarioPercepts;
    private int turnsInTargetAreaToWin;
    private Distance maxMoveDistanceIntruder;
    private Distance maxSprintDistanceIntruder;
    private int sprintCooldown;

    public ScenarioIntruderPercepts(
        ScenarioPercepts scenarioPercepts,
        int turnsInTargetAreaToWin,
        Distance maxMoveDistanceIntruder,
        Distance maxSprintDistanceIntruder,
        int sprintCooldown
    ) {
        Require.notNull(scenarioPercepts);
        Require.notNull(maxMoveDistanceIntruder);
        Require.notNull(maxSprintDistanceIntruder);
        Require.notNegative(turnsInTargetAreaToWin, "The number of turns can not be negative!");
        Require.notNegative(sprintCooldown, "Sprint cool down must not be negative!");
        this.scenarioPercepts = scenarioPercepts;
        this.turnsInTargetAreaToWin = turnsInTargetAreaToWin;
        this.maxMoveDistanceIntruder = maxMoveDistanceIntruder;
        this.maxSprintDistanceIntruder = maxSprintDistanceIntruder;
        this.sprintCooldown = sprintCooldown;
    }

    public ScenarioPercepts getScenarioPercepts() {
        return scenarioPercepts;
    }

    /**
     * @return Indicates how many turns the intruders needs to stay in target area in order to win.
     */
    public int getTurnsInTargetAreaToWin() {
        return turnsInTargetAreaToWin;
    }

    /**
     * @return The max distance can be modified by slow down modifiers.
     * @see SlowDownModifiers
     */
    public Distance getMaxMoveDistanceIntruder() {
        return maxMoveDistanceIntruder;
    }

    /**
     * @return The max distance can be modified by slow down modifiers.
     * @see SlowDownModifiers
     */
    public Distance getMaxSprintDistanceIntruder() {
        return maxSprintDistanceIntruder;
    }

    public int getSprintCooldown() {
        return sprintCooldown;
    }

}
