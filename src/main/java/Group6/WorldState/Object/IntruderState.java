package Group6.WorldState.Object;

import Group6.GUI.Agent;
import Group6.Geometry.Distance;
import Group6.Geometry.Quadrilateral;
import Group6.WorldState.Scenario;
import Group6.WorldState.WorldState;
import Interop.Action.Sprint;
import Interop.Agent.Intruder;
import Group6.Geometry.Direction;
import Group6.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

/**
 * @author Tomasz Darmetko
 */
public class IntruderState extends AgentState {

    public IntruderState(Intruder intruder, Point location, Direction direction) {
        super(location, direction, 0, false, true);
        this.intruder = intruder;
    }

    private Intruder intruder;

    public Intruder getIntruder() {
        return intruder;
    }

    public ObjectPerceptType getType() {
        return ObjectPerceptType.Intruder;
    }

    public void sprint(WorldState worldState, Sprint action) {
        requireNoCooldown(action);
        move(worldState, new Distance(action.getDistance()));
        addCooldown(worldState.getScenario().getSprintCooldown());
        markActionAsExecuted();
    }

    static public IntruderState spawnIntruder(Scenario scenario, Intruder intruder) {
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
