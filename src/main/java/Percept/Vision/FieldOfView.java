package Percept.Vision;

import Geometry.*;
import Utils.Require;

import java.util.Objects;

/**
 * Represents the field of view of an agent.
 * An agent is able to perceive by vision only in this field of view.
 *
 * Everything in the field of view is relative to an agent.
 * The field of view always faces in the direction that an agent is facing.
 *
 * Field of View:
 *
 *       ⌄--⌄--⌄----- Rays
 *       \  |  /  <-- Range
 *        \ | /
 *         \|/
 *          V  <----- Angle
 *        Agent
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
