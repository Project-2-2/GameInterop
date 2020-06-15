package Group6.Geometry;

import Group6.GUI.*;
import Group6.Geometry.Collection.Points;
import Group6.Geometry.Contract.Area;
import Group6.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tomasz Darmetko
 */
public class Quadrilateral implements Area {

    private Point pointA;
    private Point pointB;
    private Point pointC;
    private Point pointD;

    private LineSegment sideAB;
    private LineSegment sideBC;
    private LineSegment sideCD;
    private LineSegment sideDA;

    private double minX = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY;

    private double minY = Double.POSITIVE_INFINITY;
    private double maxY = Double.NEGATIVE_INFINITY;

    /**
     * Point outside the bounding box. Useful for ray casting algorithm.
     */
    private Point pointOutsideBoundingBox;

    public Quadrilateral(Point pointA, Point pointB, Point pointC, Point pointD) {

        this.pointA = pointA;
        this.pointB = pointB;
        this.pointC = pointC;
        this.pointD = pointD;

        sideAB = new LineSegment(pointA, pointB);
        sideBC = new LineSegment(pointB, pointC);
        sideCD = new LineSegment(pointC, pointD);
        sideDA = new LineSegment(pointD, pointA);

        for (Point point: getAllPoints()) {
            minX = Math.min(minX, point.getX());
            maxX = Math.max(maxX, point.getX());
            minY = Math.min(minY, point.getY());
            maxY = Math.max(maxY, point.getY());
        }

        pointOutsideBoundingBox = new Point(maxX + 1, maxY + 1);

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

    public Point getCenter() {
        return new Point(
            getMinX() + ((getMaxX() - getMinX()) / 2),
            getMinY() + ((getMaxY() - getMinY()) / 2)
        );
    }

    public LineSegment getSideAB() {
        return sideAB;
    }

    public LineSegment getSideBC() {
        return sideBC;
    }

    public LineSegment getSideCD() {
        return sideCD;
    }

    public LineSegment getSideDA() {
        return sideDA;
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

    public List<Point> getAllPoints() {
        return Arrays.asList(
            pointA,
            pointB,
            pointC,
            pointD
        );
    }

    public List<LineSegment> getAllSides() {
        return Arrays.asList(
            sideAB,
            sideBC,
            sideCD,
            sideDA
        );
    }

    public Points getIntersections(LineSegment lineSegment) {
        Set<Point> points = new HashSet<>();
        for (LineSegment side: getAllSides()) {
            if(!side.isIntersecting(lineSegment)) continue;
            points.add(side.getIntersectionPointWith(lineSegment));
        }
        return new Points(points);
    }

    public boolean isInRange(Point point, Distance distance) {
        for (Point vertex: getAllPoints()) {
            if(vertex.getDistance(point).getValue() <= distance.getValue()) return true;
        }
        return false;
    }

    public Point getRandomPointInside() {
        Point randomPoint;
        do {
            randomPoint = new Point(
                Utils.randomBetween(minX, maxX),
                Utils.randomBetween(minY, maxY)
            );
        } while (!hasInside(randomPoint));
        return randomPoint;
    }

    /**
     * @link https://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
     */
    public boolean hasInside(Point point) {

        if(!isInBoundingBox(point)) return false;

        // From Wikipedia:
        // One simple way of finding whether the point is inside or outside a simple polygon is to test how many times
        // a ray, starting from the point and going in any fixed direction, intersects the edges of the polygon.
        // If the point is on the outside of the polygon the ray will intersect its edge an even number of times.
        // If the point is on the inside of the polygon then it will intersect the edge an odd number of times.
        int countIntersections = 0;

        LineSegment ray = new LineSegment(point, pointOutsideBoundingBox);
        for (LineSegment side: getAllSides()) {

            if(!ray.isIntersecting(side)) {
                continue;
            }

            Point intersection = ray.getIntersectionPointWith(side);

            // From Wikipedia:
            // If the ray passes exactly through a vertex of a polygon, then it will intersect 2 segments at their endpoints.
            // A similar problem arises with horizontal segments that happen to fall on the ray.
            // The issue is solved as follows: If the intersection point is a vertex of a tested polygon side,
            // then the intersection counts only if the second vertex of the side lies below the ray.
            // This is effectively equivalent to considering vertices on the ray as lying slightly above the ray.
            if(intersection.isEqualTo(side.getA())) {
                boolean isBelowRay = side.getB().getY() < intersection.getY();
                if(isBelowRay) countIntersections++;
            } else
            if(intersection.isEqualTo(side.getB())) {
                boolean isBelowRay = side.getA().getY() < intersection.getY();
                if(isBelowRay) countIntersections++;
            } else {
                countIntersections++;
            }

        }

        return countIntersections % 2 == 1;

    }

    private boolean isInBoundingBox(Point point) {
        return point.getX() >= minX && point.getX() <= maxX
            && point.getY() >= minY && point.getY() <= maxY;
    }

    public Wall getWallGui(){
        double[] x = {pointA.getX(),pointB.getX(),pointC.getX(),pointD.getX()};
        double[] y = {pointA.getY(),pointB.getY(),pointC.getY(),pointD.getY()};
        return new Wall(x, y);
    }
    public Door getDoorGui(){
        double[] x = {pointA.getX(),pointB.getX(),pointC.getX(),pointD.getX()};
        double[] y = {pointA.getY(),pointB.getY(),pointC.getY(),pointD.getY()};
        return new Door(x, y);
    }
    public Sentry getSentryGui(){
        double[] x = {pointA.getX(),pointB.getX(),pointC.getX(),pointD.getX()};
        double[] y = {pointA.getY(),pointB.getY(),pointC.getY(),pointD.getY()};
        return new Sentry(x, y);
    }
    public Spawn getSpawnGui(){
        double[] x = {pointA.getX(),pointB.getX(),pointC.getX(),pointD.getX()};
        double[] y = {pointA.getY(),pointB.getY(),pointC.getY(),pointD.getY()};
        return new Spawn(x, y);
    }
    public Target getTargetGui(){
        double[] x = {pointA.getX(),pointB.getX(),pointC.getX(),pointD.getX()};
        double[] y = {pointA.getY(),pointB.getY(),pointC.getY(),pointD.getY()};
        return new Target(x, y);
    }

    public Window getWindowGui(){
        double[] x = {pointA.getX(),pointB.getX(),pointC.getX(),pointD.getX()};
        double[] y = {pointA.getY(),pointB.getY(),pointC.getY(),pointD.getY()};
        return new Window(x, y);
    }

    public ShadedArea getShadedAreaGui(){
        double[] x = {pointA.getX(),pointB.getX(),pointC.getX(),pointD.getX()};
        double[] y = {pointA.getY(),pointB.getY(),pointC.getY(),pointD.getY()};
        return new ShadedArea(x, y);
    }
    public double getHeight(){
        return (pointA.getDistance(pointD).getValue());
    }
    public double getWidth(){
        return (pointA.getDistance(pointB).getValue());
    }
}
