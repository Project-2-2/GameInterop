package Group6.Percept;

import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Pheromone;
import Group6.WorldState.WorldState;
import Interop.Percept.Smell.SmellPercepts;

import java.util.stream.Collectors;

/**
 * @author Tomasz Darmetko
 */
public class SmellPreceptsBuilder {
    public SmellPercepts buildPercepts(WorldState worldState, AgentState agentState) {
        return new SmellPercepts(
            worldState.getPheromones().stream()
                .filter((Pheromone smell) -> {
                    return smell.canBeFeltFrom(worldState, agentState.getLocation());
                })
                .map((Pheromone smell) -> {
                    return smell.toSmellPerceptOf(agentState);
                })
                .collect(Collectors.toSet())
        );
    }
}
