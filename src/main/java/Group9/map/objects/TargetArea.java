package Group9.map.objects;

import Group9.gui.targetAreaGui;
import Group9.map.objects.MapObject;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class TargetArea extends MapObject {

    public TargetArea(PointContainer.Polygon area) {
        super(area, ObjectPerceptType.TargetArea);
    }
    public targetAreaGui getGui()
    {
        Vector2[] points = getArea().getAsPolygon().getPoints();
        return new targetAreaGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
    }

}
