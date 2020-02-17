package Percept.Vision;

import Geometry.*;
import Utils.Require;

import java.util.Objects;

/**
 * Represents the field of view of an agent.
 * An agent is able to perceive by vision only in this field of view.
 *
 * Everything in the field of view is relative to an agent.
 * The agent is the origin of the coordinate system of that agent percepts.
 * The field of view always faces in the direction that the agent is facing.
 * The direction the agent is facing coincides with the positive part of y-axis in the agent coordinate system.
 *
 * Field of View:
 *
 *       ⌄--⌄--⌄----- Rays
 *       \  |  /  <-- Range
 *        \ | /
 *         \|/
 *          V  <----- Angle
 *        Agent
 *    Coordinate: (0, 0)
 *
 */
public class FieldOfView {

    private Distance range;
    private Angle angle;

    public FieldOfView(Distance range, Angle angle) {
        Require.notNull(range);
        Require.notNull(angle);
        this.range = range;
        this.angle = angle;
    }

    public Distance getRange() {
        return range;
    }

    public Angle getAngle() {
        return angle;
    }

}
