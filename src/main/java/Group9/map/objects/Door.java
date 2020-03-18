package Group9.map.objects;

import Group9.gui.DoorGui;
import Group9.map.area.ModifySpeedEffect;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class Door extends MapObject {

    public Door(PointContainer.Polygon area) {
        super(area, Arrays.asList(
                new ModifySpeedEffect(area, 1, 1)
        ), ObjectPerceptType.Door);
    }

    public DoorGui getGui()
    {
        DoorGui door;
        PointContainer.Quadrilateral area = this.getArea().getAsQuadrilateral();
        Vector2[] points = area.getPoints();
        door = new DoorGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
        return door;
    }

}
