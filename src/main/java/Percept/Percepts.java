package Percept;

import Action.*;
import Geometry.Angle;
import Geometry.Distance;
import Percept.Scenario.ScenarioPercepts;
import Percept.Smell.SmellPercepts;
import Percept.Sound.SoundPercepts;
import Percept.Vision.FieldOfView;
import Percept.Vision.ObjectPercepts;
import Percept.Vision.VisionPrecepts;
import Utils.Require;

import java.util.Collections;
import java.util.Objects;

/**
 * Represents the percepts of an agent.
 *
 * Percept: A perceived object as it exists in the mind of someone perceiving it;
 *          the mental impression that is the result of perceiving something.
 *
 * All precepts are relative to the agent.
 * This means that each agent has effectively it's own coordinate system.
 * The agent is always located in the point (0, 0) of that coordinate system.
 * The direction the agent is facing coincides with the positive part of y-axis in the agent coordinate system.
 * All direction percepts (angles) are relative to the y-axis, and angles grow clockwise.
 *
 * You should also take a look at:
 * @see Percept.Vision.FieldOfView
 * @see Utils.Utils#clockAngle
 */
public class Percepts {

    private VisionPrecepts vision;
    private SoundPercepts sounds;
    private SmellPercepts smells;
    private AreaPercepts areaPercepts;

    private boolean wasLastActionExecuted;

    protected Percepts(
        VisionPrecepts vision,
        SoundPercepts sounds,
        SmellPercepts smells,
        AreaPercepts areaPercepts,
        boolean wasLastActionExecuted
    ) {
        Require.notNull(vision);
        Require.notNull(sounds);
        Require.notNull(smells);
        Require.notNull(areaPercepts);
        this.vision = vision;
        this.sounds = sounds;
        this.smells = smells;
        this.areaPercepts = areaPercepts;
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

    public AreaPercepts getAreaPercepts() {
        return areaPercepts;
    }

    /**
     * This information allows an agent to recover from issuing an invalid action.
     * @return Whether the last action issued by an agent was executed.
     */
    public boolean wasLastActionExecuted() {
        return wasLastActionExecuted;
    }

}
