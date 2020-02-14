package Geometry;

import java.util.Collections;
import java.util.List;

public class LineCurve {

    private List<Point> curve;

    public LineCurve(List<Point> curve) {
        // ensure immutability
        this.curve = Collections.unmodifiableList(curve);
    }

    public List<Point> getPointsList() {
        return curve;
    }

}
