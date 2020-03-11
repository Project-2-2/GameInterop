package Group9.map.dynamic;

import Group9.agent.AgentContainer;
import Group9.math.Vector2;
import Interop.Percept.Sound.SoundPerceptType;

public class Sound extends DynamicObject<AgentContainer<?>> {

    private final SoundPerceptType type;

    public Sound(SoundPerceptType type, AgentContainer<?> source, Vector2 center, double radius, int lifetime) {
        super(source, center, radius, lifetime);
        this.type = type;
    }

    public SoundPerceptType getType() {
        return type;
    }


}
