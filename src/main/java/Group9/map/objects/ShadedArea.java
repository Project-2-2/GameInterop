package Group9.map.objects;

import Group9.gui.ShadedareaGui;
import Group9.map.area.ModifyViewEffect;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class ShadedArea extends MapObject {
    public ShadedArea(PointContainer area, double guardModifier, double intruderModifier) {
        super(area, Arrays.asList(
            new ModifyViewEffect(area, guardModifier, intruderModifier)
        ), ObjectPerceptType.ShadedArea);
    }
    public ShadedareaGui getGui()
    {
        Vector2[] points = getArea().getAsQuadrilateral().getPoints();
        return new ShadedareaGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
    }
}
