package Group6.WorldState;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Contract.Area;
import Group6.Geometry.Distance;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;
import Group6.WorldState.Contract.Object;
import Group6.WorldState.Object.Teleport;
import Group6.WorldState.Object.WorldStateObjects;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;

/**
 * @author Tomasz Darmetko
 */
public class Teleports implements Area {

    private List<Teleport> teleports;

    public Teleports(List<Teleport> teleports) {
        this.teleports = Collections.unmodifiableList(teleports);
    }

    public List<Teleport> getAll() {
        return teleports;
    }

    public Points getIntersections(LineSegment lineSegment) {
        Set<Point> intersections = new HashSet<>();
        for (Teleport teleport: teleports) {
            intersections.addAll(teleport.getIntersections(lineSegment).getAll());
        }
        return new Points(intersections);
    }

    public boolean isInRange(Point point, Distance distance) {
        for (Teleport teleport: teleports) {
            if(teleport.isInRange(point, distance)) return true;
        }
        return false;
    }

    public boolean hasInside(Point point) {
        for (Teleport teleport: teleports) {
            if(teleport.hasInside(point)) return true;
        }
        return false;
    }

    public Quadrilateral getTargetArea(Point point) {
        Quadrilateral targetArea = null;
        for(Teleport teleport: teleports) {
            if(teleport.hasInside(point)) {
                if(targetArea != null) throw new RuntimeException("Two target areas found! Seems like a map issue.");
                targetArea = teleport.getTargetArea(point);
            }
        }
        if(targetArea == null) throw new RuntimeException("Given point is not inside any teleport area: " + point);
        return targetArea;
    }

    public WorldStateObjects toObjects() {
        return new WorldStateObjects((Collection<Object>) (Collection<?>) teleports);
    }

}
