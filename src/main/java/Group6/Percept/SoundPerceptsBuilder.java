package Group6.Percept;

import Group6.WorldState.Object.AgentState;
import Group6.WorldState.Sound;
import Group6.WorldState.WorldState;
import Interop.Percept.Sound.SoundPercepts;

import java.util.stream.Collectors;

public class SoundPerceptsBuilder {
    public SoundPercepts buildPercepts(WorldState worldState, AgentState agentState) {
        return new SoundPercepts(
            worldState.getSounds().stream()
                .filter((Sound sound) -> { return sound.canBeHeardFrom(agentState.getLocation()); })
                .map((Sound sound) -> { return sound.toSoundPerceptOf(agentState); })
                .collect(Collectors.toSet())
        );
    }
}
