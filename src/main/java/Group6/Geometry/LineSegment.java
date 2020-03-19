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

    public LineSegment(Point a, Direction direction, double lenght){
        double x = ((lenght*Math.sin(direction.getDegrees())));
        //b = new Point();
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

    @Override
    public String toString() {

        return "("+getA().getX()+","+getA().getY()+") - ("+getB().getX()+","+getB().getY()+")";
    }
}
