package Group9.map.objects;

import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

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
