package Group1.AgentsGroup01.Geometry;

import Interop.Geometry.Point;

public class LineCut {

    private Point start;
    private Point end;

    public LineCut(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    public Point findIntersection(LineCut l2) {
        LineCut l1 = new LineCut(this.start, this.end);

        // inspiration: https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Java
        double a1 = l1.start.getY() - l1.end.getY();
        double b1 = l1.end.getX() - l1.start.getX();
        double c1 = a1 * l1.end.getX() + b1 * l1.end.getY();

        double a2 = l2.start.getY() - l2.end.getY();
        double b2 = l2.end.getX() - l2.start.getX();
        double c2 = a2 * l2.end.getX() + b2 * l2.end.getY();

        double delta = a1 * b2 - a2 * b1;

        // reason: we cannot say == 0 due to precision errors
        // and delta can be negative as well, so abs()
        if (Math.abs(delta) < Precision.threshold) {
            return null;
        }

        // Checks if intersection point is also on both lineCuts
        Point intersectionPoint = new Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);
        if (l1.isPointOnLine(intersectionPoint) && l2.isPointOnLine(intersectionPoint)){
            return intersectionPoint;
        }

        return null;
    }

    public boolean isPointOnLine(Point c) {
        // inspiration: https://stackoverflow.com/questions/11907947/how-to-check-if-a-point-lies-on-a-line-between-2-other-points

        // check if point is on line
        double dxc = c.getX() - start.getX();
        double dyc = c.getY() - start.getY();

        double dxl = end.getX() - start.getX();
        double dyl = end.getY() - start.getY();

        double cross = dxc * dyl - dyc * dxl;

        if (Math.abs(cross) > Precision.threshold)
            return false;


        // check if point is also on the lineCut parts
        if ( (Math.min(start.getY(), end.getY()) > c.getY()) || (Math.max(start.getY(), end.getY()) < c.getY()) ) {
            return false;
        }
        if ( (Math.min(start.getX(), end.getX()) > c.getX()) || (Math.max(start.getX(), end.getX()) < c.getX()) ) {
            return false;
        }

        return true;
    }

    public boolean isPointOnExtendedLine(Point c) {
        // inspiration: https://stackoverflow.com/questions/11907947/how-to-check-if-a-point-lies-on-a-line-between-2-other-points

        // check if point is on line
        double dxc = c.getX() - start.getX();
        double dyc = c.getY() - start.getY();

        double dxl = end.getX() - start.getX();
        double dyl = end.getY() - start.getY();

        double cross = dxc * dyl - dyc * dxl;

        if (Math.abs(cross) > Precision.threshold)
            return false;

        return true;
    }

    public boolean isLineIntersecting(LineCut l2) {
        // https://stackoverflow.com/questions/16314069/calculation-of-intersections-between-line-segments
        // first we check if there is start point of intersection
        Point pointOfIntersection = findIntersection(l2);
        if(pointOfIntersection == null) {
            return false;
        }
        // if there is one, we need to check, if this point lies on both of the lineCuts
        boolean isPointInBothLines = l2.isPointOnLine(pointOfIntersection) && this.isPointOnLine(pointOfIntersection);

        return isPointInBothLines;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public boolean equals(LineCut l2) {
        return this.getStart().getX() - l2.getStart().getX() <= Precision.threshold &&
                this.getStart().getY() - l2.getStart().getY() <= Precision.threshold &&
                this.getEnd().getX() - l2.getEnd().getX() <= Precision.threshold &&
                this.getEnd().getY() - l2.getEnd().getY() <= Precision.threshold;
    }
}
