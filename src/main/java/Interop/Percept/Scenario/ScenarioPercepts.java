package Interop.Percept.Scenario;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Utils.Require;

/**
 * Represents percepts relevant to an agent that are specified in a scenario file.
 */
public final class ScenarioPercepts {

    private GameMode gameMode;
    private Distance captureDistance;
    private Angle maxRotationAngle;
    private SlowDownModifiers slowDownModifiers;
    private Distance radiusPheromone;
    private int pheromoneCooldown;

    public ScenarioPercepts(
        GameMode gameMode,
        Distance captureDistance,
        Angle maxRotationAngle,
        SlowDownModifiers slowDownModifiers,
        Distance radiusPheromone,
        int pheromoneCooldown
    ) {
        Require.notNull(gameMode);
        Require.notNull(captureDistance);
        Require.notNull(maxRotationAngle);
        Require.notNull(slowDownModifiers);
        Require.notNull(radiusPheromone);
        Require.positive(maxRotationAngle.getDegrees(), "Max rotation angle must be positive!");
        Require.notNegative(pheromoneCooldown, "Pheromone cool down must not be negative!");
        this.gameMode = gameMode;
        this.captureDistance = captureDistance;
        this.maxRotationAngle = maxRotationAngle;
        this.slowDownModifiers = slowDownModifiers;
        this.radiusPheromone = radiusPheromone;
        this.pheromoneCooldown = pheromoneCooldown;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Distance getCaptureDistance() {
        return captureDistance;
    }

    public Angle getMaxRotationAngle() {
        return maxRotationAngle;
    }

    public int getPheromoneCooldown() {
        return pheromoneCooldown;
    }

    public Distance getRadiusPheromone() {
        return radiusPheromone;
    }

    public SlowDownModifiers getSlowDownModifiers() {
        return slowDownModifiers;
    }

}
