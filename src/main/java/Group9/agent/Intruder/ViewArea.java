package Group9.agent.Intruder;

import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.ArrayList;

public class ViewArea {
    private Distance range;
    private Angle alpha;
    private Direction direction;
    private Coordinate origin;
    private Coordinate left;
    private Coordinate right;
    public ViewArea(Distance range, Angle alpha, Direction direction, double originX, double originY)
    {
        this.range = range;
        this.alpha = alpha;
        this.direction = direction;
        this.origin = new Coordinate(originX, originY);
        constructArea();
    }
    private void constructArea()
    {
        double leftX = Math.cos(direction.getRadians() - 0.5 * alpha.getRadians()) * range.getValue();
        double leftY = Math.sin(direction.getRadians() - 0.5 * alpha.getRadians()) * range.getValue();
        this.left = new Coordinate(leftX, leftY);
        double rightX = Math.cos(direction.getRadians() + 0.5 * alpha.getRadians()) * range.getValue();
        double rightY = Math.sin(direction.getRadians() + 0.5 * alpha.getRadians()) * range.getValue();
        this.right = new Coordinate(rightX, rightY);

    }
    public double partContained(Cell c)
    {
        ArrayList<Coordinate> pointsContained = pointsContained(c);
        if(pointsContained.size() == 4)
        {
            return 1.0;
        }
        else if (pointsContained.size() == 0)
        {
            return 0.0;
        }
        else if (pointsContained.size() == 1)
        {
            Line[] lines = {new Line(origin, left), new Line(left, right), new Line(right, origin)};
            return solveSingle(pointsContained.get(0), lines, c.getPoints());
        }
        else if (pointsContained.size() == 2)
        {
            Line[] lines = {new Line(origin, left), new Line(left, right), new Line(right, origin)};
            return solveDouble(pointsContained, lines, c.getPoints());
        }
        else if (pointsContained.size() == 3)
        {
            Line[] lines = {new Line(origin, left), new Line(left, right), new Line(right, origin)};
            return solveTriple(pointsContained, lines);
        }
        else
        {
            return 0.0;
        }
    }
    private double solveSingle(Coordinate point, Line[] lines, Coordinate[] allPoints)
    {
        ArrayList<Coordinate> remainingPoints = new ArrayList<>();
        Line lineHorizontal = new Line();
        Line lineVertical = new Line();
        for (Coordinate allPoint : allPoints) {
            if (!point.equals(allPoint)) {
                remainingPoints.add(allPoint);
            }
        }
        for (Coordinate remainingPoint : remainingPoints) {
            if (remainingPoint.getX() == point.getX()) {
                lineVertical = new Line(point, remainingPoint);
            }
            if (remainingPoint.getY() == point.getY()) {
                lineHorizontal = new Line(point, remainingPoint);
            }
        }
        Coordinate point1 = Line.getIntersectionPoint(lineHorizontal, lines);
        Coordinate point2 = Line.getIntersectionPoint(lineVertical, lines);
        double area = Math.abs((point1.getX() - point.getX()) * (point2.getY() - point.getY())) * 0.5;
        return area;
    }
    private double solveDouble(ArrayList<Coordinate> points, Line[] lines, Coordinate[] allPoints)
    {
        ArrayList<Coordinate> remainingPoints = new ArrayList<>();
        double area = 0;
        for (Coordinate allPoint : allPoints) {
            if (!points.get(0).equals(allPoint) && !points.get(1).equals(allPoint)) {
                remainingPoints.add(allPoint);
            }
        }
        if(points.get(0).getX() != remainingPoints.get(0).getX() && points.get(1).getX() != remainingPoints.get(0).getX())
        {
            Line line1 = new Line(points.get(0), new Coordinate(remainingPoints.get(0).getX(), points.get(0).getY()));
            Line line2 = new Line(points.get(1), new Coordinate(remainingPoints.get(0).getX(), points.get(1).getY()));
            Coordinate point1 = Line.getIntersectionPoint(line1, lines);
            Coordinate point2 = Line.getIntersectionPoint(line2, lines);
            area = Math.min(point1.getX(), point2.getX()) * 1 + 0.5 * Math.abs(point1.getX() - point2.getX());
        }
        else
        {
            Line line1 = new Line(points.get(0), new Coordinate(points.get(0).getX(), remainingPoints.get(0).getY()));
            Line line2 = new Line(points.get(1), new Coordinate(points.get(1).getX(), remainingPoints.get(0).getY()));
            Coordinate point1 = Line.getIntersectionPoint(line1, lines);
            Coordinate point2 = Line.getIntersectionPoint(line2, lines);
            area = Math.min(point1.getY(), point2.getY()) * 1 + 0.5 * Math.abs(point1.getY() - point2.getY());
        }
        return area;
    }
    private double solveTriple(ArrayList<Coordinate> points, Line[] lines)
    {
        double area = 0.5;//Three points means there is at least a triangle covered of area .5(The three points in the viewArea)
        if(points.get(0).getX() == points.get(1).getX() || points.get(0).getY() == points.get(1).getY())
        {
            if(points.get(0).getX() == points.get(2).getX())
            {
                Coordinate[] triangle1 = {points.get(1), points.get(2), Line.getIntersectionPoint(points.get(2), "h", (points.get(2).getX() - points.get(1).getX()), lines)};
                Coordinate[] triangle2 = {points.get(1), Line.getIntersectionPoint(points.get(2), "h", (points.get(2).getX() - points.get(1).getX()), lines), Line.getIntersectionPoint(points.get(1), "v", (points.get(1).getY()-points.get(2).getY()), lines)};
                area += getTriangleArea(triangle1);
                area += getTriangleArea(triangle2);
                return area;
            }
            else if (points.get(0).getY() == points.get(2).getY())
            {
                Coordinate[] triangle1 = {points.get(1), points.get(2), Line.getIntersectionPoint(points.get(2), "v", (points.get(2).getY() - points.get(1).getY()), lines)};
                Coordinate[] triangle2 = {points.get(1), Line.getIntersectionPoint(points.get(2), "v", (points.get(2).getY() - points.get(1).getY()), lines), Line.getIntersectionPoint(points.get(1), "h", (points.get(1).getX()-points.get(2).getX()), lines)};
                area += getTriangleArea(triangle1);
                area += getTriangleArea(triangle2);
                return area;
            }
            else if (points.get(1).getX() == points.get(2).getX())
            {
                Coordinate[] triangle1 = {points.get(0), points.get(2), Line.getIntersectionPoint(points.get(2), "v", (points.get(2).getY() - points.get(0).getY()), lines)};
                Coordinate[] triangle2 = {points.get(0), Line.getIntersectionPoint(points.get(2), "v", (points.get(2).getY() - points.get(0).getY()), lines), Line.getIntersectionPoint(points.get(0), "h", (points.get(0).getX()-points.get(2).getX()), lines)};
                area += getTriangleArea(triangle1);
                area += getTriangleArea(triangle2);
                return area;
            }
            else
            {
                Coordinate[] triangle1 = {points.get(0), points.get(2), Line.getIntersectionPoint(points.get(2), "h", (points.get(2).getX() - points.get(0).getX()), lines)};
                Coordinate[] triangle2 = {points.get(0), Line.getIntersectionPoint(points.get(2), "h", (points.get(2).getX() - points.get(0).getX()), lines), Line.getIntersectionPoint(points.get(0), "v", (points.get(0).getY()-points.get(2).getY()), lines)};
                area += getTriangleArea(triangle1);
                area += getTriangleArea(triangle2);
                return area;
            }
        }
        else
        {
            if (points.get(2).getX() == points.get(0).getX())
            {
                Coordinate[] triangle1 = {points.get(0), points.get(1), Line.getIntersectionPoint(points.get(0), "h", (points.get(0).getX() - points.get(1).getX()), lines)};
                Coordinate[] triangle2 = {points.get(0), Line.getIntersectionPoint(points.get(0), "h", (points.get(0).getX() - points.get(1).getX()), lines), Line.getIntersectionPoint(points.get(1), "v", (points.get(1).getY()-points.get(0).getY()), lines)};
                area += getTriangleArea(triangle1);
                area += getTriangleArea(triangle2);
                return area;
            }
            else
            {
                Coordinate[] triangle1 = {points.get(0), points.get(1), Line.getIntersectionPoint(points.get(0), "v", (points.get(0).getY() - points.get(1).getY()), lines)};
                Coordinate[] triangle2 = {points.get(0), Line.getIntersectionPoint(points.get(0), "v", (points.get(0).getY() - points.get(1).getY()), lines), Line.getIntersectionPoint(points.get(1), "h", (points.get(1).getX()-points.get(0).getX()), lines)};
                area += getTriangleArea(triangle1);
                area += getTriangleArea(triangle2);
                return area;
            }
        }
    }
    private double getTriangleArea(Coordinate[] points)
    {
        Line base = new Line(points[0], points[1]);
        Line height = Line.getOrthogonal(base, points[2]);
        return base.getLength() * height.getLength() * .5;
    }
    public ArrayList<Coordinate> pointsContained(Cell c)
    {
        // The four corner points of the cell
        Coordinate[] points = c.getPoints();
        ArrayList<Coordinate> containedPoints = new ArrayList<>();
        Line[] lines = {new Line(origin, left), new Line(left, right), new Line(right, origin)};
        for(int i=0; i<points.length; i++)
        {
            containedPoints.add(points[i]);
            if(!checkHorizontal(lines, points[i].getX(), points[i].getY()) || checkVertical(lines, points[i].getX(), points[i].getY()))
            {
                containedPoints.remove(containedPoints.size()-1);
            }
        }
        return containedPoints;
    }
    private boolean checkHorizontal(Line[] lines, double x, double y)
    {
        double highest = Double.NEGATIVE_INFINITY;
        double lowest = Double.POSITIVE_INFINITY;
        for(int i=0; i<lines.length; i++)
        {
            if(lines[i].getX(y) != 0.5)
            {
                if(highest < lines[i].getX(y))
                {
                    highest = lines[i].getX(y);
                }
                if(lowest > lines[i].getX(y))
                {
                    lowest = lines[i].getX(y);
                }
            }
        }
        if(x > lowest && x < highest)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean checkVertical(Line[] lines, double x, double y)
    {
        double highest = Double.NEGATIVE_INFINITY;
        double lowest = Double.POSITIVE_INFINITY;
        for(int i=0; i<lines.length; i++)
        {
            if(lines[i].getY(x) != 0.5)
            {
                if(highest < lines[i].getY(x))
                {
                    highest = lines[i].getY(x);
                }
                if(lowest > lines[i].getY(x))
                {
                    lowest = lines[i].getY(x);
                }
            }
        }
        if(y > lowest && y < highest)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
