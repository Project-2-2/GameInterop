package Group9.map.objects;

import Group9.map.area.ModifyViewEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class ShadedArea extends MapObject {
    public ShadedArea(PointContainer area, double guardModifier, double intruderModifier) {
        super(area, Arrays.asList(
            new ModifyViewEffect(area, guardModifier, intruderModifier)
        ), ObjectPerceptType.ShadedArea);
    }
}
