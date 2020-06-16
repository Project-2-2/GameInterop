package Group6.Geometry;

import Group6.Utils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Tomasz Darmetko
 */
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
     * @link https://en.wikipedia.org/wiki/Line–line_intersection
     */
    public boolean isIntersecting(LineSegment lineSegment) {
        Point intersection = getIntersectionPointWith(lineSegment);
        if(intersection == null) return false;
        return this.includes(intersection) && lineSegment.includes(intersection);
    }
    public double angleOfPoints(Point a, Point b){
        if(a.getY() != b.getY()) { //check if it is a horizontal line, then we do only a 90 degree shift so return 0
            minX = Math.min(a.getX(), b.getX());
            maxX = Math.max(a.getX(), b.getX());
            minY = Math.min(a.getY(), b.getY());
            maxY = Math.max(a.getY(), b.getY());
            double deltaY = maxY - minY;
            double deltaX = maxX - minX;
            System.out.println("delta x" + deltaX);
            System.out.println("delta y" + deltaY);
            double radians = deltaY / deltaX;
            double result = Math.toDegrees(Math.atan(radians));
            if (b.getX() < a.getX())
                result = -1 * result;
            System.out.println("results " + result);
            return result;

        }
        else{
            return 0;
        }

    }

    public boolean includes(Point point) {
        // must be inside bounding box
        if(minX > point.getX() || maxX < point.getX()) return false;
        if(minY > point.getY() || maxY < point.getY()) return false;
        // must fulfill line equation: (y−yA)(xB−xA)−(x−xA)(yB−yA) = 0
        return Math.abs(
            (point.getY() - a.getY()) * (b.getX() - a.getX())
            -
            (point.getX() - a.getX()) * (b.getY() - a.getY())
        ) < Tolerance.epsilon;
    }

    public Point getIntersectionPointWith(LineSegment lineSegment) {

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

        if(Math.abs(divisor) < Tolerance.epsilon) return null;

        double x = ((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4)) / divisor;
        double y = ((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4)) / divisor;

        return new Point(x, y);

    }

    public Line toLine() {
        return new Line(a, b);
    }

    public String toString() {
        return "LineSegment{" +
            "a=" + a +
            ", b=" + b +
            '}';
    }

}
