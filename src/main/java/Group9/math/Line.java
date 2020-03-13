package Group9.math;

import Group9.tree.PointContainer;
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

    public Vector2 getNormal()
    {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        return new Vector2(-dy, dx).normalise();
    }

    public boolean intersect(Line other)
    {
        return PointContainer.twoLinesIntersect(this.getStart(),this.getEnd(),other.getStart(),other.getEnd()) != null;
    }

    @Override
    public String toString() {
        return "Line{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
