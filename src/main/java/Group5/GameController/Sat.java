package Group5.GameController;

import Interop.Geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Sat {

    /**
     * class uses seperated axis theorem for colission detection
     * this way every convex shape with an angle can be checked for colission
     * define the shape with a set of vectors and this class will calculate if there is any colission
     * So for a rectangle you need 4 vectors containing the position of each corner from the rectangle
     * @param args
     */

    public static void main(String[] args) {
        //to convex shapes that will be checked for collision
        Point[] tank = {new Point(0, 0), new Point(200, 0), new Point(200, 50),
                new Point(0, 50)};
        Point[] shell = {new Point(10, 10), new Point(400, 800), new Point(800, 800),
                new Point(800, 400)};
        Point[] test = {new Point(0,45),new Point(100,100)};

        ArrayList<Point> poep=new ArrayList<>(List.of(new Point(10,10),new Point(400,800),new Point(800,800),new Point(800,400)));


        System.out.println(Sat.hasCollided(poep,circleToPolygon(poep,new Point(1,1),15)));


    }

    /**
     * creates a polygon from a circle
     * this polygon has vectors parallel to the vectors of the other polygon that has to be checked for colission
     * @param compare the polygon that the circle will be checked if it has colission with
     * @param p the center of the circle
     * @param radius the radius of the circle
     * @return
     */
    public static ArrayList<Point> circleToPolygon(ArrayList<Point> compare, Point p, double radius){
        ArrayList<Point> edges = polyToEdges(compare);
        ArrayList<Point> circlePolygon = new ArrayList<>();
        for (int i = 0; i<edges.size();i++){
           // System.out.println(edges.get(i).toPoint().getClockDirection().getRadians());
           // System.out.println(Math.atan2(edges.get(i).getX(),edges.get(i).getY()));
//            System.out.println(Math.atan2(compare[i].getX()-compare[i+1].getX(),compare[i].getY()-compare[i+1].getY()));
            double x = p.getX()+radius*Math.cos(edges.get(i).getClockDirection().getRadians());
            double y = p.getY()+radius*Math.sin(edges.get(i).getClockDirection().getRadians());
            System.out.println("x: " + x + " y: "+ y);
            circlePolygon.add(new Point(x,y));
        }
        return circlePolygon;

    }

    public static Boolean hasCollided(ArrayList<Point> poly1, ArrayList<Point> poly2) {
            return runSAT(poly1, poly2);
    }

    private static Boolean runSAT(ArrayList<Point> poly1, ArrayList<Point> poly2) {
        // Implements the actual SAT algorithm
        ArrayList<Point> edges = polyToEdges(poly1);
        edges.addAll(polyToEdges(poly2));
        Point[] axes = new Point[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            axes[i] = orthogonal(edges.get(i));
        }

        for (Point axis : axes) {
            if (!overlap(project(poly1, axis), project(poly2, axis))) {
                // The polys don't overlap on this axis so they can't be touching
                return false;
            }
        }

        // The polys overlap on all axes so they must be touching
        return true;
    }

    /**
     * Returns a vector going from point1 to point2
     */
    private static Point edgeVector(Point point1, Point point2) {
        return new Point(point2.getX() - point1.getX(), point2.getY() - point1.getY());
    }

    /**
     * Returns an array of the edges of the poly as vectors
     */
    private static ArrayList<Point> polyToEdges(ArrayList<Point> poly) {
        ArrayList<Point> vectors = new ArrayList<>(poly.size());
        for (int i = 0; i < poly.size(); i++) {
            vectors.add(edgeVector(poly.get(i), poly.get((i + 1) % poly.size())));
        }
        return vectors;
    }

    /**
     * Returns the dot (or scalar) product of the two vectors
     */
    private static double dotProduct(Point vector1, Point vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
    }

    /**
     * Returns a vector showing how much of the poly lies along the axis
     */
    private static Point project(ArrayList<Point> poly, Point axis) {
        List<Double> dots = new ArrayList<>();
        for (Point vector : poly) {
            dots.add(dotProduct(vector, axis));
        }
        return new Point(Collections.min(dots), Collections.max(dots));
    }

    /**
     * Returns a boolean indicating if the two projections overlap
     */
    private static boolean overlap(Point projection1, Point projection2) {
        return projection1.getX() <= projection2.getY() &&
                projection2.getX() <= projection1.getY();
    }

    /**
     * Returns a new vector which is orthogonal to the current vector
     */
    public static Point orthogonal(Point p) {
        return new Point(p.getY(), -p.getX());
    }

    public static Point add(Point original,Point other) {
        double newX = original.getX() +other.getX();
        double newY = original.getY() + other.getY();
        return new Point(newX,newY);
    }

    /**
     * @param factor to what factor we multiply the vector
     * @return the multiplied vector.
     */
    public static Point mul(Point original,double factor) {
        double newX = original.getX()*factor;
        double newY = original.getY()*factor;
        return (new Point(newX,newY));
    }

    public static  double lengthSquared(Point original) {
        double xx = 0;
        double yy = 0;
        if (original.getX() != 0 ) {
            xx = original.getX()*original.getX();
        }
        if (original.getY() != 0 ) {
            yy = original.getY()*original.getY();
        }
        return xx + yy;
    }

    public static double length(Point original) {
        return Math.sqrt(lengthSquared(original));
    }

    private static double distanceSquared(Point original,Point other) {
        double dx = original.getX() - other.getX();
        double dy = original.getY()-original.getY();
        return dx*dx+dy*dy;
    }
    public static double distance(Point original,Point other) {
        return Math.sqrt(distanceSquared(original,other));
    }


    /**
     * rotates a vector
     * @param angle RADIANS
     * @return
     */
    public static Point rotate(Point original,double angle){
        return new Point(original.getX()*Math.cos(angle)-original.getY()*Math.sin(angle),original.getY()*Math.cos(angle)+original.getX()*Math.sin(angle));
    }

    /**
     * @return normalize the vector, multiplying by 1/its length.
     */
    public static Point normalize(Point original) {
        return mul(original,1.0/length(original));
    }

    /**
     * creates the absolute value vector
     * @return
     */
    public static Point absVector(Point original){
        double absX=Math.abs(original.getX());
        double absY=Math.abs(original.getY());
        return new Point(absX,absY);
    }



}