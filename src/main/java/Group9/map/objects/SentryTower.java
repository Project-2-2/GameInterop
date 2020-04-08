package Group9.map.objects;

import Group9.map.ViewRange;
import Group9.map.area.ModifySpeedEffect;
import Group9.map.area.ModifyViewRangeEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class SentryTower extends MapObject {

    public SentryTower(PointContainer area, double sentrySlowdownModifier, ViewRange viewRange) {
        super(area, Arrays.asList(
                new ModifySpeedEffect(area, sentrySlowdownModifier,sentrySlowdownModifier),
                new ModifyViewRangeEffect(area, viewRange)
        ), ObjectPerceptType.SentryTower);
    }

}
