package Group9.agent.Intruder;

public class Coordinate {
    private double x;
    private double y;
    public Coordinate(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
    public Coordinate()
    {

    }
    public double getX()
    {
        return x;
    }

    public double getY() {
        return y;
    }
    public boolean equals(Coordinate c)
    {
        if(x == c.getX() && y == c.getY())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void decrementX()
    {
        x--;
    }
    public void decrementY()
    {
        y--;
    }
    public void incrementY()
    {
        y++;
    }
    public void incrementX()
    {
        x++;
    }
    public void add(double x, double y)
    {
        this.x += x;
        this.y += y;
    }
    @Override
    public String toString()
    {
        return "x: " + x + " y: " + y;
    }
}
