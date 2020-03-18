package Group6.Percept;

import Group6.WorldState.AgentState;
import Group6.WorldState.Pheromone;
import Group6.WorldState.Sound;
import Group6.WorldState.WorldState;
import Interop.Percept.Smell.SmellPercepts;

import java.util.Collections;
import java.util.stream.Collectors;

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
