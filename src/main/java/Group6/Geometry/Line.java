package Group6.Geometry;

/**
 * @author Tomasz Darmetko
 */
public class Line {

    private double A;
    private double B;
    private double C;

    public Line(double a, double b, double c) {
        A = a;
        B = b;
        C = c;
    }

    public Line(Point a, Point b) {
        A = a.getY() - b.getY();
        B = b.getX() - a.getX();
        C = a.getX() * b.getY() - b.getX() * a.getY();
    }

    public Line(LineSegment lineSegment) {
        this(lineSegment.getA(), lineSegment.getB());
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

    public double getC() {
        return C;
    }

    public boolean isVertical() {
        return Math.abs(B) < Tolerance.epsilon;
    }

    public double getX() {
        if(!isVertical()) throw new RuntimeException("Line is not vertical!");
        return -C/A;
    }

    public double getYIntercept() {
        return -C/B;
    }

    public double getSlope() {
        return -A/B;
    }

    public double getY(double x) {
        return getSlope() * x + getYIntercept();
    }

    public String toString() {
        return "Line{" +
            "A=" + A +
            ", B=" + B +
            ", C=" + C +
            ", vertical=" + isVertical() +
            ", slope=" + getSlope() +
            '}';
    }

}
