package Group9.map.objects;

import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class Door extends MapObject {

    public Door(PointContainer.Quadrilateral area) {
        super(area, ObjectPerceptType.Door);
    }

}
