package Group9.map.objects;

import Group9.map.area.ModifyViewEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class ShadedArea extends MapObject {
    public ShadedArea(PointContainer area, double guardModifier, double intruderModifier) {
        super(area, ObjectPerceptType.ShadedArea);
        this.addEffects(new ModifyViewEffect(this, area, guardModifier, intruderModifier));
    }
}
