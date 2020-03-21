package Group9.map.dynamic;

import Group9.agent.container.AgentContainer;
import Group9.gui.YellGui;
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

    public YellGui getGui()
    {
        YellGui gui = new YellGui(this.getCenter().getX(), this.getCenter().getY(), this.getRadius());
        return gui;
    }


}
