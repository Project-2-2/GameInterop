package Interop.Action;

import Interop.Geometry.Angle;

/**
 * This class represents an intention of rotation issued by an agent.
 *
 * The agent is allowed to specify the angle in range limited by scenario parameters.
 *
 * Positive angle results in clockwise rotation.
 * Negative angle results in anticlockwise rotation.
 */
public final class Rotate implements Action, GuardAction, IntruderAction {

    private Angle angle;

    public Rotate(Angle angle) {
        this.angle = angle;
    }

    public Angle getAngle() {
        return angle;
    }

}
