package Action;

import Geometry.Angle;

/**
 * This class represents an action of rotation that an agent wants to take.
 */
public class Rotate implements Action, GuardAction, IntruderAction {

    private Angle angle;

    public Rotate(Angle angle) {
        this.angle = angle;
    }

    public Angle getAngle() {
        return angle;
    }

}
