package Group6.Geometry;

public class LineSegment {

    private Point a;
    private Point b;

    public LineSegment(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public LineSegment(Point a, Direction direction, double lenght){
        double x = ((lenght*Math.sin(direction.getDegrees()));
        Point b = new Point()
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

}
