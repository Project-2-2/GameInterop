package Group9.math;

import Group9.Game;
import Interop.Geometry.Point;
import Interop.Utils.Utils;

import java.util.Objects;

public class Vector2 {

    private final double x, y;
    private final double length;

    public Vector2(double x, double y)
    {
        this.x = x;
        this.y = y;
        this.length = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public Vector2 normalise()
    {
        if(this.length == 0)
        {
            return new Vector2(0, 0);
        }
        return new Vector2(this.x / this.length, this.y / this.length);
    }

    public double length()
    {
        return this.length;
    }

    public Vector2 mul(Vector2 other)
    {
        return mul(other.getX(), other.getY());
    }

    public Vector2 mul(double x, double y)
    {
        return new Vector2(this.x * x, this.y * y);
    }

    public Vector2 mul(double n){
        return new Vector2(this.x * n, this.y * n);
    }

    public Vector2 add(Vector2 add)
    {
        return this.add(add.getX(), add.getY());
    }

    public Vector2 add(double x, double y)
    {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 sub(Vector2 sub)
    {
        return this.add(-sub.getX(), -sub.getY());
    }

    public Vector2 sub(double x, double y)
    {
        return this.add(-x, -y);
    }

    public Vector2 flip() {
        return this.mul(-1);
    }

    /**
     * See: https://matthew-brett.github.io/teaching/rotation_2d.html
     *      https://en.wikipedia.org/wiki/Rotation_matrix
     * @param radians
     * @return (new) vector rotated anticlockwise by 'angle' radians
     */
    public Vector2 rotated(double radians) {
        return new Vector2(
                Math.cos(radians) * x  -  Math.sin(radians) * y,
                Math.sin(radians) * x  +  Math.cos(radians) * y
        );
    }

    public double dot(Vector2 other)
    {
        return (this.x * other.getX()) + (this.y * other.getY());
    }

    public double angle(Vector2 other)
    {
        double angle = Math.atan2(other.getY(), other.getX()) - Math.atan2(this.getY(), this.getX());
        if(angle < 0)
        {
            angle += 2 * Math.PI;
        }
        angle %= (2 * Math.PI);
        return angle;
    }

    public double distance(Vector2 other)
    {
        return Math.sqrt(Math.pow(this.x - other.getX(), 2) + Math.pow(this.y - other.getY(), 2));
    }

    public double getAngle()
    {
        double angle = Math.asin(y);
        if(x < 0)
        {
            angle = Math.PI - angle;
        }
        return angle;

    }

    public double getClockDirection() {
        return Utils.clockAngle(this.x, this. y);
    }

    public Point toVexing()
    {
        return new Point(this.x, this.y);
    }

    public static Vector2 from(Point point)
    {
        return new Vector2(point.getX(), point.getY());
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                ", length=" + length +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Double.compare(vector2.x, x) == 0 &&
                Double.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Vector2 clone() {
        return new Vector2(this.x, this.y);
    }


    public static class Random extends Vector2 {
        public Random() {
            super(Game._RANDOM.nextDouble(), Game._RANDOM.nextDouble());
        }
    }

    public static class Origin extends Vector2 {
        public Origin() {
            super(0, 0);
        }
    }

}
