package Group5.GameController;

import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import javafx.css.converter.DeriveColorConverter;

import java.util.*;

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

    protected ObjectPerceptType type;

    protected static ArrayList<Area> areas; //stores all the objects/area

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

        areas.add(this);
    }


    public ObjectPerceptType getObjectsPerceptType() {
        return this.type;
    }



    public ArrayList<Integer> getPosition() {
        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(this.x1);
        positions.add(this.y1);
        positions.add(this.x2);
        positions.add(this.y2);
        positions.add(this.x3);
        positions.add(this.y3);
        positions.add(this.x4);
        positions.add(this.y4);
        return positions;
    }

    /**
     * @return a list of Points that represents the sides of an area
     */
    public ArrayList<ArrayList<Vector2D>> getPositions() {
        ArrayList<ArrayList<Vector2D>> positions = new ArrayList<>();
        positions.get(0).add(new Vector2D(this.x1,this.y1));
        positions.get(0).add(new Vector2D(this.x2,this.y2));
        positions.get(1).add(new Vector2D(this.x2,this.y2));
        positions.get(1).add(new Vector2D(this.x3,this.y3));
        positions.get(2).add(new Vector2D(this.x3,this.y3));
        positions.get(2).add(new Vector2D(this.x4,this.y4));
        positions.get(3).add(new Vector2D(this.x4,this.y4));
        positions.get(3).add(new Vector2D(this.x1,this.y1));

        return positions;
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
     * Ionas call this method to get the intesection
     * @param point1 one endpoint of the first vector
     * @param point2 second endpoint of the first vector
     * @param point3 one endpoint of the second vector
     * @param point4 second endpoint of the second vector
     * @return
     */
    public static Point getIntersectionPoint(Point point1, Point point2, Point point3, Point point4){

        //if there is no colission
        if (!Sat.hasCollided(new Vector2D[]{new Vector2D(point1),new Vector2D(point2)}, new Vector2D[]{new Vector2D(point3),new Vector2D(point4)})){
            return new Point(0,0);
        }

        double tNom = ((point1.getX()-point3.getX())*(point3.getY()-point4.getY()))-((point1.getY()-point3.getY())*(point3.getX()-point4.getX()));
        double tDenom = ((point1.getX()-point2.getX())*(point3.getY()-point4.getY()))-((point1.getY()-point2.getY())*(point3.getX()-point4.getX()));

        double uNom = ((point1.getX()-point2.getX())*(point1.getY()-point3.getY()))-((point1.getY()-point2.getY())*(point1.getX()-point3.getX()));
        double uDenom= ((point1.getX()-point2.getX())*(point3.getY()-point4.getY()))-((point1.getY()-point2.getY())*(point3.getX()-point4.getX()));

        double t = tNom/tDenom;
        double u = uNom/uDenom;

        double xIntersect = point1.getX()+t*(point2.getX()-point1.getX());
        double yIntersect = point1.getY()+t*(point2.getY()-point1.getY());

        //System.out.println(xIntersect);
        //System.out.println(yIntersect);

        return new Point(xIntersect,yIntersect);
    }

    /**
     * Ionas call this method to get the intesection
     * @param point1 one endpoint of the first vector
     * @param point2 second endpoint of the first vector
     * @param point3 one endpoint of the second vector
     * @param point4 second endpoint of the second vector
     * @return
     */
    public static Point getIntersectionVector(Vector2D point1, Vector2D point2, Vector2D point3, Vector2D point4){

        //if there is no colission
        if (!Sat.hasCollided(new Vector2D[]{new Vector2D(point1),new Vector2D(point2)}, new Vector2D[]{new Vector2D(point3),new Vector2D(point4)})){
            return new Point(0,0);
        }

        double tNom = ((point1.getX()-point3.getX())*(point3.getY()-point4.getY()))-((point1.getY()-point3.getY())*(point3.getX()-point4.getX()));
        double tDenom = ((point1.getX()-point2.getX())*(point3.getY()-point4.getY()))-((point1.getY()-point2.getY())*(point3.getX()-point4.getX()));

        double uNom = ((point1.getX()-point2.getX())*(point1.getY()-point3.getY()))-((point1.getY()-point2.getY())*(point1.getX()-point3.getX()));
        double uDenom= ((point1.getX()-point2.getX())*(point3.getY()-point4.getY()))-((point1.getY()-point2.getY())*(point3.getX()-point4.getX()));

        double t = tNom/tDenom;
        double u = uNom/uDenom;

        double xIntersect = point1.getX()+t*(point2.getX()-point1.getX());
        double yIntersect = point1.getY()+t*(point2.getY()-point1.getY());

        //System.out.println(xIntersect);
        //System.out.println(yIntersect);

        return new Point(xIntersect,yIntersect);
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

    public static ArrayList<Area> getAreas() {
        return areas;
    }

}