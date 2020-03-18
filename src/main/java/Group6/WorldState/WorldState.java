package Group6.WorldState;

import Group6.Agent.Factory.AgentsFactories;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WorldState {

    private int turn = 0;
    private Scenario scenario;
    private List<IntruderState> intruderStates;
    private List<GuardState> guardStates;
    private Set<Pheromone> pheromones = new HashSet<>();
    private Set<Sound> soundsToPerceiveNow = new HashSet<>();
    private Set<Sound> soundsToPerceiveInNextTurn = new HashSet<>();

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
        propagateSoundsToNextTurn();
        removeExpiredPheromones();
    }

    private void propagateSoundsToNextTurn() {
        // TODO: Is the following code correct?
        // From guidelines: Turn system: World state is updated after each action issued by an agent
        soundsToPerceiveNow = soundsToPerceiveInNextTurn;
        soundsToPerceiveInNextTurn = new HashSet<>();
    }

    private void removeExpiredPheromones() {
        pheromones = pheromones.stream()
            .filter((Pheromone pheromone) -> { return !pheromone.isExpired(this); })
            .collect(Collectors.toSet());
    }

    public void addSound(Sound sound) {
        soundsToPerceiveInNextTurn.add(sound);
    }

    public void addPheromone(Pheromone pheromone) {
        pheromones.add(pheromone);
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

    public Set<Sound> getSounds() {
        return soundsToPerceiveNow;
    }

}
