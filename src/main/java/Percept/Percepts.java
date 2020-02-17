package Percept;

import Action.*;
import Percept.Smell.SmellPercepts;
import Percept.Sound.SoundPercepts;
import Percept.Vision.VisionPrecepts;
import Utils.Require;

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
    private SmellPercepts smells;

    private boolean wasLastActionExecuted;

    public Percepts(VisionPrecepts vision, SoundPercepts sounds, SmellPercepts smells, boolean wasLastActionExecuted) {
        Require.notNull(vision);
        Require.notNull(sounds);
        Require.notNull(smells);
        this.vision = vision;
        this.sounds = sounds;
        this.smells = smells;
        this.wasLastActionExecuted = wasLastActionExecuted;
    }

    public VisionPrecepts getVision() {
        return vision;
    }

    public SoundPercepts getSounds() {
        return sounds;
    }

    public SmellPercepts getSmells() {
        return smells;
    }

    /**
     * This information allows an agent to recover from issuing an invalid action.
     * @return Whether the last action issued by an agent was executed.
     */
    public boolean wasLastActionExecuted() {
        return wasLastActionExecuted;
    }

}
