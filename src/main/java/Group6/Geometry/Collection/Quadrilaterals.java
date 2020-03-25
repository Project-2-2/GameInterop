package Group6.Geometry.Collection;

import Group6.Geometry.Contract.Area;
import Group6.Geometry.Distance;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tomasz Darmetko
 */
public class Quadrilaterals implements Area {

    private List<Quadrilateral> quadrilaterals;

    public Quadrilaterals(List<Quadrilateral> quadrilaterals) {
        this.quadrilaterals = Collections.unmodifiableList(quadrilaterals);
    }

    public List<Quadrilateral> getAll() {
        return quadrilaterals;
    }

    public Points getIntersections(LineSegment lineSegment) {
        Set<Point> intersections = new HashSet<>();
        for (Quadrilateral quadrilateral: quadrilaterals) {
            intersections.addAll(quadrilateral.getIntersections(lineSegment).getAll());
        }
        return new Points(intersections);
    }

    public boolean isInRange(Point point, Distance distance) {
        for (Quadrilateral quadrilateral: quadrilaterals) {
            if(quadrilateral.isInRange(point, distance)) return true;
        }
        return false;
    }

    public boolean hasInside(Point point) {
        for (Quadrilateral quadrilateral: quadrilaterals) {
            if(quadrilateral.hasInside(point)) return true;
        }
        return false;
    }

}
