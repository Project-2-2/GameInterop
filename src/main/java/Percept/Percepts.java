package Percept;

import Percept.Sound.SoundPercepts;
import Percept.Vision.VisionPrecepts;

/**
 * Represents the percepts as perceived by an agent.
 */
public class Percepts {

    private VisionPrecepts vision;
    private SoundPercepts sounds;

    public Percepts(VisionPrecepts vision, SoundPercepts sounds) {
        this.vision = vision;
        this.sounds = sounds;
    }

    public VisionPrecepts getVision() {
        return vision;
    }

    public SoundPercepts getSounds() {
        return sounds;
    }

}
