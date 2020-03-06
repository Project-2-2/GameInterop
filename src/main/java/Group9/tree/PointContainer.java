package Group9.tree;

import Group9.math.Line;
import Group9.math.Vector2;

import java.util.Arrays;

public class PointContainer {

    public static class Quadrilateral extends PointContainer {

        private Vector2[] points = new Vector2[4];
        private Line[] lines = new Line[4];

        public Quadrilateral(Vector2 a, Vector2 b, Vector2 c, Vector2 d)
        {
            this.points[0] = a;
            this.points[1] = b;
            this.points[2] = c;
            this.points[3] = d;

            this.lines[0] = new Line(a, b);
            this.lines[1] = new Line(b, c);
            this.lines[2] = new Line(c, d);
            this.lines[3] = new Line(d, a);
        }

        public Vector2[] getPoints() {
            return points;
        }

        public Line[] getLines()
        {
            return this.lines;
        }


    }

    public static class Circle extends PointContainer
    {
        private Vector2 center;
        private double radius;

        public Circle(Vector2 center, double radius)
        {
            this.center = center;
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }

        public Vector2 getCenter() {
            return center;
        }
    }

    public static boolean intersect(PointContainer containerA, PointContainer containerB)
    {


        if (containerA instanceof Quadrilateral && containerB instanceof Quadrilateral)
        {

            //--- check if any of the lines intersect
            Quadrilateral ca = (Quadrilateral) containerA;
            Quadrilateral cb = (Quadrilateral) containerB;
            for(Line a : ca.getLines()) {
                for(Line b : cb.getLines())
                {
                    if(a.intersect(b))
                    {
                        return true;
                    }
                }
            }

            return Arrays.stream(ca.getPoints()).anyMatch(point -> isPointInside(cb, point))
                    || Arrays.stream(cb.getPoints()).anyMatch(point -> isPointInside(ca, point));
        }
        else if((containerA instanceof Circle || containerB instanceof Circle) && (containerA instanceof Quadrilateral || containerB instanceof Quadrilateral))
        {
            Circle circle = (containerA instanceof Circle) ? (Circle) containerA : (Circle) containerB;
            Quadrilateral quadrilateral = (containerA instanceof Quadrilateral) ? (Quadrilateral) containerA : (Quadrilateral) containerB;

            if(isPointInside(quadrilateral, circle.getCenter()))
            {
                return true;
            }

            //TODO

        }
        else if(containerA instanceof Circle && containerB instanceof Circle)
        {
            Circle a = (Circle) containerA;
            Circle b = (Circle) containerB;

            return a.getCenter().distance(b.getCenter()) < Math.min(a.getRadius(), b.getRadius());
        }
        else
        {
            throw new IllegalStateException();
        }

        return false;
    }

    private static boolean isPointInside(Quadrilateral q, Vector2 point)
    {
        // check if any of the points are contained in the other polygon
        int num = 4;
        int j = num -1;
        boolean c = false;

        for(int i = 0; i < num; i++)
        {
            /*if ((q.points[i].getY() > point.getY()) != (q.points[j].getY() > point.getY()))
                if (point.getX() < q.points[i].getX() + (q.points[j].getX() - q.points[i].getX()) * (point.getY() - q.points[i].getY()) /
                        (q.points[j].getY() - q.points[i].getY())) {
                    c = !c;
                }*/
            if (q.points[i].getY() < point.getY() && q.points[j].getY() >= point.getY()
                    ||  q.points[j].getY() < point.getY() && q.points[i].getY() >= point.getY()) {
                if (q.points[i].getY() + (point.getY() - q.points[i].getY())/(q.points[j].getY() - q.points[i].getY())*(q.points[j].getX()-q.points[i].getX()) < point.getX()) {
                    c = !c;
                }
            }
            j = i;
        }

        return c;
    }

}
