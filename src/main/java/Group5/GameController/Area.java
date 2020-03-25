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

    protected double x1;
    protected double x2;
    protected double x3;
    protected double x4;
    protected double y1;
    protected double y2;
    protected double y3;
    protected double y4;

    protected ObjectPerceptType type;

    protected static ArrayList<Area> areas = new ArrayList<>(); //stores all the objects/area

    public Area(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4){
        leftBoundary=Math.min(Math.min(x1,x2),Math.min(x3,x4));
        rightBoundary=Math.max(Math.max(x1,x2),Math.max(x3,x4));
        topBoundary=Math.min(Math.max(y1,y2),Math.max(y3,y4));
        bottomBoundary=Math.max(Math.max(y1,y2),Math.max(y3,y4));
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y1 = y1;
        this.y2 = y2;
        this.y3 = y3;
        this.y4 = y4;
        this.type = ObjectPerceptType.Wall;

        areas.add(this);
    }

    public Area(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, boolean nothing){
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

    public Area(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, ObjectPerceptType objectType){
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
        this.type = objectType;

        areas.add(this);
    }

    /**
     * For area recognition
     */
    public Area(double x1, double y1, double x2, double y2, ObjectPerceptType objectType) {
        setPositions(x1,y1,x2,y2);
        this.type = objectType;
    }

    /**
     * For Area recognition
     */
    public void setPositions(double x1, double y1, double x2, double y2) {
        if (x1 == x2) {
            if (y1 < y2) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;

            }else {
                this.x1 = x2;
                this.y1 = y2;
                this.x2 = x1;
                this.y2 = y1;
            }

        }else if (y1 == y2) {
            if (x1 < x2) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;

            }else {
                this.x1 = x2;
                this.y1 = y2;
                this.x2 = x1;
                this.y2 = y1;
            }


        }else if (x1 < x2 && y1 < y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x3 = x2;
            this.y3 = y2;

        }else if (x1 > x2 && y1 > y2) {
            this.x1 = x2;
            this.y1 = y2;
            this.x3 = x1;
            this.y3 = y1;
        }
    }

    public void addPoint(Point p) {
        double x = p.getX();
        double y = p.getY();

        if (x < this.x1 && y < this.y1) {
            this.x1 = x;
            this.y1 = y;

        }else if (x > this.x2 && y < this.y2) {
            this.x2 = x;
            this.y2 = y;

        }else if (x > this.x3 && y > this.y3) {
            this.x3 = x;
            this.y3 = y;

        }else if (x < this.x4 && y > this.y4) {
            this.x4 = x;
            this.y4 = y;
        }
    }


    public ObjectPerceptType getObjectsPerceptType() {
        return this.type;
    }

    /**
     * @return a list of Points that represents the corners of an area
     */
    public ArrayList<ArrayList<Point>> getPositions() {
        ArrayList<ArrayList<Point>> positions = new ArrayList<>();
        positions.add(new ArrayList<>());
        positions.add(new ArrayList<>());
        positions.add(new ArrayList<>());
        positions.add(new ArrayList<>());

        positions.get(0).add(new Point(this.x1,this.y1));
        positions.get(0).add(new Point(this.x2,this.y2));

        positions.get(1).add(new Point(this.x2,this.y2));
        positions.get(1).add(new Point(this.x3,this.y3));

        positions.get(2).add(new Point(this.x3,this.y3));
        positions.get(2).add(new Point(this.x4,this.y4));

        positions.get(3).add(new Point(this.x4,this.y4));
        positions.get(3).add(new Point(this.x1,this.y1));
        return positions;
    }

    /*
        Check whether a point is in the target area
    */
    public boolean isHit(double x,double y){
        //Point[] movement = new Point[]{new Point(x,y)};
        ArrayList<Point> movement = new ArrayList<>();
        movement.add(new Point(x,y));
        return Sat.hasCollided(movement,getAreaVectors());
    }



    public boolean isHit(Point p){
        //Point[] movement = new Point[]{new Point(p)};
        ArrayList<Point> movement = new ArrayList<>();
        movement.add(p);
        return Sat.hasCollided(movement,getAreaVectors());
    }

    public boolean isHit(Area other){
        return Sat.hasCollided(this.getAreaVectors(),other.getAreaVectors());
    }

    /*
        Check whether something with a radius is in the target area

    */
    public boolean isHit(double x,double y,double radius){
        ArrayList<Point> circlePolygon = Sat.circleToPolygon(this.getAreaVectors(),new Point(x,y),radius);
        return Sat.hasCollided(this.getAreaVectors(),circlePolygon);
        //return (y-radius>bottomBoundary)&(y+radius<topBoundary)&(x-radius>leftBoundary)&(x+radius<rightBoundary);
    }
    /*
        Check whether something with a radius is in the target area

    */
    public boolean isHit(Point center,double radius){
        ArrayList<Point> circlePolygon = Sat.circleToPolygon(this.getAreaVectors(),center,radius);
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
        if (!Sat.hasCollided(new ArrayList<Point>(List.of(point1,point2)),new ArrayList<Point>(List.of(point3,point4)))){
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
    /*
    public static Point getIntersectionVector(Point point1, Point point2, Point point3, Point point4){

        //if there is no colission
        if (!Sat.hasCollided(new Point[]{new Point(point1),new Point(point2)}, new Point[]{new Point(point3),new Point(point4)})){
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

     */



    /**
     * CALL THIS METHOD FOR VISSION
     * You need to have the two ends of the vision vector
     * @param startVector
     * @param endVector
     * @return
     */
    public boolean isHit(Point startVector, Point endVector){
        ArrayList<Point> rayVector = new ArrayList<>(List.of(startVector,endVector));
        return Sat.hasCollided(this.getAreaVectors(),rayVector);
    }

    public ArrayList<Point> getAreaVectors(){
        Point leftBottom = new Point(x1,y1);
        Point rightBottom = new Point(x2,y2);
        Point leftTop = new Point(x3,y3);
        Point rightTop = new Point(x4,y4);

        ArrayList<Point> vectors = new ArrayList<>();
        vectors.add(leftBottom);
        vectors.add(rightBottom);
        vectors.add(leftTop);
        vectors.add(rightTop);
        return vectors;
    }

    public static ArrayList<Area> getAreas() {
        return areas;
    }

    public Point getCentre(){
        return new Point((x1+x2+x3+x4)/4,(y1+y2+y3+y4)/4);
    }
    public Point getClosest(Point point){
        double x = Math.min(Math.min(Math.min(x1-point.getX(),x2-point.getX()),x3-point.getX()),x4-point.getX());
        double y = Math.min(Math.min(Math.min(y1-point.getX(),y2-point.getX()),y3-point.getX()),y4-point.getX());
        return new Point(x+point.getX(), y+point.getY());
    }

}