package Group9.map.objects;

import Group9.gui.WindowsGui;
import Group9.gui.targetAreaGui;
import Group9.math.Vector2;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class Window extends MapObject {

    public Window(PointContainer area) {
        super(area, ObjectPerceptType.Window);
    }
    public WindowsGui getGui()
    {
        Vector2[] points = getArea().getAsPolygon().getPoints();
        return new WindowsGui(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
    }

}
