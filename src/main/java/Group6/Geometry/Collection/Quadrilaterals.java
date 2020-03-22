package Group6.Geometry.Collection;

import Group6.Geometry.Contract.Area;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;

import java.util.Collections;
import java.util.List;

public class Quadrilaterals implements Area {

    private List<Quadrilateral> quadrilaterals;

    public Quadrilaterals(List<Quadrilateral> quadrilaterals) {
        this.quadrilaterals = Collections.unmodifiableList(quadrilaterals);
    }

    public List<Quadrilateral> getAll() {
        return quadrilaterals;
    }

    public boolean hasInside(Point point) {
        for (Quadrilateral quadrilateral: quadrilaterals) {
            if(quadrilateral.hasInside(point)) return true;
        }
        return false;
    }

}