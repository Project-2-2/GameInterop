package Group4.OurInterop;

import java.awt.geom.Point2D;

public class Area extends java.awt.geom.Area {
    protected int leftBoundary;
    protected int rightBoundary;
    protected int topBoundary;
    protected int bottomBoundary;
    protected Point2D center;

    public Area(){
        leftBoundary=0;
        rightBoundary=1;
        topBoundary=0;
        bottomBoundary=1;
    }

    public Area(int x1, int y1, int x2, int y2){
        leftBoundary=Math.min(x1,x2);
        rightBoundary=Math.max(x1,x2);
        topBoundary=Math.max(y1,y2);
        bottomBoundary=Math.min(y1,y2);
    }

    /*
        Check whether a point is in the target area
    */
    public boolean isHit(double x,double y){
        return (y>bottomBoundary)&(y<topBoundary)&(x>leftBoundary)&(x<rightBoundary);
    }

    /**
     *
     * @param x : x location of circle center
     * @param y : y location of circle center
     * @param radius
     * @return  returns if the circle intersects with this area
     */
    public boolean isHit(double x,double y,double radius){

        double circCentreX = x;
        double circCentreY = y;

        double circDistX = Math.abs(circCentreX - this.getCenter().getX());
        double circDistY = Math.abs(circCentreY - this.getCenter().getY());

        if (circDistX > (this.getWidth()/2 + radius)) { return false; }
        if (circDistY > (this.getHeight()/2 + radius)) { return false; }

        if (circDistX <= (this.getWidth()/2 )) { return true; }
        if (circDistY <= (this.getHeight()/2 )) { return true; }

        double cornerDistance_sq = ((circDistX - this.getWidth()/2)*(circDistX - this.getWidth()/2)) + ((circDistY - this.getHeight()/2)*(circDistY - this.getHeight()/2));
        return (cornerDistance_sq <= (radius*radius));
    }

    public Point2D getCenter(){
        this.center.setLocation((this.leftBoundary + (this.getWidth()/2)), (this.bottomBoundary + (this.getHeight()/2)));
        return this.center;
    }

    public double getWidth(){
        return ((this.rightBoundary-this.leftBoundary)/2);
    }

    public double getHeight(){
        return ((this.topBoundary-this.bottomBoundary)/2);
    }

    public int getLeftBoundary() {
        return leftBoundary;
    }

    public int getRightBoundary() {
        return rightBoundary;
    }

    public int getTopBoundary() {
        return topBoundary;
    }

    public int getBottomBoundary() {
        return bottomBoundary;
    }

}
