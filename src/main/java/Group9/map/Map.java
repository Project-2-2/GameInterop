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

        public Map build()
        {
            ScenarioPercepts scenarioPercepts = new ScenarioPercepts(gameMode, this.captureDistance, this.maxRotationAngle,
                    new SlowDownModifiers(this.windowSlowdownModifier, this.doorSlowdownModifier, this.sentrySlowdownModifier),
                    this.pheromoneRadius, this.pheromoneCooldown);

            return new Map(scenarioPercepts, null, null);
        }

    }


}
