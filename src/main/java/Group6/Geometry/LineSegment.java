package Group6.Geometry;

import Group6.Utils;

public class LineSegment {

    private Point a;
    private Point b;

    private double minX;
    private double maxX;

    private double minY;
    private double maxY;

    public LineSegment(Point a, Point b) {

        this.a = a;
        this.b = b;

        if(a.isEqualTo(b)) {
            throw new RuntimeException(
                "The ends of line segment must be two different points!\n" +
                "Point A: " + a + "\n" +
                "Point B: " + b + "\n"
            );
        }

        minX = Math.min(a.getX(), b.getX());
        maxX = Math.max(a.getX(), b.getX());
        minY = Math.min(a.getY(), b.getY());
        maxY = Math.max(a.getY(), b.getY());

    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    /**
     * @link https://silentmatt.com/rectangle-intersection/
     * @link https://martin-thoma.com/how-to-check-if-two-line-segments-intersect/#Where_do_two_line_segments_intersect
     */
    public boolean isIntersecting(LineSegment lineSegment) {
        return minX <= lineSegment.maxX
            && maxX >= lineSegment.minX
            && minY <= lineSegment.maxY
            && maxY >= lineSegment.minY;
    }

    public Point getIntersectionPointWith(LineSegment lineSegment) {

        if(!this.isIntersecting(lineSegment)) {
            throw new RuntimeException(
                "There is no intersection point if two line segments do not intersect!\n" +
                "This: " + this + "\n" +
                "Given:" + lineSegment
            );
        }

        double x1 = a.getX();
        double y1 = a.getY();

        double x2 = b.getX();
        double y2 = b.getY();

        double x3 = lineSegment.getA().getX();
        double y3 = lineSegment.getA().getY();

        double x4 = lineSegment.getB().getX();
        double y4 = lineSegment.getB().getY();

        // @link https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line
        double divisor = (x1-x2)*(y3-y4)-(y1-y2)*(x3-x4);
        double x = ((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4)) / divisor;
        double y = ((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4)) / divisor;

        return new Point(x, y);

    }

    public String toString() {
        return "LineSegment{" +
            "a=" + a +
            ", b=" + b +
            '}';
    }

}
