package Group9.tree;

import Group9.math.Line;
import Group9.math.Vector2;
import Interop.Geometry.Vector;

import java.util.Arrays;

public abstract class PointContainer {

    abstract public void translate(Vector2 vector);

    abstract public Vector2 getCenter();

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

        @Override
        public void translate(Vector2 vector) {
            for (int i = 0; i < this.points.length; i++) {
                this.points[i] = this.points[i].add(vector);
            }

            this.lines[0] = new Line(this.points[0], this.points[1]);
            this.lines[1] = new Line(this.points[1], this.points[2]);
            this.lines[2] = new Line(this.points[2], this.points[3]);
            this.lines[3] = new Line(this.points[3], this.points[0]);
        }

        @Override
        public Vector2 getCenter() {
            return twoLinesIntersect(points[0], points[2], points[1], points[3]);
        }

        @Override
        public Quadrilateral clone() throws CloneNotSupportedException {
            return new Quadrilateral(this.points[0].clone(), this.points[1].clone(), this.points[2].clone(),
                    this.points[3].clone());
        }



        public static void main(String args[]){
            Quadrilateral a = new Quadrilateral(new Vector2(1,1), new Vector2(2,2), new Vector2(1,4), new Vector2(8,12));
            System.out.println(a.getCenter());
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

        @Override
        public Vector2 getCenter() {
            return center;
        }

        @Override
        public void translate(Vector2 vector) {
            this.center = this.center.add(vector);
        }

        @Override
        public Circle clone() throws CloneNotSupportedException {
            return new Circle(center.clone(), this.radius);
        }
    }

    @Override
    public PointContainer clone() throws CloneNotSupportedException {
        if(this instanceof Circle)
        {
            return ((Circle) this).clone();
        }
        else if(this instanceof Quadrilateral)
        {
            return ((Quadrilateral) this).clone();
        }
        throw new CloneNotSupportedException();
    }

    public static boolean intersect(PointContainer containerA, Line other)
    {
        if(containerA instanceof Quadrilateral)
        {
            for (Line a : ((Quadrilateral) containerA).getLines()) {
                if(a.intersect(other))
                {
                    return true;
                }
            }

            return false;
        }
        else if(containerA instanceof Circle)
        {
            //TODO should be the same as :CircleIntersection
            throw new IllegalArgumentException("Implement circle intersection test");
        }
        else
        {
            throw new IllegalArgumentException();
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

            //TODO :CircleIntersection

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

    /**
     * Calculate whether 2 lines intersect with each other
     * @param vec1 start point of line 1.
     * @param vec2 start point of line 2.
     * @param vec3 start point of line 3.
     * @param vec4 start point of line 4.
     * @return the vector that points to the intersection point, returns null when no intersection is found
     */
    public Vector2 twoLinesIntersect(Vector2 vec1, Vector2 vec2, Vector2 vec3, Vector2 vec4){
        //http://mathworld.wolfram.com/Line-LineIntersection.html
        double x1 = vec1.getX();
        double y1 = vec1.getY();
        double x2 = vec2.getX();
        double y2 = vec2.getY();
        double x3 = vec3.getX();
        double y3 = vec3.getY();
        double x4 = vec4.getX();
        double y4 = vec4.getY();
        double parallelDenominator = determinant(x1-x2, y1-y2, x3-x4, y3-y4);
        System.out.println(parallelDenominator);

        if(parallelDenominator == 0.0){
            return null;
        }

        double determinantLine1 = determinant(x1, y1, x2, y2);
        double determinantLine2 = determinant(x3, y3, x4, y4);
        double xValue = determinant(determinantLine1, x1-x2, determinantLine2, y1-y2);
        double yValue = determinant(determinantLine1, x3-x4, determinantLine2, y3-y4);
        double xToCheck = xValue/parallelDenominator;
        double yToCheck = yValue/parallelDenominator;

        if((((x1>=xToCheck&&x2<=xToCheck)||(x2>=xToCheck&&x1<=xToCheck))&&((y1>=yToCheck&&y2<=yToCheck)||(y2>=yToCheck&&y1<=yToCheck))) &&
                (((x3>=xToCheck&&x4<=xToCheck)||(x4>=xToCheck&&x3<=xToCheck))&&((y3>=yToCheck&&y4<=yToCheck)||(y4>=yToCheck&&y3<=yToCheck)))){
            return new Vector2(xToCheck,yToCheck);
        }

        return null;
    }
    private double determinant(double x1, double y1, double x2, double y2){
        return (x1*y2)-(x2*y1);
    }

}
