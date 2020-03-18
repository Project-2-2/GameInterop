package Group6.WorldState;

import Group6.Geometry.Contract.Area;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;

import java.util.Collections;
import java.util.List;

public class Teleports implements Area {

    private List<Teleport> teleports;

    public Teleports(List<Teleport> teleports) {
        this.teleports = Collections.unmodifiableList(teleports);
    }

    public List<Teleport> getAll() {
        return teleports;
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

}
