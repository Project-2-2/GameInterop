package Group9.map.objects;

import Group9.map.area.ModifySpeedEffect;
import Group9.tree.PointContainer;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

public class Door extends MapObject {

    public Door(PointContainer.Quadrilateral area) {
        super(area, Arrays.asList(
                new ModifySpeedEffect(area, 1, 1)
        ), ObjectPerceptType.Door);
    }

}
