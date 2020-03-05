package Group9.math;

import java.awt.geom.Line2D;

public class Line {

    private Vector2 start;
    private Vector2 end;

    public Line(Vector2 start, Vector2 end)
    {
        this.start = start;
        this.end = end;
    }

    public Vector2 getStart()
    {
        return this.start;
    }

    public Vector2 getEnd()
    {
        return this.end;
    }

    public boolean intersect(Line line)
    {
        return Line2D.linesIntersect(
                this.start.getX(), this.start.getY(),
                this.end.getX(), this.end.getY(),
                line.getStart().getX(), this.getStart().getY(),
                line.getEnd().getX(), this.getEnd().getY()
        );
    }

}
