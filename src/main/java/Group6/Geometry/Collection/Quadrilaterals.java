package Group6.Geometry.Collection;

import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quadrilaterals {

    private List<Quadrilateral> quadrilaterals;

    public Quadrilaterals(ArrayList<Quadrilateral> quadrilaterals) {
        this.quadrilaterals = Collections.unmodifiableList(quadrilaterals);
    }

    public List<Quadrilateral> getAll() {
        return quadrilaterals;
    }

    public boolean isInside(Point point) {
        for (Quadrilateral quadrilateral: quadrilaterals) {
            if(quadrilateral.isInside(point)) return true;
        }
        return false;
    }

}
