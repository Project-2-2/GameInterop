package Group6.Geometry;

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

    public boolean isIntersecting(LineSegment lineSegment) {
        return a.getX() <= lineSegment.b.getX()
            && lineSegment.a.getX() >= b.getX()
            && a.getY() <= lineSegment.b.getY()
            && lineSegment.a.getY() >= b.getY();
    }

}
