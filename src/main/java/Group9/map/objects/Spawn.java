package Group9.map.objects;

import Group9.map.area.EffectArea;
import Group9.tree.PointContainer;
import Interop.Geometry.Vector;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.List;

public abstract class Spawn extends MapObject  {

    public Spawn(PointContainer area, List<EffectArea> effects) {
        super(area, effects, ObjectPerceptType.EmptySpace);
    }

    public static class Intruder extends Spawn {
        public Intruder(PointContainer.Quadrilateral area, List<EffectArea> effects) {
            super(area, effects);
        }
    }
    public static class Guard extends Spawn {
        public Guard(PointContainer.Quadrilateral area, List<EffectArea> effects) {
            super(area, effects);
        }
    }

    public Vector generateRandomLocation()
    {
        //TODO generate random location within the spawn area
        return null;
    }

}
