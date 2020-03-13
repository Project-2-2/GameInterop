package Group9.map.objects;

import Group9.map.area.ModifyViewEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class ShadedArea extends MapObject {
    public ShadedArea(PointContainer area) {
        super(area, Arrays.asList(
            new ModifyViewEffect(area, 1, 1) //TODO replace with correct values
        ), ObjectPerceptType.ShadedArea);
    }
}
