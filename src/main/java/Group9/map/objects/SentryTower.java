package Group9.map.objects;

import Group9.gui.SentryGui;
import Group9.map.area.ModifySpeedEffect;
import Group9.map.area.ModifyViewEffect;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class SentryTower extends MapObject {

    public SentryTower(PointContainer area, double sentrySlowdownModifier) {
        super(area, Arrays.asList(
                new ModifyViewEffect(area, 1, 1), //TODO correct values...
                new ModifySpeedEffect(area, sentrySlowdownModifier,sentrySlowdownModifier)
        ), ObjectPerceptType.SentryTower);
    }
    public SentryGui getGui()
    {
        Vector2[] points = this.getArea().getAsPolygon().getPoints();
        SentryGui sentry = new SentryGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
        return sentry;
    }

}
