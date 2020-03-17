package Group6.WorldState;

import Group6.Geometry.Distance;
import Group6.Geometry.Point;
import Interop.Percept.Sound.SoundPerceptType;

public class Sound {

    private Point source;
    private Distance distance;
    private SoundPerceptType type;

    public Sound(Point source, Distance distance, SoundPerceptType type) {
        this.source = source;
        this.distance = distance;
        this.type = type;
    }

    public Point getSource() {
        return source;
    }

    public Distance getDistance() {
        return distance;
    }

    public SoundPerceptType getType() {
        return type;
    }

}
