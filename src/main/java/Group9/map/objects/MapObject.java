package Group9.map.objects;

import Group9.map.area.EffectArea;
import Group9.tree.Container;
import Group9.tree.PointContainer;

import java.util.ArrayList;
import java.util.List;

public class MapObject implements Container {

    private final PointContainer area;
    private final List<EffectArea> effects;

    public MapObject(PointContainer area)
    {
        this(area, new ArrayList<>());
    }

    public MapObject(PointContainer area, List<EffectArea> effects)
    {
        this.area = area;
        this.effects = effects;
    }

    @Override
    public PointContainer getContainer() {
        return this.area;
    }

    public List<EffectArea> getEffects() {
        return this.effects;
    }
}
