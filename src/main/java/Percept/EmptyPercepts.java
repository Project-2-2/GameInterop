package Percept;

import Geometry.Angle;
import Geometry.Direction;
import Geometry.Distance;
import Percept.Smell.SmellPercepts;
import Percept.Sound.SoundPercepts;
import Percept.Vision.FieldOfView;
import Percept.Vision.ObjectPercepts;
import Percept.Vision.VisionPrecepts;

import java.util.Collections;

/**
 * In certain situations an agent may not be allowed to perceive the state of an external world.
 * This method creates percepts of an agent that is not allowed to perceive.
 * However, the agent is still allowed to trace the passage of time.
 * Therefore, on each turn, the agent should receive empty percepts created in this class.
 */
public class EmptyPercepts {

    /**
     * @param wasLastActionExecuted Whether the last action issued by an agent was executed.
     * @return Empty percepts.
     */
    public static GuardPercepts createEmptyForGuard(boolean wasLastActionExecuted) {
        return new GuardPercepts(
            getEmptyVisionPercepts(),
            getEmptySoundPercepts(),
            getEmptySmellPercepts(),
            wasLastActionExecuted
        );
    }

    /**
     * @param wasLastActionExecuted Whether the last action issued by an agent was executed.
     * @return Empty percepts.
     */
    public static IntruderPercepts createEmptyForIntruder(boolean wasLastActionExecuted) {
        return new IntruderPercepts(
            Direction.fromDegrees(0),
            getEmptyVisionPercepts(),
            getEmptySoundPercepts(),
            getEmptySmellPercepts(),
            wasLastActionExecuted
        );
    }

    private static VisionPrecepts getEmptyVisionPercepts() {
        return new VisionPrecepts(
            new FieldOfView(new Distance(0), Angle.fromRadians(0)),
            new ObjectPercepts(Collections.emptySet())
        );
    }

    private static SoundPercepts getEmptySoundPercepts() {
        return new SoundPercepts();
    }

    private static SmellPercepts getEmptySmellPercepts() {
        return new SmellPercepts(Collections.emptySet());
    }

}
