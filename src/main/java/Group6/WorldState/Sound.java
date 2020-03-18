package Group6.WorldState;

import Group6.Geometry.Distance;
import Group6.Geometry.Point;
import Interop.Geometry.Direction;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;

public class Sound {

    private Point source;
    private Distance audibleInRadius;
    private SoundPerceptType type;

    public Sound(Point source, Distance audibleInRadius, SoundPerceptType type) {
        this.source = source;
        this.audibleInRadius = audibleInRadius;
        this.type = type;
    }

    public Point getSource() {
        return source;
    }

    public Distance getAudibleInRadius() {
        return audibleInRadius;
    }

    public SoundPerceptType getType() {
        return type;
    }

    public boolean canBeHeardFrom(Point point) {
        return source.getDistance(point).getValue() <= audibleInRadius.getValue();
    }

    public SoundPercept toSoundPerceptOf(AgentState agentState) {
        return new SoundPercept(
            type,
            agentState.getPerceivedDirectionTo(source).toInteropDirection()
        );
    }

}
