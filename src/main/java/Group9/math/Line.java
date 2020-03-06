package Group9.math;

import Interop.Geometry.Vector;

import java.awt.geom.Line2D;

public class Line {

    private final Vector2 start;
    private final Vector2 end;

    private final boolean horizontal;
    private final boolean vertical;

    public Line(Vector2 start, Vector2 end)
    {
        this.start = start;
        this.end = end;


        this.horizontal = start.getY() == end.getY();
        this.vertical = start.getX() == end.getX();
    }

    public Vector2 getStart()
    {
        return this.start;
    }

    public Vector2 getEnd()
    {
        return this.end;
    }

    public boolean isHorizontal()
    {
        return this.horizontal;
    }

    public boolean isVertical() {
        return vertical;
    }

    public boolean intersect(Line line)
    {
        double a_min_x = Math.min(start.getX(), end.getX());
        double a_max_x = Math.max(start.getX(), end.getX());
        double a_min_y = Math.min(start.getY(), end.getY());
        double a_max_y = Math.max(start.getY(), end.getY());

        double b_min_y = Math.min(line.getStart().getY(), line.getEnd().getY());
        double b_max_y = Math.max(line.getStart().getY(), line.getEnd().getY());
        double b_min_x = Math.min(line.getStart().getX(), line.getEnd().getX());
        double b_max_x = Math.max(line.getStart().getX(), line.getEnd().getX());

        if(this.isHorizontal() && line.isHorizontal())
        {
            if(this.start.getY() == line.getStart().getY())
            {


                return ((a_min_x < b_min_x && a_max_x > b_min_x) || (a_min_x < b_max_x && a_max_x > b_max_x));
            }
            else
            {
                return false;
            }
        }
        else if(this.isVertical() & line.isVertical())
        {
            if(this.start.getX() == line.getStart().getX())
            {
                return ((a_min_y < b_min_y && a_max_y > b_min_y) || (a_min_y < b_max_y && a_max_y > b_max_y));
            }
            else
            {
                return false;
            }
        }
        else if(this.isHorizontal() && line.isVertical())
        {
            return ((b_min_y < a_min_y && b_max_y > a_min_y) && (a_min_x < b_max_x && a_max_x > b_max_x));
        }
        else if(this.isVertical() && line.isHorizontal())
        {
            return ((a_min_y < b_min_y && a_max_y > b_min_y) && (b_min_x < a_max_x && b_max_x > a_max_x));
        }

        if(true)
        {
            throw new IllegalStateException();
        }

        Vector2 a_start = (start.getX() < end.getX()) ? start : end;
        Vector2 a_end = (start.getX() > end.getX()) ? end : start;
        double a_slope = (a_end.getY() - a_start.getY()) / (a_end.getX() - a_start.getX());
        double a_intercept = a_start.getY() - a_slope * a_start.getY();



        return Line2D.linesIntersect(
                this.start.getX(), this.start.getY(),
                this.end.getX(), this.end.getY(),
                line.getStart().getX(), this.getStart().getY(),
                line.getEnd().getX(), this.getEnd().getY()
        );
    }

}
