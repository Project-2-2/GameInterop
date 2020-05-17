package Group6.Agent;

import Interop.Action.Action;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Scenario.SlowDownModifiers;

/**
 * @author Tomasz Darmetko
 */
public class ActionsFactory {

    public static Rotate getRandomRotate(Percepts percepts) {
        return getPartMaxRotate(percepts, 1. - 2. * Math.random());
    }

    public static Rotate getPartMaxRotate(Percepts percepts, double modifier) {
        return ActionsFactory.getValidRotate(
            ActionsFactory.getMaxRotationDegrees(percepts) * modifier,
            percepts
        );
    }

    public static Rotate getValidRotate(double desiredDegrees, Percepts percepts) {

        Angle.fromDegrees(desiredDegrees);

        double sign = Math.signum(desiredDegrees);
        double validAngle = Math.min(Math.abs(desiredDegrees), getMaxRotationDegrees(percepts));

        return new Rotate(Angle.fromDegrees(sign * validAngle));

    }

    public static Move getMaxMove(Percepts percepts) {
        return getValidMove(getMaxMoveDistance(percepts), percepts);
    }

    public static Move getValidMove(double desiredDistance, Percepts percepts) {

        new Distance(desiredDistance);

        double slowDownModifier = getSlowDownModifier(percepts);
        double maxDistance = getMaxMoveDistance(percepts);

        return new Move(new Distance(slowDownModifier * Math.min(desiredDistance, maxDistance)));

    }

    public static double getMaxRotationDegrees(Percepts percepts) {
        return getScenarioPercepts(percepts).getMaxRotationAngle().getDegrees();
    }

    public static double getMaxMoveDistance(Percepts percepts) {
        if(percepts instanceof IntruderPercepts) {
            return ((IntruderPercepts)percepts)
                .getScenarioIntruderPercepts()
                .getMaxMoveDistanceIntruder()
                .getValue();
        } else if(percepts instanceof GuardPercepts) {
            return ((GuardPercepts)percepts)
                .getScenarioGuardPercepts()
                .getMaxMoveDistanceGuard()
                .getValue();
        } else {
            throw new RuntimeException("Unknown percepts... " + percepts);
        }
    }

    public static double getSlowDownModifier(Percepts percepts) {
        ScenarioPercepts scenarioPercepts = getScenarioPercepts(percepts);
        SlowDownModifiers slowDownModifiers = scenarioPercepts.getSlowDownModifiers();

        double slowDownModifier = 1;
        if(percepts.getAreaPercepts().isInDoor()) {
            slowDownModifier = Math.min(slowDownModifier, slowDownModifiers.getInDoor());
        }

        if(percepts.getAreaPercepts().isInWindow()) {
            slowDownModifier = Math.min(slowDownModifier, slowDownModifiers.getInWindow());
        }

        if(percepts.getAreaPercepts().isInSentryTower()) {
            slowDownModifier = Math.min(slowDownModifier, slowDownModifiers.getInSentryTower());
        }
        return slowDownModifier;
    }

    public static ScenarioPercepts getScenarioPercepts(Percepts percepts) {
        if(percepts instanceof IntruderPercepts) {
            return ((IntruderPercepts)percepts)
                .getScenarioIntruderPercepts()
                .getScenarioPercepts();
        } else if(percepts instanceof GuardPercepts) {
            return ((GuardPercepts)percepts)
                .getScenarioGuardPercepts()
                .getScenarioPercepts();
        } else {
            throw new RuntimeException("Unknown percepts... " + percepts);
        }
    }

}
