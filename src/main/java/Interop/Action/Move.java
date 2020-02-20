package Interop.Action;

import Interop.Geometry.Distance;

/**
 * This class represents intention to make a move.
 *
 * The agent is allowed to specify the move distance in range limited by scenario parameters.
 */
public final class Move implements Action, IntruderAction, GuardAction {

    private Distance distance;

    public Move(Distance distance) {
        this.distance = distance;
    }

    public Distance getDistance() {
        return distance;
    }

}