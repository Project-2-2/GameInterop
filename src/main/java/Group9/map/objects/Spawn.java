package Group9.map.objects;

import Group9.map.area.EffectArea;
import Group9.tree.PointContainer;

import java.util.List;

public abstract class Spawn extends MapObject  {

    public Spawn(PointContainer area, List<EffectArea> effects) {
        super(area, effects);
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

}
