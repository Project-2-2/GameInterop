package Group6.Agent;

import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

/**
 * @author Tomasz Darmetko
 */
public class PerceptsService {
    public static ObjectPercepts getAgentPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType().isAgent());
    }
    public static ObjectPercepts getWallPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Wall);
    }
    public static ObjectPercepts getTeleportPercepts(Percepts percepts) {
        return percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Teleport);
    }
}
