package Group6.Geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Quadrilateral {

    private Point pointA;
    private Point pointB;
    private Point pointC;
    private Point pointD;

    private LineSegment AB;
    private LineSegment BC;
    private LineSegment CD;
    private LineSegment DA;

    public Quadrilateral(Point pointA, Point pointB, Point pointC, Point pointD) {

        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
        this.pointD = pointD;

        AB = new LineSegment(pointA, pointB);
        BC = new LineSegment(pointB, pointC);
        CD = new LineSegment(pointC, pointD);
        DA = new LineSegment(pointD, pointA);

    }

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public Point getPointC() {
        return pointC;
    }

    public Point getPointD() {
        return pointD;
    }

    public LineSegment getAB() {
        return AB;
    }

    public LineSegment getBC() {
        return BC;
    }

    public LineSegment getCD() {
        return CD;
    }

    public LineSegment getDA() {
        return DA;
    }

    public List<Point> getAllPoints() {
        return Arrays.asList(
            pointA,
            pointB,
            pointC,
            pointD
        );
    }

    public List<LineSegment> getAllLineSegments() {
        return Arrays.asList(
            AB,
            BC,
            CD,
            DA
        );
    }

}
