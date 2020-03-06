package Group9.map.objects;

import Group9.tree.Container;
import Group9.tree.PointContainer;

public class MapObject implements Container {

    private final PointContainer area;

    public MapObject(PointContainer area)
    {
        this.area = area;
    }

    @Override
    public PointContainer getContainer() {
        return this.area;
    }

}
