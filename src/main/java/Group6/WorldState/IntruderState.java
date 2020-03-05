package Group6.WorldState;

import Interop.Agent.Intruder;
import Group6.Geometry.Direction;
import Group6.Geometry.Point;

public class IntruderState extends AgentState {

    public IntruderState(Intruder intruder, Point location, Direction direction) {
        super(location, direction, 0, false, true);
        this.intruder = intruder;
    }

    private Intruder intruder;

    public Intruder getIntruder() {
        return intruder;
    }

}
