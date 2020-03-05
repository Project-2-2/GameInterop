package Group6.Percept;

import Group6.WorldState.AgentState;
import Group6.WorldState.WorldState;
import Interop.Percept.Smell.SmellPercepts;

import java.util.Collections;

public class SmellPreceptsBuilder {
    public SmellPercepts buildPercepts(WorldState worldState, AgentState agentState) {
        return new SmellPercepts(Collections.emptySet());
    }
}
