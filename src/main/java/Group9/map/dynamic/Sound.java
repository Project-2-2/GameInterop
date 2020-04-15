package Group9.map.dynamic;

import Group9.agent.container.AgentContainer;
import Interop.Percept.Sound.SoundPerceptType;

public class Sound extends DynamicObject<AgentContainer<?>> {

    private final SoundPerceptType type;

    public Sound(SoundPerceptType type, AgentContainer<?> source, double radius, int lifetime) {
        super(source, source.getPosition(), radius, lifetime);
        this.type = type;
    }

    public SoundPerceptType getType() {
        return type;
    }

    @Override
    public Sound clone() {
        return new Sound(type, getSource(), getRadius(), getLifetime());
    }
}
