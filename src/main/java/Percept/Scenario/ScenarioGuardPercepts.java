package Percept.Scenario;

import Geometry.Angle;
import Geometry.Distance;

/**
 * Represents scenario percepts with percepts relevant to the guards.
 */
public class ScenarioGuardPercepts {

    private ScenarioPercepts scenarioPercepts;
    private Distance maxMoveDistanceGuard;

    public ScenarioGuardPercepts(ScenarioPercepts scenarioPercepts, Distance maxMoveDistanceGuard) {
        this.scenarioPercepts = scenarioPercepts;
        this.maxMoveDistanceGuard = maxMoveDistanceGuard;
    }

    public ScenarioPercepts getScenarioPercepts() {
        return scenarioPercepts;
    }

    /**
     * @return The max distance can be modified by slow down modifiers.
     * @see SlowDownModifiers
     */
    public Distance getMaxMoveDistanceGuard() {
        return maxMoveDistanceGuard;
    }

}
