package Group6.WorldState;

import Group6.Geometry.Quadrilateral;
import Interop.Agent.Guard;
import Group6.Geometry.Direction;
import Group6.Geometry.Point;

import java.util.Random;

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

    static GuardState spawnGuard(Scenario scenario, Guard guard) {
        // TODO: add check between other world state elements
        Quadrilateral spawnArea = scenario.getSpawnAreaGuards();
        return new GuardState(
            guard,
            spawnArea.getRandomPointInside(),
            Direction.random()
        );
    }

}
