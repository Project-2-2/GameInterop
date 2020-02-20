package Interop.Action;

import Interop.Geometry.Distance;

/**
 * Represents an intention to sprint issued by an intruder agent.
 *
 * The agent is allowed to specify the sprint distance in range limited by scenario parameters.
 *
 * After a sprint an agent will enter a cool down period.
 */
public final class Sprint implements Action, IntruderAction {

    private Distance distance;

    public Sprint(Distance distance) {
        this.distance = distance;
    }

    public Distance getDistance() {
        return distance;
    }

}
