package Percept;

import Geometry.Direction;
import Percept.Smell.SmellPercepts;
import Percept.Sound.SoundPercepts;
import Percept.Vision.VisionPrecepts;

/**
 * Represents percepts of an agent, including percepts specific to a intruder agent.
 *
 * Please, make sure that you review the documentation of the parent class!
 *
 * @see Percepts
 */
public class IntruderPercepts extends Percepts {

    private Direction targetDirection;

    public IntruderPercepts(
        Direction targetDirection,
        VisionPrecepts vision,
        SoundPercepts sounds,
        SmellPercepts smells,
        boolean wasLastActionExecuted
    ) {
        super(vision, sounds, smells, wasLastActionExecuted);
        this.targetDirection = targetDirection;
    }

    /**
     * @return The direction of the target relative to an agent.
     */
    public Direction getTargetDirection() {
        return targetDirection;
    }

}
