package Group9.math;

import Interop.Geometry.Vector;

import java.awt.geom.Line2D;

public class Line {

    private final Vector2 start;
    private final Vector2 end;

    public Line(Vector2 start, Vector2 end)
    {
        if(start.getX() <= end.getX())
        {
            this.start = start;
            this.end = end;
        }
        else
        {
            this.start = end;
            this.end = start;
        }

    }

    public Vector2 getStart()
    {
        return this.start;
    }

    public Vector2 getEnd()
    {
        return this.end;
    }

    public boolean intersect(Line other)
    {
        return doBoundingBoxesIntersect(this.getBoundingBox(), other.getBoundingBox())
                && lineSegmentTouchesOrCrossesLine(this, other)
                && lineSegmentTouchesOrCrossesLine(other, this);
    }

    private Vector2[] getBoundingBox()
    {
        return new Vector2[] {
                new Vector2(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY())),
                new Vector2(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()))
        };
    }

    private static boolean doBoundingBoxesIntersect(Vector2[] a, Vector2[] b) {
        return a[0].getX() <= b[1].getX()
                && a[1].getX() >= b[0].getX()
                && a[0].getY() <= b[1].getY()
                && a[1].getY() >= b[0].getY();
    }


    private static double crossProduct(Vector2 a, Vector2 b)
    {
        return a.getX() * b.getY() - b.getX() * a.getY();
    }

    public static boolean isPointOnLine(Line a, Vector2 b) {
        // Move the image, so that a.first is on (0|0)
        Line aTmp = new Line(new Vector2(0, 0), new Vector2(
                a.getEnd().getX() - a.getStart().getX(), a.getEnd().getY() - a.getStart().getY()));
        Vector2 bTmp = new Vector2(b.getY() - a.getStart().getY(), b.getY() - a.getStart().getY());
        double r = crossProduct(aTmp.getEnd(), bTmp);
        return Math.abs(r) < 1.0E-06;
    }

    public static boolean isPointRightOfLine(Line a, Vector2 b) {
        // Move the image, so that a.first is on (0|0)
        Line aTmp = new Line(new Vector2(0, 0), new Vector2(
                a.getEnd().getX() - a.getStart().getX(), a.getEnd().getY() - a.getStart().getY()));
        Vector2 bTmp = new Vector2(b.getX() - a.getStart().getX(), b.getY() - a.getStart().getY());
        return crossProduct(aTmp.getEnd(), bTmp) < 0;
    }

    public static boolean lineSegmentTouchesOrCrossesLine(Line a, Line b) {
        return isPointOnLine(a, b.getStart())
                || isPointOnLine(a, b.getEnd())
                || (isPointRightOfLine(a, b.getStart()) ^ isPointRightOfLine(a,
                b.getEnd()));
    }

}
