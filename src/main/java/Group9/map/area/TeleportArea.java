package Group9.map.area;

import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class TeleportArea extends EffectArea {

    public TeleportArea(PointContainer pointContainer) {
        super(pointContainer, ObjectPerceptType.Teleport);
    }

    @Override
    void applyEffect() {

    }

}
