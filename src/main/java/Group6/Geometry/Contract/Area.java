package Group6.Geometry.Contract;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Distance;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;

/**
 * @author Tomasz Darmetko
 */
public interface Area {
    boolean hasInside(Point point);
    boolean isInRange(Point point, Distance distance);
    Points getIntersections(LineSegment lineSegment);
}
