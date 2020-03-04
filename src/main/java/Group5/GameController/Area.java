package Group5.GameController;

import Interop.Geometry.Point;

public class Area {
    protected int leftBoundary;
    protected int rightBoundary;
    protected int topBoundary;
    protected int bottomBoundary;

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

    public boolean isHit(Point p){
        return (p.getY()>bottomBoundary)&(p.getY()<topBoundary)&(p.getX()>leftBoundary)&(p.getX()<rightBoundary);
    }

    public boolean isHit(Area other){
        if(other.rightBoundary>leftBoundary&&other.bottomBoundary<bottomBoundary&&other.bottomBoundary>topBoundary){
            return true;
        }
        if (other.leftBoundary<rightBoundary&&other.bottomBoundary<bottomBoundary&&other.bottomBoundary>topBoundary){
            return true;
        }
        return false;

    }
    /*
        Check whether something with a radius is in the target area

    */
    public boolean isHit(double x,double y,double radius){

        return (y-radius>bottomBoundary)&(y+radius<topBoundary)&(x-radius>leftBoundary)&(x+radius<rightBoundary);
    }

    public Vector2D[] getAreaVectors(){
        Vector2D[] vectors = new Vector2D[4];
        Vector2D leftBottom = new Vector2D(leftBoundary,bottomBoundary);
        Vector2D rightBottom = new Vector2D(rightBoundary,bottomBoundary);
        Vector2D leftTop = new Vector2D(leftBoundary,topBoundary);
        Vector2D rightTop = new Vector2D(rightBoundary,topBoundary);
        vectors[0] = leftBottom;
        vectors[1] = rightBottom;
        vectors[2] = leftTop;
        vectors[3] = rightTop;
        return vectors;
    }

}