/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Group8.Controller.Utils;
/**
 *
 * @author joel
 */
public class Area {
    protected int leftBoundary;
    protected int rightBoundary;
    protected int topBoundary;
    protected int bottomBoundary;
    protected int[] corners;

    public Area(){
        leftBoundary=0;
        rightBoundary=1;
        topBoundary=0;
        bottomBoundary=1;
        corners = new int[]{leftBoundary,rightBoundary,topBoundary,bottomBoundary};
    }

    /**
     * Constructor
     * Params are for top left and bottom right corner
     */
    public Area(int x1, int y1, int x2, int y2){
        leftBoundary=Math.min(x1,x2);
        rightBoundary=Math.max(x1,x2);
        topBoundary=Math.min(y1,y2);
        bottomBoundary=Math.max(y1,y2);
        corners = new int[]{leftBoundary,rightBoundary,topBoundary,bottomBoundary};
    }

    public int getCorner(int i){return corners[i];}

    /*
        Check whether a point is in the target area
    */
    public boolean isHit(double x,double y){
        return (y>bottomBoundary)&(y<topBoundary)&(x>leftBoundary)&(x<rightBoundary);
    }

    /*
        Check whether something with a radius is in the target area
        STILL TO BE IMPLEMENTED
    */
    public boolean isHit(double x,double y,double radius){
        return false;
    }
}