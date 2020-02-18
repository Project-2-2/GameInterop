package Percept.Scenario;

import Geometry.Angle;
import Geometry.Distance;
import Utils.Require;

/**
 * Represents scenario percepts extended with intruder specific scenario percepts.
 */
public class ScenarioIntruderPercepts {

    private ScenarioPercepts scenarioPercepts;
    private Distance maxMoveDistanceIntruder;
    private Distance maxSprintDistanceIntruder;
    private int sprintCooldown;

    public ScenarioIntruderPercepts(
        ScenarioPercepts scenarioPercepts,
        Distance maxMoveDistanceIntruder,
        Distance maxSprintDistanceIntruder,
        int sprintCooldown
    ) {
        Require.notNegative(sprintCooldown, "Sprint cool down must not be negative!");
        this.scenarioPercepts = scenarioPercepts;
        this.maxMoveDistanceIntruder = maxMoveDistanceIntruder;
        this.maxSprintDistanceIntruder = maxSprintDistanceIntruder;
        this.sprintCooldown = sprintCooldown;
    }

    public ScenarioPercepts getScenarioPercepts() {
        return scenarioPercepts;
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
