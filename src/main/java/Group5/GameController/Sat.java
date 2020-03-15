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
        Vector2D[] tank = {new Vector2D(0, 0), new Vector2D(200, 0), new Vector2D(200, 50),
                new Vector2D(0, 50)};
        Vector2D[] shell = {new Vector2D(10, 10), new Vector2D(400, 800), new Vector2D(800, 800),
                new Vector2D(800, 400)};
        Vector2D[] test = {new Vector2D(0,45),new Vector2D(100,100)};


        System.out.println(Sat.hasCollided(shell,circleToPolygon(shell,new Point(1,1),15)));


    }

    /**
     * creates a polygon from a circle
     * this polygon has vectors parallel to the vectors of the other polygon that has to be checked for colission
     * @param compare the polygon that the circle will be checked if it has colission with
     * @param p the center of the circle
     * @param radius the radius of the circle
     * @return
     */
    public static Vector2D[] circleToPolygon(Vector2D[] compare, Point p, double radius){
        ArrayList<Vector2D> edges = polyToEdges(compare);
        Vector2D[] circlePolygon = new Vector2D[edges.size()];
        for (int i = 0; i<edges.size();i++){
           // System.out.println(edges.get(i).toPoint().getClockDirection().getRadians());
           // System.out.println(Math.atan2(edges.get(i).getX(),edges.get(i).getY()));
//            System.out.println(Math.atan2(compare[i].getX()-compare[i+1].getX(),compare[i].getY()-compare[i+1].getY()));
            double x = p.getX()+radius*Math.cos(edges.get(i).toPoint().getClockDirection().getRadians());
            double y = p.getY()+radius*Math.sin(edges.get(i).toPoint().getClockDirection().getRadians());
            System.out.println("x: " + x + " y: "+ y);
            circlePolygon[i] = new Vector2D(x,y);
        }
        return circlePolygon;

    }

    public static Boolean hasCollided(Vector2D[] poly1, Vector2D[] poly2) {
            return runSAT(poly1, poly2);
    }

    private static Boolean runSAT(Vector2D[] poly1, Vector2D[] poly2) {
        // Implements the actual SAT algorithm
        ArrayList<Vector2D> edges = polyToEdges(poly1);
        edges.addAll(polyToEdges(poly2));
        Vector2D[] axes = new Vector2D[edges.size()];
        for (int i = 0; i < edges.size(); i++) {
            axes[i] = edges.get(i).orthogonal();
        }

        for (Vector2D axis : axes) {
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
    private static Vector2D edgeVector(Vector2D point1, Vector2D point2) {
        return new Vector2D(point2.getX() - point1.getX(), point2.getY() - point1.getY());
    }

    /**
     * Returns an array of the edges of the poly as vectors
     */
    private static ArrayList<Vector2D> polyToEdges(Vector2D[] poly) {
        ArrayList<Vector2D> vectors = new ArrayList<>(poly.length);
        for (int i = 0; i < poly.length; i++) {
            vectors.add(edgeVector(poly[i], poly[(i + 1) % poly.length]));
        }
        return vectors;
    }

    /**
     * Returns the dot (or scalar) product of the two vectors
     */
    private static double dotProduct(Vector2D vector1, Vector2D vector2) {
        return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
    }

    /**
     * Returns a vector showing how much of the poly lies along the axis
     */
    private static Vector2D project(Vector2D[] poly, Vector2D axis) {
        List<Double> dots = new ArrayList<>();
        for (Vector2D vector : poly) {
            dots.add(dotProduct(vector, axis));
        }
        return new Vector2D(Collections.min(dots), Collections.max(dots));
    }

    /**
     * Returns a boolean indicating if the two projections overlap
     */
    private static boolean overlap(Vector2D projection1, Vector2D projection2) {
        return projection1.getX() <= projection2.getY() &&
                projection2.getX() <= projection1.getY();
    }

}