package Group8.Controller.Utils;

public class Vector2d {
    private double x, y;
    public Vector2d(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double getLength(){
        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2d mul(double dx,double dy){
        return new Vector2d(this.x*dx,this.y*dy);
    }

    //Rotates a vector counter clock-wise by specified degrees
    public Vector2d rotate(double degrees){
        return new Vector2d(x*Math.cos(degrees)-y*Math.sin(degrees),x*Math.sin(degrees)+y*Math.cos(degrees));
    }
}
