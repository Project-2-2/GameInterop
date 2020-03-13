package Group9.map.dynamic;

import Group9.math.Vector2;
import Group9.tree.PointContainer;

public class DynamicObject<T> extends PointContainer.Circle {

    private T source;
    private int lifetime;

    public DynamicObject(T source, Vector2 center, double radius, int lifetime) {
        super(center, radius);
        this.source = source;
        this.lifetime = lifetime;
    }

    public T getSource() {
        return source;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

}
