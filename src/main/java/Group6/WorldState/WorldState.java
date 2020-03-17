package Group6.WorldState;

import Group6.Agent.Factory.AgentsFactories;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class WorldState {

    private int turn = 0;
    private Scenario scenario;
    private List<IntruderState> intruderStates;
    private List<GuardState> guardStates;
    private Set<Pheromone> pheromones = new HashSet<>();

    public WorldState(Scenario scenario, List<IntruderState> intruderStates, List<GuardState> guardStates) {
        this.scenario = scenario;
        this.intruderStates = intruderStates;
        this.guardStates = guardStates;
    }

    public WorldState(Scenario scenario, AgentsFactories agentsFactory, String intruderType, String guardType) {

        this.scenario = scenario;

        intruderStates = new ArrayList<>();
        guardStates = new ArrayList<>();

        List<Intruder> intruders = agentsFactory.createIntruders(intruderType, scenario.getNumIntruders());
        for(Intruder intruder: intruders) {
            intruderStates.add(IntruderState.spawnIntruder(scenario, intruder));
        }

        List<Guard> guards = agentsFactory.createGuards(guardType, scenario.getNumGuards());
        for(Guard guard: guards) {
            guardStates.add(GuardState.spawnGuard(scenario, guard));
        }

    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn() {
        turn++;
        getIntruderStates().forEach(IntruderState::nextTurn);
        getGuardStates().forEach(GuardState::nextTurn);
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
