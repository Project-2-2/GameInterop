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
}
