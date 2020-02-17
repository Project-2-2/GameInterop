package Percept;

import Action.*;
import Percept.Sound.SoundPercepts;
import Percept.Vision.VisionPrecepts;

import java.util.Objects;

/**
 * Represents the percepts of an agent.
 *
 * Percept: A perceived object as it exists in the mind of someone perceiving it;
 *          the mental impression that is the result of perceiving something.
 */
public class Percepts {

    private VisionPrecepts vision;
    private SoundPercepts sounds;

    private boolean wasLastActionExecuted;

    public Percepts(VisionPrecepts vision, SoundPercepts sounds, boolean wasLastActionExecuted) {
        Require.notNull(vision);
        Require.notNull(sounds);
        this.vision = vision;
        this.sounds = sounds;
        this.wasLastActionExecuted = wasLastActionExecuted;
    }

    public VisionPrecepts getVision() {
        return vision;
    }

    public SoundPercepts getSounds() {
        return sounds;
    }

    /**
     * This information allows an agent to recover from issuing an invalid action.
     * @return Whether the last action issued by an agent was executed.
     */
    public boolean wasLastActionExecuted() {
        return wasLastActionExecuted;
    }

}
