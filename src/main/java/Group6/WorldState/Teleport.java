package Group6.WorldState;

import Group6.Geometry.Collection.Quadrilaterals;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;

import java.util.Arrays;

public class Teleport extends Quadrilaterals {

    private Quadrilateral thisSide;
    private Quadrilateral thatSide;

    public Teleport(Quadrilateral thisSide, Quadrilateral thatSide) {
        super(Arrays.asList(thisSide, thatSide));
        this.thisSide = thisSide;
        this.thatSide = thatSide;
    }

    public Quadrilateral getThisSide() {
        return thisSide;
    }

    public Quadrilateral getThatSide() {
        return thatSide;
    }

    public Quadrilateral getTargetArea(Point from) {
        if(!hasInside(from)) throw new RuntimeException(
            "The given point: " + from + " is not in either side of the teleport! No target area can be obtained."
        );
        return getThisSide().hasInside(from) ? getThatSide() : getThisSide();
    }

}
