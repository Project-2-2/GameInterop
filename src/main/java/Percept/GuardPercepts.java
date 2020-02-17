package Percept;

import Geometry.Direction;
import Percept.Smell.SmellPercepts;
import Percept.Sound.SoundPercepts;
import Percept.Vision.VisionPrecepts;

/**
 * Represents percepts of an agent, including percepts specific to a guard agent.
 *
 * Please, make sure that you review the documentation of the parent class!
 *
 * @see Percepts
 */
public class GuardPercepts extends Percepts {
    public GuardPercepts(
        VisionPrecepts vision,
        SoundPercepts sounds,
        SmellPercepts smells,
        boolean wasLastActionExecuted
    ) {
        super(vision, sounds, smells, wasLastActionExecuted);
    }

}
