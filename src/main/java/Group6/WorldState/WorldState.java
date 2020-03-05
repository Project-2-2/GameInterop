package Group6.WorldState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldState {

    private Scenario scenario;
    private List<IntruderState> intruderStates;
    private List<GuardState> guardStates;
    private Set<Pheromone> pheromones = new HashSet<>();

    public WorldState(Scenario scenario, List<IntruderState> intruderStates, List<GuardState> guardStates) {
        this.scenario = scenario;
        this.intruderStates = intruderStates;
        this.guardStates = guardStates;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public List<IntruderState> getIntruderStates() {
        return intruderStates;
    }

    public List<GuardState> getGuardStates() {
        return guardStates;
    }

    public Set<Pheromone> getPheromones() {
        return pheromones;
    }

}
