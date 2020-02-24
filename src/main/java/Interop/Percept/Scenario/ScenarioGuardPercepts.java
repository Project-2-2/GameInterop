package Interop.Percept.Scenario;

import Interop.Geometry.Distance;
import Interop.Utils.Require;

/**
 * Represents scenario percepts with percepts relevant to the guards.
 */
public final class ScenarioGuardPercepts {

    private ScenarioPercepts scenarioPercepts;
    private Distance maxMoveDistanceGuard;

    public ScenarioGuardPercepts(ScenarioPercepts scenarioPercepts, Distance maxMoveDistanceGuard) {
        Require.notNull(scenarioPercepts);
        Require.notNull(maxMoveDistanceGuard);
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
