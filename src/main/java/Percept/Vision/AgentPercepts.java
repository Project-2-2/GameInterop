package Percept.Vision;

import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents perception of a set of agents.
 */
public class AgentPercepts {

    private Set<AgentPercept> agentPercepts;

    public AgentPercepts(Set<AgentPercept> agentPercepts) {
        // the perception of agents must not be modified after construction!
        this.agentPercepts = Collections.unmodifiableSet(agentPercepts);
    }

    public Set<AgentPercept> getAll() {
        return agentPercepts;
    }

    public AgentPercepts getGuards() {
        return filter((AgentPercept agentPercept) -> {
            return agentPercept.getType() == AgentPerceptType.Guard;
        });
    }

    public AgentPercepts getIntruders() {
        return filter((AgentPercept agentPercept) -> {
            return agentPercept.getType() == AgentPerceptType.Intruder;
        });
    }

    /**
     * Allows to select agents that fulfil a given predicate.
     *
     * @param predicate Receives an agent percept; Decides whether to include the percept in the return set.
     *                  The return value of true allows to include the percept.
     *                  The return value of false allows to exclude the percept.
     *
     * @return The new set of agent percepts filtered out by the result of evaluating the given predicate.
     */
    public AgentPercepts filter(Predicate<? super AgentPercept> predicate) {
        return new AgentPercepts(agentPercepts.stream().filter(predicate).collect(Collectors.toSet()));
    }

}
