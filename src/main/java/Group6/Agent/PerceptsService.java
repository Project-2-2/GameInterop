package Group6.Agent;

import Interop.Percept.Percepts;
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

    public static ObjectPercepts getTargetPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.TargetArea);
    }

}
