package Group6.WorldState.Object;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Collection.Quadrilaterals;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;
import Group6.WorldState.Contract.Object;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Arrays;

/**
 * @author Tomasz Darmetko
 */
public class Teleport extends Quadrilaterals implements Object {

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

    public ObjectPerceptType getType() {
        return ObjectPerceptType.Teleport;
    }

}
