package Group9.map.area;

import Group9.map.area.EffectArea;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class ShadedArea extends EffectArea {

    public ShadedArea(PointContainer pointContainer) {
        super(pointContainer, ObjectPerceptType.ShadedArea);
    }

    @Override
    void applyEffect() {

    }
}
