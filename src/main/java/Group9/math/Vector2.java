package Group9.math;

import Interop.Geometry.Angle;
import Interop.Geometry.Vector;
import Interop.Utils.Utils;

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
        return new Vector2(this.x / this.length, this.y / this.length);
    }


    public double length()
    {
        return this.length;
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
     * returns (new) vector rotated anticlockwise by 'angle' radians
     * See: https://matthew-brett.github.io/teaching/rotation_2d.html
     *      https://en.wikipedia.org/wiki/Rotation_matrix
     */
    public Vector2 rotated(Angle angle) {
        double new_x = Math.cos(angle.getRadians()) * x  -  Math.sin(angle.getRadians()) * y;
        double new_y = Math.sin(angle.getRadians()) * x  +  Math.cos(angle.getRadians()) * y;

        return new Vector2(new_x, new_y);
    }

    public double distance(Vector2 other)
    {
        return Math.sqrt(Math.pow(this.x - other.getX(), 2) + Math.pow(this.y - other.getY(), 2));
    }

    public double getClockDirection() {
        return Utils.clockAngle(this.x, this. y);
    }

    public Vector toVexing()
    {
        return new Vector(this.x, this.y);
    }

    public static Vector2 from(Vector point)
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
    public Vector2 clone() {
        return new Vector2(this.x, this.y);
    }
}
