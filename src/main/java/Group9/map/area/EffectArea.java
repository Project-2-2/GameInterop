package Group9.map.area;

import Group9.tree.Container;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public abstract class EffectArea implements Container {

    private final PointContainer pointContainer;
    private final ObjectPerceptType type;

    public EffectArea(PointContainer pointContainer, ObjectPerceptType type)
    {
        this.pointContainer = pointContainer;
        this.type = type;
    }

    public ObjectPerceptType getType() {
        return type;
    }

    abstract void applyEffect(); //TODO

    @Override
    public PointContainer getContainer() {
        return pointContainer;
    }

    public static boolean is(ObjectPerceptType type)
    {
        return (type == ObjectPerceptType.ShadedArea || type == ObjectPerceptType.TargetArea
                || type == ObjectPerceptType.Teleport);
    }

}
