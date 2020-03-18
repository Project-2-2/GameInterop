package Group6.WorldState;

import Group6.Geometry.Point;
import Interop.Action.DropPheromone;
import Interop.Percept.Smell.SmellPerceptType;

public class Pheromone {

    public enum Team {
        Intruders, Guards;
        public static Team getByAgentState(AgentState agentState) {
            if(agentState instanceof IntruderState) return Intruders;
            if(agentState instanceof GuardState) return Guards;
            throw new RuntimeException("Unrecognized agent state: " + agentState.getClass().getName());
        }
    }

    private Team team;
    private Point location;
    private int droppedAtTurn;
    private SmellPerceptType type;

    public Pheromone(Team team, Point location, int droppedAtTurn, SmellPerceptType type) {
        this.team = team;
        this.location = location;
        this.droppedAtTurn = droppedAtTurn;
        this.type = type;
    }

    public static Pheromone createByAgent(WorldState worldState, AgentState agentState, DropPheromone action) {
        return new Pheromone(
            Team.getByAgentState(agentState),
            agentState.getLocation(),
            worldState.getTurn(),
            action.getType()
        );
    }

    public Team getTeam() {
        return team;
    }

    public Point getLocation() {
        return location;
    }

    public int getDroppedAtTurn() {
        return droppedAtTurn;
    }

    public SmellPerceptType getType() {
        return type;
    }

}
