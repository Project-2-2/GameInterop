package Group9.tree;

import Group9.Game;
import Group9.math.Vector2;
import Interop.Utils.Utils;

import java.util.*;

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
            Vector2 center = twoLinesIntersect(points[0], points[2], points[1], points[3]);
            assert center != null;
            return center;
        }

        @Override
        public Quadrilateral clone() throws CloneNotSupportedException {
            return new Quadrilateral(this.points[0].clone(), this.points[1].clone(), this.points[2].clone(),
                    this.points[3].clone());
        }

        public Vector2 generateRandomLocation()
        {

            //--- follows: https://www.cs.princeton.edu/~funk/tog02.pdf @ 4.2
            //--- pick one of the two possible triangles
            // TODO if we wanted to make this actually uniform, we would need to calculate the area of the triangles
            //  and weight them appropriately... (low priority)
            Vector2 A = points[0];
            Vector2 B = points[1];
            Vector2 C = points[2];

            if(Game._RANDOM.nextBoolean())
            {
                B = points[2];
                C = points[3];
            }

            double r1 = Game._RANDOM.nextDouble();
            double r2 = Game._RANDOM.nextDouble();

            //--- P = A * sqrt(r1) + B * (1-r2)*sqrt(r1) + C * sqrt(r1)*r2
            final Vector2 r =  A.mul(1 - Math.sqrt(r1))
                    .add(B.mul((1 - r2) * Math.sqrt(r1)))
                    .add(C.mul(Math.sqrt(r1)*r2));
                    //|| isInTriangle(q.points[0], q.points[2], q.points[3], point);

            assert PointContainer.isPointInside(this, r);
            return r;
        }


        public static class Rectangle extends Quadrilateral {
            private double topY, bottomY, leftmostX, rightmostX;

            public Rectangle(Vector2 a, Vector2 b, Vector2 c, Vector2 d) {
                super(a, b, c, d);
            }

            public Rectangle(double topY, double bottomY, double leftmostX, double rightmostX) {
                super(  new Vector2(leftmostX, topY),
                        new Vector2(rightmostX, topY),
                        new Vector2(rightmostX, bottomY),
                        new Vector2(leftmostX, bottomY)
                );

                this.topY = topY;
                this.bottomY = bottomY;
                this.leftmostX = leftmostX;
                this.rightmostX = rightmostX;
            }

            public double getHorizonalSize() {
                return Math.abs(rightmostX - leftmostX);
            }

            public double getVerticalSize() {
                return Math.abs(topY - bottomY);
            }

            public double getTopY() {
                return topY;
            }

            public double getBottomY() {
                return bottomY;
            }

            public double getLeftmostX() {
                return leftmostX;
            }

            public double getRightmostX() {
                return rightmostX;
            }

        }

        /**
         * Returns the smallest rectangle (whose sides are parallel to x and y axises) containing the Quadrilateral
         * Idea: https://imgur.com/a/NOwvwRM
         * @return
         */
        public static Quadrilateral.Rectangle containingRectangle(Quadrilateral q) {
            // not efficient, but probably sufficiently efficient
            double topY = Arrays.stream(q.getPoints()).min(Comparator.comparing(Vector2::getY)).get().getY();
            double bottomY = Arrays.stream(q.getPoints()).max(Comparator.comparing(Vector2::getY)).get().getY();
            double rightmostX = Arrays.stream(q.getPoints()).max(Comparator.comparing(Vector2::getX)).get().getX();
            double leftmostX = Arrays.stream(q.getPoints()).min(Comparator.comparing(Vector2::getX)).get().getX();

            return new Quadrilateral.Rectangle(topY, bottomY, leftmostX, rightmostX);
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
            return new Circle(center.clone(), this.getRadius());
        }
    }

    public static class Line extends PointContainer{

        private Vector2 start;
        private Vector2 end;

        public Line(Vector2 start, Vector2 end)
        {
            super();
            if(start.getX() <= end.getX())
            {
                this.start = start;
                this.end = end;
            }
            else
            {
                this.start = end;
                this.end = start;
            }

        }

        public Vector2 getStart()
        {
            return this.start;
        }

        public Vector2 getEnd()
        {
            return this.end;
        }

        public Vector2 getNormal()
        {
            double dx = end.getX() - start.getX();
            double dy = end.getY() - start.getY();
            return new Vector2(-dy, dx).normalise();
        }

        @Override
        public String toString() {
            return "Line{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }

        @Override
        public void translate(Vector2 vector) {
            this.start = this.start.add(vector);
            this.end = this.end.add(vector);

        }

        @Override
        public Vector2 getCenter() {
            return this.start.add(this.end).mul(0.5);
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

    public PointContainer.Quadrilateral getAsQuadrilateral()
    {
        return (PointContainer.Quadrilateral) this;
    }

    public PointContainer.Circle getAsCircle()
    {
        return (PointContainer.Circle) this;
    }

    public static boolean intersect(PointContainer containerA, PointContainer containerB)
    {

        if (containerA instanceof Quadrilateral || containerB instanceof Quadrilateral)
        {

            Quadrilateral quadrilateral = (containerA instanceof Quadrilateral) ? (Quadrilateral) containerA : (Quadrilateral) containerB;

            if(containerA instanceof Quadrilateral && containerB instanceof Quadrilateral)
            {
                Quadrilateral other = (quadrilateral == containerB) ? (Quadrilateral) containerA : (Quadrilateral) containerB;
                for(Line a : quadrilateral.getLines()) {
                    for(Line b : other.getLines())
                    {
                        if(intersect(a, b))
                        {
                            return true;
                        }
                    }
                }

                return Arrays.stream(quadrilateral.getPoints()).anyMatch(point -> isPointInside(other, point))
                        || Arrays.stream(other.getPoints()).anyMatch(point -> isPointInside(quadrilateral, point));
            }
            else if(containerA instanceof Circle || containerB instanceof Circle)
            {
                Circle circle = (containerA instanceof Circle) ? (Circle) containerA : (Circle) containerB;

                if(isPointInside(quadrilateral, circle.getCenter()))
                {
                    return true;
                }

                for(Line a : quadrilateral.getLines())
                {
                    if(circleLineIntersect(circle, a).length != 0)
                    {
                        return true;
                    }
                }

                return false;
            }
            else if(containerA instanceof Line || containerB instanceof Line)
            {
                Line line = (containerA instanceof Line) ? (Line) containerA : (Line) containerB;
                for(Line a : quadrilateral.getLines())
                {
                    if(intersect(a, line))
                    {
                        return true;
                    }
                }
                return false;
            }

        }
        else if(containerA instanceof Circle && containerB instanceof Circle)
        {
            Circle a = (Circle) containerA;
            Circle b = (Circle) containerB;

            return a.getCenter().distance(b.getCenter()) < Math.min(a.getRadius(), b.getRadius());
        }
        else if(containerA instanceof Line || containerB instanceof Line)
        {
            Line line = (containerA instanceof Line) ? (Line) containerA : (Line) containerB;

            if(containerA instanceof Line && containerB instanceof Line)
            {
                Line other = (line == containerA) ? (Line) containerB : (Line) containerA;
                return PointContainer.twoLinesIntersect(line.getStart(), line.getEnd(), other.getStart(), other.getEnd()) != null;
            }
            else if(containerA instanceof Circle || containerB instanceof Circle)
            {
                Circle circle = (containerA instanceof Circle) ? (Circle) containerA : (Circle) containerB;
                return circleLineIntersect(circle, line).length != 0;
            }

        }

        throw new IllegalArgumentException();

    }

    /**
     * Splits the quadrilateral into two triangles and uses a Barycentric technique to check whether it is in one of the
     * triangles.
     *
     * @link https://blackpawn.com/texts/pointinpoly/default.html
     * @param q
     * @param point
     * @return true, if inside, otherwise false.
     */
    private static boolean isPointInside(Quadrilateral q, Vector2 point)
    {
        return isInTriangle(q.points[0], q.points[1], q.points[2], point)
                || isInTriangle(q.points[0], q.points[2], q.points[3], point);
    }

    private static boolean isInTriangle(Vector2 A, Vector2 B, Vector2 C, Vector2 P)
    {
        Vector2 v0 = C.sub(A);
        Vector2 v1 = B.sub(A);
        Vector2 v2 = P.sub(A);

        double dot00 = v0.dot(v0);
        double dot01 = v0.dot(v1);
        double dot02 = v0.dot(v2);
        double dot11 = v1.dot(v1);
        double dot12 = v1.dot(v2);

        double invDenom = 1D / (dot00 * dot11 - dot01 * dot01);
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= 0) && (v >= 0) && (u + v < 1);
    }

    private static Vector2[] circleLineIntersect(Circle circle, Line line){
        //https://mathworld.wolfram.com/Circle-LineIntersection.html
        Vector2[] returnArray = new Vector2[0];
        double r = circle.getRadius();
        Vector2 centerCircle = circle.getCenter();

        double dx = line.getEnd().getX() - line.getStart().getX(); //- 2*centerCircle.getX();
        double dy = line.getEnd().getY() - line.getStart().getY(); //- 2*centerCircle.getY();
        double dr = Math.sqrt(dx*dx + dy*dy);

        double sgndy =  dy >= 0 ? 1 : -1;

        double determinant = determinant(line.getStart().getX()-centerCircle.getX(),line.getStart().getY()-centerCircle.getY(),
                line.getEnd().getX()-centerCircle.getX(),line.getEnd().getY()-centerCircle.getY());

        double discriminant = r*r * dr*dr - (determinant*determinant);

        //TODO: Maybe decide the right case using an EPSILON value to make close calls for the tangent lines more clear
        if(discriminant == 0) {
            returnArray = new Vector2[1];
            double returnX = (determinant * dy) / (dr*dr);
            double returnY = (-determinant * dx) / (dr*dr);
            returnArray[0] = new Vector2(returnX+centerCircle.getX(), returnY+centerCircle.getY());

        }  else if(discriminant > 0) {
            returnArray = new Vector2[2];
            double returnX1 = ((determinant * dy) - (sgndy * dx * Math.sqrt(discriminant)))/(dr*dr);
            double returnX2 = ((determinant * dy) + (sgndy * dx * Math.sqrt(discriminant)))/(dr*dr);
            double sqrtD = Math.abs(dy) * Math.sqrt(discriminant);
            double returnY1 = ((-determinant * dx) - sqrtD)/(dr*dr);
            double returnY2 = ((-determinant * dx) + sqrtD)/(dr*dr);
            returnArray[0] = new Vector2(returnX1+centerCircle.getX(), returnY1+centerCircle.getY());
            returnArray[1] = new Vector2(returnX2+centerCircle.getX(), returnY2+centerCircle.getY());
        }

        return returnArray;
    }

    /**
     * Calculate whether 2 lines intersect with each other
     * @param vec1 start point of line 1.
     * @param vec2 start point of line 2.
     * @param vec3 start point of line 3.
     * @param vec4 start point of line 4.
     * @return the vector that points to the intersection point, returns null when no intersection is found
     */
    private static Vector2 twoLinesIntersect(Vector2 vec1, Vector2 vec2, Vector2 vec3, Vector2 vec4){
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

        if(parallelDenominator == 0.0){
            return null;
        }

        double determinantLine1 = determinant(x1, y1, x2, y2);
        double determinantLine2 = determinant(x3, y3, x4, y4);
        double xValue = determinant(determinantLine1, x1-x2, determinantLine2, x3-x4);
        double yValue = determinant(determinantLine1, y1-y2, determinantLine2, y3-y4);
        double xToCheck = xValue/parallelDenominator;
        double yToCheck = yValue/parallelDenominator;

        if (((x1 >= xToCheck && x2 <= xToCheck) || (x2 >= xToCheck && x1 <= xToCheck)) && ((y1 >= yToCheck && y2 <= yToCheck) || (y2 >= yToCheck && y1 <= yToCheck)))
            if (((x3 >= xToCheck && x4 <= xToCheck) || (x4 >= xToCheck && x3 <= xToCheck)) && ((y3 >= yToCheck && y4 <= yToCheck) || (y4 >= yToCheck && y3 <= yToCheck))) {
                return new Vector2(xToCheck, yToCheck);
            }

        return null;
    }

    private static Vector2 twoLinesIntersect(Line a, Line b) {
        return twoLinesIntersect(a.getStart(), a.getEnd(), b.getStart(), b.getEnd());
    }

    public static List<Vector2> intersectionPoints(PointContainer pointContainer, Line l) {
        List<Vector2> intersectionPoints = new ArrayList<Vector2>();

        if (pointContainer instanceof Line) {
            intersectionPoints.add(twoLinesIntersect((Line) pointContainer,l));
        } else if (pointContainer instanceof Circle) {
            Collections.addAll(intersectionPoints, circleLineIntersect((Circle) pointContainer, l));
        } else if (pointContainer instanceof Quadrilateral) {
            Quadrilateral q = (Quadrilateral) pointContainer;

            for (Line ql : q.getLines()) {
                Vector2 intersectPoint = twoLinesIntersect(ql, l);
                if (intersectPoint != null) {
                    intersectionPoints.add(intersectPoint);
                }
            }
        }

        return intersectionPoints;
    }


    /**
     * matrix defined as
     * | a b |
     * | c d |
     * note,
     * @param x1 a or d
     * @param y1 c or b
     * @param x2 b or c
     * @param y2 d or a
     * @return The determinant of a 2x2 matrix
     */
    private static double determinant(double x1, double y1, double x2, double y2){
        return (x1*y2)-(x2*y1);
    }
}
