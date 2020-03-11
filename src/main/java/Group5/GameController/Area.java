package Group5.GameController;

import Interop.Geometry.Point;

public class Area {
    protected int leftBoundary;
    protected int rightBoundary;
    protected int topBoundary;
    protected int bottomBoundary;

    protected int x1;
    protected int x2;
    protected int x3;
    protected int x4;
    protected int y1;
    protected int y2;
    protected int y3;
    protected int y4;

    public Area(){
        leftBoundary=0;
        rightBoundary=1;
        topBoundary=0;
        bottomBoundary=1;
    }

    public Area(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        leftBoundary=Math.min(Math.min(x1,x2),Math.min(x3,x4));
        rightBoundary=Math.max(Math.max(x1,x2),Math.max(x3,x4));
        topBoundary=Math.min(Math.max(y1,y2),Math.max(y3,y4));
        bottomBoundary=Math.max(Math.max(y1,y2),Math.max(y3,y4));
        this.x1=x1;
        this.x2=x2;
        this.x3=x3;
        this.x4=x4;
        this.y1=y1;
        this.y2=y2;
        this.y3=y3;
        this.y4=y4;

    }

    /*
        Check whether a point is in the target area
    */
    public boolean isHit(double x,double y){
        Vector2D[] movement = new Vector2D[]{new Vector2D(x,y)};
        return Sat.hasCollided(movement,getAreaVectors());
    }



    public boolean isHit(Point p){
        Vector2D[] movement = new Vector2D[]{new Vector2D(p)};
        return Sat.hasCollided(movement,getAreaVectors());
    }

    public boolean isHit(Area other){
        return Sat.hasCollided(this.getAreaVectors(),other.getAreaVectors());
    }

    /*
        Check whether something with a radius is in the target area

    */
    public boolean isHit(double x,double y,double radius){
        Vector2D[] circlePolygon = Sat.circleToPolygon(this.getAreaVectors(),new Point(x,y),radius);
        return Sat.hasCollided(this.getAreaVectors(),circlePolygon);
        //return (y-radius>bottomBoundary)&(y+radius<topBoundary)&(x-radius>leftBoundary)&(x+radius<rightBoundary);
    }
    /*
        Check whether something with a radius is in the target area

    */
    public boolean isHit(Point center,double radius){
        Vector2D[] circlePolygon = Sat.circleToPolygon(this.getAreaVectors(),center,radius);
        return Sat.hasCollided(this.getAreaVectors(),circlePolygon);
        //return (y-radius>bottomBoundary)&(y+radius<topBoundary)&(x-radius>leftBoundary)&(x+radius<rightBoundary);
    }


    /**
     * CALL THIS METHOD FOR VISSION
     * You need to have the two ends of the vision vector
     * @param startVector
     * @param endVector
     * @return
     */
    public boolean isHit(Point startVector, Point endVector){
        Vector2D[] rayVector = {new Vector2D(startVector),new Vector2D(endVector)};
        return Sat.hasCollided(this.getAreaVectors(),rayVector);
    }

    public Vector2D[] getAreaVectors(){
        Vector2D[] vectors = new Vector2D[4];
        Vector2D leftBottom = new Vector2D(x1,y1);
        Vector2D rightBottom = new Vector2D(x2,y2);
        Vector2D leftTop = new Vector2D(x3,y3);
        Vector2D rightTop = new Vector2D(x4,y4);
        vectors[0] = leftBottom;
        vectors[1] = rightBottom;
        vectors[2] = leftTop;
        vectors[3] = rightTop;
        return vectors;
    }

}