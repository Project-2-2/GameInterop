package Group9.map.objects;

import Group9.map.area.EffectArea;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.ObjectPerceptType;

import java.awt.*;
import java.util.List;
import java.util.Random;

public abstract class Spawn extends MapObject  {

    public Spawn(PointContainer.Quadrilateral area) {
        super(area, ObjectPerceptType.EmptySpace);
    }

    public static class Intruder extends Spawn {
        public Intruder(PointContainer.Quadrilateral area) {
            super(area);
        }
    }
    public static class Guard extends Spawn {
        public Guard(PointContainer.Quadrilateral area) {
            super(area);
        }
    }

}
