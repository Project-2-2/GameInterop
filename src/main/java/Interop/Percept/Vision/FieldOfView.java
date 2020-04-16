package Interop.Percept.Vision;

import Group9.PiMath;
import Interop.Geometry.*;
import Interop.Utils.Require;
import Interop.Utils.Utils;

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
 *        AgentGui
 *    Coordinate: (0, 0)
 *
 */
public final class FieldOfView {

    private Distance range;
    private Angle viewAngle;

    public FieldOfView(Distance range, Angle viewAngle) {
        Require.notNull(range);
        Require.notNull(viewAngle);
        Require.notNegative(viewAngle.getRadians(), "The view angle must not be negative!");
        if(viewAngle.getDegrees() > 360) {
            throw new RuntimeException(
                "View angle bigger than 360 degree does not make sense!\n" +
                "View angle given (degrees): " + viewAngle.getDegrees()
            );
        }
        this.range = range;
        this.viewAngle = viewAngle;
    }

    public Distance getRange() {
        return range;
    }

    public Angle getViewAngle() {
        return viewAngle;
    }

    public boolean isInView(Point point) {
        return isInRange(point) && isInViewAngle(point);
    }

    private boolean isInViewAngle(Point point) {
        return Angle.fromRadians(0).getDistance(point.getClockDirection()).getRadians()
            <= getViewAngle().getRadians() / 2;
    }

    private boolean isInRange(Point point) {
        return point.getDistanceFromOrigin().getValue() <= getRange().getValue();
    }

}
