package Group6.Geometry;

import Group6.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Quadrilateral {

    private Point pointA;
    private Point pointB;
    private Point pointC;
    private Point pointD;

    private LineSegment sideAB;
    private LineSegment sideBC;
    private LineSegment sideCD;
    private LineSegment sideDA;

    private double minX;
    private double maxX;

    private double minY;
    private double maxY;

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
            (getMinX() + getMaxX()) / 2,
            (getMinY() + getMaxY()) / 2
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

    public Point getRandomPointInside() {
        Point randomPoint;
        do {
            randomPoint = new Point(
                Utils.randomBetween(minX, maxX),
                Utils.randomBetween(minY, maxY)
            );
        } while (!isInside(randomPoint));
        return randomPoint;
    }

    public boolean isInside(Point point) {
        if(!isInBoundingBox(point)) return false;
        int countIntersections = 0;
        LineSegment ray = new LineSegment(point, pointOutsideBoundingBox);
        for (LineSegment side: getAllSides()) {
            if(ray.isIntersecting(side)) countIntersections++;
        }
        return countIntersections % 2 == 1;
    }

    private boolean isInBoundingBox(Point point) {
        return point.getX() >= minX && point.getX() <= maxX
            && point.getY() >= minY && point.getY() <= maxY;
    }

}
