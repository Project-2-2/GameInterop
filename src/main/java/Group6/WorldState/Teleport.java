package Group6.WorldState;

import Group6.Geometry.Quadrilateral;

public class Teleport {

    private Quadrilateral thisSide;
    private Quadrilateral thatSide;

    public Teleport(Quadrilateral thisSide, Quadrilateral thatSide) {
        this.thisSide = thisSide;
        this.thatSide = thatSide;
    }

    public Quadrilateral getThisSide() {
        return thisSide;
    }

    public Quadrilateral getThatSide() {
        return thatSide;
    }

}
