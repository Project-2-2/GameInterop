package Group9.map.objects;

import Group9.map.area.ModifySpeedEffect;
import Group9.map.area.ModifyViewEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class SentryTower extends MapObject {

    public SentryTower(PointContainer area) {
        super(area, Arrays.asList(
                new ModifyViewEffect(area, 1, 1), //TODO correct values...
                new ModifySpeedEffect(area, 1,1) //TODO correct values...
        ), ObjectPerceptType.SentryTower);
    }

}
