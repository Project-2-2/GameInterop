package Group6.Geometry.Collection;

import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.Geometry.Quadrilateral;

import java.util.ArrayList;

public class Quadrilaterals {

    private ArrayList<Quadrilateral> quadrilaterals;

    public Quadrilaterals(ArrayList<Quadrilateral> quadrilaterals) {
        this.quadrilaterals = quadrilaterals;
    }

    public boolean isInside(Point point) {
        for (Quadrilateral quadrilateral: quadrilaterals) {
            if(quadrilateral.isInside(point)) return true;
        }
        return false;
    }

}
