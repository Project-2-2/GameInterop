package Group9.agent.Intruder;

public class Line {
    private Coordinate start;
    private Coordinate end;
    private double a;
    private double b;
    public Line(Coordinate start, Coordinate end)
    {
        this.start = start;
        this.end = end;
        this.a = (end.getY() - start.getY()) / (end.getX() - start.getX());
        this.b = start.getY() - (a * start.getX());
    }
    public Line()
    {

    }
    public double getY(double x)
    {
        double y;
        y = a*x + b;
        if((y > start.getY() && y > end.getY()) || (y < start.getY() && y < end.getY()))
        {
            return 0.5;
        }
        return y;
    }
    public double getX(double y)
    {
        double x;
        x = (y - b) / a;
        if((x > start.getX() && x > end.getX()) || (x < start.getX() && x < start.getX()))
        {
            return 0.5;
        }
        return x;
    }
    public double getA()
    {
        return a;
    }

    public double getB() {
        return b;
    }

    public boolean crossesHorizontal(double y)
    {
        if((y > start.getY() && y > end.getY()) || (y < start.getY() && y < end.getY()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean crossesVertical(double x)
    {
        if((x > start.getX() && x > end.getX()) || (x < start.getX() && x < start.getX()))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public static Coordinate getIntersectionPoint(Line line, Line[] lines)
    {
        Coordinate intersectionPoint = new Coordinate();
        for(int i=0; i<lines.length; i++)
        {
            double x = (line.getB() - lines[i].getB()) / (lines[i].getA() - line.getA());
            if(line.crossesVertical(x))
            {
                intersectionPoint = new Coordinate(x, line.getY(x));
            }
        }
        return intersectionPoint;
    }
    public static Coordinate getIntersectionPoint(Coordinate point, String direction, double upOrDown, Line[] lines)
    {
        Coordinate intersectionPoint = new Coordinate();
        double otherCo = 0;
        for(int i=0; i<lines.length; i++)
        {

        }
        switch (direction) {
            case "h": {
                for (Line line : lines) {
                    if ((line.getX(point.getY()) * upOrDown) > (point.getX() * upOrDown) && line.getX(point.getY()) != 0.5) {
                        return new Coordinate(line.getX(point.getY()), point.getY());
                    }
                }
                break;
            }
            case "v": {
                for(Line line : lines) {
                    if((line.getY(point.getX())*upOrDown) > (point.getY() * upOrDown) && line.getY(point.getX()) != 0.5)
                    {
                        return new Coordinate(point.getX(), line.getY(point.getX()));
                    }
                }
                break;
            }
        }
        return null;
    }
    public double getOrthogonal()
    {
        return -1/a;
    }
    public static Line getOrthogonal(Line base, Coordinate corner)
    {
        double a = base.getOrthogonal();
        double b = corner.getY() - a * corner.getX();
        double x = (base.getB() - b) / (a - base.getA());
        double y = a * x + b;
        return new Line(corner, new Coordinate(x,y));
    }
    public double getLength()
    {
        return Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2));
    }

}
