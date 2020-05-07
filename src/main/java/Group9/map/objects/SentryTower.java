package Group9.map.objects;

import Group9.map.ViewRange;
import Group9.map.area.ModifySpeedEffect;
import Group9.map.area.ModifyViewRangeEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class SentryTower extends MapObject {

    public SentryTower(PointContainer area, double sentrySlowdownModifier, ViewRange viewRange) {
        super(area, ObjectPerceptType.SentryTower);
        this.addEffects(
                new ModifySpeedEffect(this, area, sentrySlowdownModifier,sentrySlowdownModifier),
                new ModifyViewRangeEffect(this, area, viewRange)
        );
    }

}
