package Group10.Competition;

import Interop.Geometry.Point;

public class Line {

    private Point ls;
    private Point le;

    public Line(Point ls, Point le){
        this.ls = ls;
        this.le = le;
    }

    public Point getIntersection(Line l2) {

        Line l1 = new Line(this.ls, this.le);

        double a1 = l1.ls.getY() - l1.le.getY();
        double b1 = l1.le.getX() - l1.ls.getX();
        double c1 = a1 * l1.le.getX() + b1 * l1.le.getY();

        double a2 = l2.ls.getY() - l2.le.getY();
        double b2 = l2.le.getX() - l2.ls.getX();
        double c2 = a2 * l2.le.getX() + b2 * l2.le.getY();

        double delta = a1 * b2 - a2 * b1;

        if (Math.abs(delta) < Bias.bias) {
            return null;
        }

        Point intersectionPoint = new Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta);

        return intersectionPoint;
    }

    public boolean isPointOnLine(Point c) {

        double dxc = c.getX() - ls.getX();
        double dyc = c.getY() - ls.getY();

        double dxl = le.getX() - ls.getX();
        double dyl = le.getY() - ls.getY();

        double cross = dxc * dyl - dyc * dxl;

        if (Math.abs(cross) > Bias.bias)
            return false;

        if ( (Math.min(ls.getY(), le.getY()) > c.getY()) || (Math.max(ls.getY(), le.getY()) < c.getY()) ) {
            return false;
        }
        if ( (Math.min(ls.getX(), le.getX()) > c.getX()) || (Math.max(ls.getX(), le.getX()) < c.getX()) ) {
            return false;
        }

        return true;
    }
    public boolean isPointOnExtendedLine(Point c) {

        double dxc = c.getX() - ls.getX();
        double dyc = c.getY() - ls.getY();

        double dxl = le.getX() - ls.getX();
        double dyl = le.getY() - ls.getY();

        double cross = dxc * dyl - dyc * dxl;

        if (Math.abs(cross) > Bias.bias)
            return false;

        return true;
    }


    public boolean isLineIntersecting(Line l2) {

        Point pointOfIntersection = getIntersection(l2);
        if(pointOfIntersection == null) {
            return false;
        }
        boolean isPointInBothLines = l2.isPointOnLine(pointOfIntersection) && this.isPointOnLine(pointOfIntersection);

        return isPointInBothLines;
    }

    public Point getLs() {
        return ls;
    }

    public Point getLe() {
        return le;
    }

    public boolean equals(Line l2) {
        return this.getLs().getX() - l2.getLs().getX() <= Bias.bias &&
                this.getLs().getY() - l2.getLs().getY() <= Bias.bias &&
                this.getLe().getX() - l2.getLe().getX() <= Bias.bias &&
                this.getLe().getY() - l2.getLe().getY() <= Bias.bias;
    }
}
