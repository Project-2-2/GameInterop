package Group9.map.area;

import Group9.tree.Container;
import Group9.tree.PointContainer;

public abstract class EffectArea implements Container {

    private final PointContainer pointContainer;

    public EffectArea(PointContainer pointContainer)
    {
        this.pointContainer = pointContainer;
    }

    abstract void applyEffect(); //TODO

    @Override
    public PointContainer getContainer() {
        return pointContainer;
    }
}
