package Group6.WorldState;

import Group6.GUI.Agent;
import Group6.Geometry.Distance;
import Group6.Geometry.Quadrilateral;
import Interop.Action.Sprint;
import Interop.Agent.Guard;
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

    public void sprint(WorldState worldState, Sprint action) {
        requireNoCooldown(action);
        move(worldState, new Distance(action.getDistance()));
        addCooldown(worldState.getScenario().getSprintCooldown());
        markActionAsExecuted();
    }

    static IntruderState spawnIntruder(Scenario scenario, Intruder intruder) {
        // TODO: add check between other world state elements
        Quadrilateral spawnArea = scenario.getSpawnAreaIntruders();
        return new IntruderState(
            intruder,
            spawnArea.getRandomPointInside(),
            Direction.random()
        );
    }

    public Agent getAgentGui(){

        return new Agent(1,getLocation().getX(),getLocation().getY());

    }

}
