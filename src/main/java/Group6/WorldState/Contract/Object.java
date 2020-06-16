package Group6.WorldState.Contract;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Contract.Area;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.Set;

/**
 * @author Tomasz Darmetko
 */
public interface Object extends Area {
    public ObjectPerceptType getType();
}
