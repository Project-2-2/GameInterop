package Group9.map.objects;

import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

public class TeleportArea extends MapObject {

    public TeleportArea(PointContainer area) {
        super(area, ObjectPerceptType.Teleport);
    }

}
