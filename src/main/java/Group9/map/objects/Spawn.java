package Group9.map.objects;

import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public abstract class Spawn extends MapObject  {

    public Spawn(PointContainer.Polygon area) {
        super(area, ObjectPerceptType.EmptySpace);
    }

    public static class Intruder extends Spawn {
        public Intruder(PointContainer.Polygon area) {
            super(area);
        }
    }
    public static class Guard extends Spawn {
        public Guard(PointContainer.Polygon area) {
            super(area);
        }
    }

}
