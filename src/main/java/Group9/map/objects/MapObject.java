package Group9.map.objects;

import Group9.map.area.EffectArea;
import Group9.tree.Container;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.ArrayList;
import java.util.List;

public class MapObject implements Container {

    private final PointContainer area;
    private final List<EffectArea> effects;

    private ObjectPerceptType type;

    public MapObject(PointContainer area, ObjectPerceptType type)
    {
        this(area, new ArrayList<>(), type);
    }

    public MapObject(PointContainer area, List<EffectArea> effects, ObjectPerceptType type)
    {
        this.area = area;
        this.effects = effects;
        this.type = type;
    }

    @Override
    public PointContainer getContainer() {
        return this.area;
    }

    public List<EffectArea> getEffects() {
        return this.effects;
    }

    public static boolean is(ObjectPerceptType type)
    {
        return (type == ObjectPerceptType.Door || type == ObjectPerceptType.SentryTower || type == ObjectPerceptType.Wall
                || type == ObjectPerceptType.Window);
    }
}
