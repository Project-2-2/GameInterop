package Group6.WorldState.Object;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Distance;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;
import Group6.WorldState.Contract.Object;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomasz Darmetko
 */
public class QuadrilateralObject implements Object {

    private Quadrilateral quadrilateral;
    private ObjectPerceptType objectPerceptType;

    public QuadrilateralObject(Quadrilateral quadrilateral, ObjectPerceptType objectPerceptType) {
        this.quadrilateral = quadrilateral;
        this.objectPerceptType = objectPerceptType;
    }

    public ObjectPerceptType getType() {
        return objectPerceptType;
    }

    public boolean isInRange(Point point, Distance distance) {
        return quadrilateral.isInRange(point, distance);
    }

    public Points getIntersections(LineSegment lineSegment) {
        return quadrilateral.getIntersections(lineSegment);
    }

    public boolean hasInside(Point point) {
        return quadrilateral.hasInside(point);
    }

}
