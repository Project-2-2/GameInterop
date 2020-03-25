package Group6.WorldState.Object;

import Group6.GUI.Agent;
import Group6.Geometry.Distance;
import Group6.Geometry.Quadrilateral;
import Group6.WorldState.Scenario;
import Group6.WorldState.Sound;
import Group6.WorldState.WorldState;
import Interop.Action.Move;
import Interop.Action.Yell;
import Interop.Agent.Guard;
import Group6.Geometry.Direction;
import Group6.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

/**
 * @author Tomasz Darmetko
 */
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

    public ObjectPerceptType getType() {
        return ObjectPerceptType.Guard;
    }

    public void move(WorldState worldState, Move action) {
        if(action.getDistance().getValue() > worldState.getScenario().getMaxMoveDistanceGuard().getValue()) {
            throw new IllegalAction("move", "move longer than allowed for guard");
        }
        super.move(worldState, action);
    }

    public void yell(WorldState worldState, Yell action) {
        requireNoCooldown(action);
        worldState.addSound(Sound.createYell(worldState.getScenario(), this));
        markActionAsExecuted();
    }

    static public GuardState spawnGuard(Scenario scenario, Guard guard) {
        // TODO: add check between other world state elements
        Quadrilateral spawnArea = scenario.getSpawnAreaGuards();
        return new GuardState(
            guard,
            spawnArea.getRandomPointInside(),
            Direction.random()
        );
    }
    public Agent getAgentGui(){

        return new Agent(1,getLocation().getX(),getLocation().getY());

    }

}
