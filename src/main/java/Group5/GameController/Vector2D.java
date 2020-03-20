package Group5.GameController;

import Interop.Geometry.Point;

public class Vector2D {

    private double x, y;

    Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    Vector2D(Point p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public Vector2D(Vector2D other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public Vector2D add(Vector2D other) {
        double newX = x +other.getX();
        double newY = y + other.getY();
        return new Vector2D(newX,newY);
    }

    public Vector2D sub(Vector2D other) {
        double newX = x - other.getX();
        double newY = y - other.getY();
        return new Vector2D(newX,newY);
    }

    /**
     * @param factor to what factor we multiply the vector
     * @return the multiplied vector.
     */
    public Vector2D mul(double factor) {
        double newX = x*factor;
        double newY = y*factor;
        return (new Vector2D(newX,newY));
    }

    /**
     * @param factor, the factor we divide in.
     * @return
     */
    public Vector2D div(double factor) {
        double newX = x/factor;
        double newY = y/factor;
        return (new Vector2D(newX,newY));
    }

    /**
     * Returns a new vector which is orthogonal to the current vector
     */
    public Vector2D orthogonal() {
        return new Vector2D(y, -x);
    }


    /**
     * creates the absolute value vector
     * @return
     */
    public Vector2D absVector(){
        Vector2D absVector = new Vector2D(this);
        absVector.x = Math.abs(absVector.x);
        absVector.y = Math.abs(absVector.y);
        return absVector;
    }

    /**
     * @return normalize the vector, multiplying by 1/its length.
     */
    public Vector2D normalize() {
        return mul(1.0/length());
    }

    public double lengthSquared() {
        double xx = 0;
        double yy = 0;
        if (this.x != 0 ) {
            xx = this.x*this.x;
        }
        if (this.y != 0 ) {
            yy = this.y*this.y;
        }
        return xx + yy;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    private double distanceSquared(Vector2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx*dx+dy*dy;
    }
    public double distance(Vector2D other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public String toString() {
        return String.format("x = %f, y = %f", x, y);
    }

    public Point toPoint(){
        Point p = new Point(x,y);
        return p;
    }


    /**
     * rotates a vector
     * @param angle RADIANS
     * @return
     */
    public Vector2D rotate(double angle){
        return new Vector2D(x*Math.cos(angle)-y*Math.sin(angle),y*Math.cos(angle)+x*Math.sin(angle));
    }

}
