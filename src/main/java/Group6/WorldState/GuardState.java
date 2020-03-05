package Group6.WorldState;

import Interop.Agent.Guard;
import Group6.Geometry.Direction;
import Group6.Geometry.Point;

public class GuardState extends AgentState {

    public GuardState(
        Guard guard,
        Point location,
        Direction direction
    ) {
        super(location, direction, 0, false, true);
        this.guard = guard;
    }

    private Guard guard;

    public Guard getGuard() {
        return guard;
    }

}
