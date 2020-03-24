package Group6.Geometry.Contract;

import Group6.Geometry.Distance;
import Group6.Geometry.Point;

public interface Area {
    boolean hasInside(Point point);
    boolean isInRange(Point point, Distance distance);
}
