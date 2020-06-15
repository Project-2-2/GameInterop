package Group6.Agent;

import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Utils.Utils;

import java.util.Iterator;

/**
 * @author Tomasz Darmetko
 */
public class PerceptsService {

    public static double getMeanClockDirection(ObjectPercepts objectPercepts) {
        double sum = objectPercepts
            .getAll()
            .stream()
            .mapToDouble(percept -> percept.getPoint().getClockDirection().getDegrees())
            .reduce(0, Double::sum);

        return sum / objectPercepts.getAll().size();
    }

    public static double getMeanDirection(ObjectPercepts objectPercepts) {
        double sum = objectPercepts
            .getAll()
            .stream()
            .mapToDouble(percept -> Utils.getSignedDistanceBetweenAngles(
                0, percept.getPoint().getClockDirection().getRadians()
            ))
            .reduce(0, Double::sum);

        return Math.toDegrees(sum / objectPercepts.getAll().size());
    }

    public static double getMeanDirection(SoundPercepts objectPercepts) {
        double sum = objectPercepts
            .getAll()
            .stream()
            .mapToDouble(percept -> percept.getDirection().getDegrees())
            .reduce(0, Double::sum);

        return sum / objectPercepts.getAll().size();
    }

    public static double getMeanDistance(ObjectPercepts objectPercepts) {
        double sum = objectPercepts
            .getAll()
            .stream()
            .mapToDouble(percept -> percept.getPoint().getDistanceFromOrigin().getValue())
            .reduce(0, Double::sum);

        return sum / objectPercepts.getAll().size();
    }

    public static ObjectPercepts getAgentPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType().isAgent());
    }

    public static ObjectPercepts getIntruderPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Intruder);
    }

    public static ObjectPercepts getWallPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Wall);
    }

    public static ObjectPercepts getDoorPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Door);
    }

    public static ObjectPercepts getPassagePercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept ->
                objectPercept.getType() == ObjectPerceptType.Door ||
                objectPercept.getType() == ObjectPerceptType.Window
            );
    }

    public static ObjectPercepts getTeleportPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Teleport);
    }

    public static SoundPercepts getYellPercepts(Percepts percepts) {
        return percepts
            .getSounds()
            .filter(soundPercept -> soundPercept.getType() == SoundPerceptType.Yell);
    }

    public static ObjectPercepts getTargetPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.TargetArea);
    }

    public static double getCaptureDistance(Percepts percepts) {
        return getScenarioPercepts(percepts).getCaptureDistance().getValue();
    }

    public static ScenarioPercepts getScenarioPercepts(Percepts percepts) {
        if(percepts instanceof IntruderPercepts) return ((IntruderPercepts) percepts)
            .getScenarioIntruderPercepts()
            .getScenarioPercepts();
        if(percepts instanceof GuardPercepts) return ((GuardPercepts) percepts)
            .getScenarioGuardPercepts()
            .getScenarioPercepts();
        throw new RuntimeException("Unknown percepts: " + percepts);
    }

}
