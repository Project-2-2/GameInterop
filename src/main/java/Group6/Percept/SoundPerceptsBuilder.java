package Group6.Percept;

import Group6.WorldState.AgentState;
import Group6.WorldState.WorldState;
import Interop.Percept.Sound.SoundPercepts;

import java.util.Collections;

public class SoundPerceptsBuilder {
    public SoundPercepts buildPercepts(WorldState worldState, AgentState agentState) {
        return new SoundPercepts(Collections.emptySet());
    }
}
