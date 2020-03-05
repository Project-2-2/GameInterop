package Group9.map;

import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Scenario.*;

public class Map {

    private final ScenarioPercepts scenarioPercepts;
    private final ScenarioGuardPercepts guardPercepts;
    private final ScenarioIntruderPercepts intruderPercepts;

    public Map(ScenarioPercepts scenarioPercepts, ScenarioGuardPercepts guardPercepts, ScenarioIntruderPercepts intruderPercepts)
    {
        this.guardPercepts = guardPercepts;
        this.intruderPercepts = intruderPercepts;
        this.scenarioPercepts = scenarioPercepts;
    }


    public static class Builder
    {

        private GameMode gameMode;
        private Distance captureDistance;
        private Angle maxRotationAngle;

        private double windowSlowdownModifier;
        private double doorSlowdownModifier;
        private double sentrySlowdownModifier;

        private Distance pheromoneRadius;
        private int pheromoneCooldown;

        public Builder() {}

        public Builder height(int height)
        {
            return this;
        }

        public Builder width(int width)
        {
            return this;
        }

        public Builder numGuards(int amount)
        {
            return this;
        }

        public Builder numIntruders(int amount)
        {
            return this;
        }

        public Builder intruderMaxMoveDistance(double max)
        {
            return this;
        }

        public Builder intruderMaxSprintDistance(double max)
        {
            return this;
        }

        public Builder intruderViewRangeNormal(double range)
        {
            return this;
        }

        public Builder intruderViewRangeShaded(double range)
        {
            return this;
        }

        public Builder guardMaxMoveDistance(double max)
        {
            return this;
        }

        public Builder guardViewRangeNormal(double range)
        {
            return this;
        }

        public Builder guardViewRangeShaded(double range)
        {
            return this;
        }

        public Builder sentryViewRange(double min, double max)
        {
            return this;
        }

        private Builder yellSoundRadius(double radius)
        {
            return this;
        }

        private Builder moveMaxSoundRadius(double radius)
        {
            return this;
        }

        private Builder windowSoundRadius(double radius)
        {
            return this;
        }

        private Builder doorSoundRadius(double radius)
        {
            return this;
        }

        public Builder sprintCooldown(int cooldown)
        {
            return this;
        }


        public Builder gameMode(GameMode gameMode)
        {
            this.gameMode = gameMode;
            return this;
        }

        public Builder winConditionIntruderRounds(int rounds)
        {
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

        public Map build()
        {
            ScenarioPercepts scenarioPercepts = new ScenarioPercepts(gameMode, this.captureDistance, this.maxRotationAngle,
                    new SlowDownModifiers(this.windowSlowdownModifier, this.doorSlowdownModifier, this.sentrySlowdownModifier),
                    this.pheromoneRadius, this.pheromoneCooldown);

            return new Map(scenarioPercepts, null, null);
        }

    }


}
