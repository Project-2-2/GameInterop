package Group6.WorldState;

import Group6.Geometry.Distance;
import Group6.Geometry.Point;
import Interop.Action.DropPheromone;
import Interop.Percept.Smell.SmellPercept;
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
    private Point source;
    private int droppedAtTurn;
    private SmellPerceptType type;

    public Pheromone(Team team, Point source, int droppedAtTurn, SmellPerceptType type) {
        this.team = team;
        this.source = source;
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

    public Point getSource() {
        return source;
    }

    public int getDroppedAtTurn() {
        return droppedAtTurn;
    }

    public int getAge(int currentTurn) {
        if(currentTurn < droppedAtTurn) throw new RuntimeException("Age of a pheromone can not be negative!");
        return currentTurn - droppedAtTurn;
    }

    public boolean isExpired(WorldState worldState) {
        return getAge(worldState.getTurn()) > worldState.getScenario().getPheromoneExpireRounds();
    }

    public SmellPerceptType getType() {
        return type;
    }

    public Distance getCurrentSmellRadius(WorldState worldState) {

        double age = getAge(worldState.getTurn());
        double expireRounds = worldState.getScenario().getPheromoneExpireRounds();
        double maxRadius = worldState.getScenario().getRadiusPheromone().getValue();

        double radiusReductionRatio = age / expireRounds;
        double radiusReduction = maxRadius * radiusReductionRatio;

        return new Distance(Math.max(0, maxRadius - radiusReduction));

    }

    public boolean canBeFeltFrom(WorldState worldState, Point point) {
        return source.getDistance(point).getValue() <= getCurrentSmellRadius(worldState).getValue();
    }

    public SmellPercept toSmellPerceptOf(AgentState agentState) {
        return new SmellPercept(
            type,
            source.getDistance(agentState.getLocation()).toInteropDistance()
        );
    }

}
