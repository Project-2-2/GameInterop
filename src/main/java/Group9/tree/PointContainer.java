package Group9.tree;

import Group9.Game;
import Group9.math.Vector2;

import java.util.*;

public abstract class PointContainer {

    abstract public void translate(Vector2 vector);

    abstract public Vector2 getCenter();

    /**
     * This class supports non-self-intersecting closed polygons of arbitrary size >= 3.
     */
    public static class Polygon extends PointContainer {

        private Vector2[] points;
        private Line[] lines;

        public Polygon(Vector2 ...points)
        {
            if(points.length < 3)
            {
                throw new IllegalArgumentException("A polygon must consist of at least 3 points");
            }

            this.points = points;
            this.lines = new Line[this.points.length];
            for(int i = 0; i < this.points.length; i++)
            {
                this.lines[i] = new Line(this.points[i], this.points[(i + 1) % this.points.length]);
            }
        }

        public double getArea()
        {
            double area = 0;
            for(int i = 0; i < points.length; i++)
            {
                area += (points[i].getX() * points[(i + 1) % points.length].getY()
                        - points[(i + 1) % points.length].getX() * points[i].getY());
            }
            return Math.abs(area) * 0.5;
        }

        public Vector2[] getPoints() {
            return points;
        }

        public Line[] getLines()
        {
            return this.lines;
        }

        public List<Vector2[]> getTriangles()
        {
            List<Vector2[]> triangles = new ArrayList<>();

            for(int i = 1; i <= this.points.length - 2; i++)
            {
                triangles.add(new Vector2[] {
                        this.points[0], this.points[i], this.points[(i + 1)]
                });
            }

            return triangles;
        }

        public Vector2 generateRandomLocation()
        {
            //--- follows: https://www.cs.princeton.edu/~funk/tog02.pdf @ 4.2

            // TODO if we wanted to make this actually uniform, we would need to calculate the area of the triangles
            //  and weight them appropriately... (low priority)
            List<Vector2[]> triangles = getTriangles();
            Vector2[] triangle = triangles.get(Math.round(Game._RANDOM.nextFloat() * (triangles.size() - 1)));

            Vector2 A = triangle[0];
            Vector2 B = triangle[1];
            Vector2 C = triangle[2];
            double r1 = Game._RANDOM.nextDouble();
            double r2 = Game._RANDOM.nextDouble();

            //--- P = A * sqrt(r1) + B * (1-r2)*sqrt(r1) + C * sqrt(r1)*r2
            final Vector2 r =  A.mul(1 - Math.sqrt(r1))
                    .add(B.mul((1 - r2) * Math.sqrt(r1)))
                    .add(C.mul(Math.sqrt(r1)*r2));
            //|| isInTriangle(q.points[0], q.points[2], q.points[3], point);

            assert isPointInside(r);
            return r;
        }

        /**
         * Splits the Polygon into triangles and uses a Barycentric technique to check whether it is in one of the
         * triangles.
         *
         * @link https://blackpawn.com/texts/pointinpoly/default.html
         * @param point
         * @return true, if inside, otherwise false.
         */
        private boolean isPointInside(Vector2 point)
        {
            return getTriangles().stream().anyMatch(e -> isInTriangle(e[0], e[1], e[2], point));
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

        @Override
        public void translate(Vector2 vector) {
            for (int i = 0; i < this.points.length; i++) {
                this.points[i] = this.points[i].add(vector);
            }

            for(int i = 0; i < points.length; i++)
            {
                lines[i] = new Line(points[i], points[(i + 1) % points.length]);
            }
        }

        @Override
        public Vector2 getCenter() {
            final double divisor = 6D * this.getArea();
            double cx = 0;
            for(int i = 0; i < points.length; i++)
            {
                cx += (points[i].getX() + points[(i + 1) % points.length].getX())
                        * (points[i].getX() * points[(i + 1) % points.length].getY()
                        - points[(i + 1) % points.length].getX() * points[i].getY());
            }

            double cy = 0;
            for(int i = 0; i < points.length; i++)
            {
                cy += (points[i].getY() + points[(i + 1) % points.length].getY())
                        * (points[i].getX() * points[(i + 1) % points.length].getY()
                        - points[(i + 1) % points.length].getX() * points[i].getY());
            }


            return new Vector2(cx/divisor, cy/divisor);
        }

        @Override
        public PointContainer.Polygon clone() {
            return new Polygon(this.points);
        }

        @Override
        public String toString() {
            return "Polygon{" +
                    "points=" + Arrays.toString(points) +
                    ", lines=" + Arrays.toString(lines) +
                    '}';
        }
    }

    public static class Quadrilateral extends Polygon {

        private Vector2[] points = new Vector2[4];
        private Line[] lines = new Line[4];

        public Quadrilateral(Vector2 a, Vector2 b, Vector2 c, Vector2 d)
        {
            super(a, b, c, d);
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

        @Override
        public PointContainer.Line clone() {
            return new Line(this.start, this.end);
        }
    }


    @Override
    public PointContainer clone() throws CloneNotSupportedException {
        if(this instanceof Line)
        {
            return ((Line) this).clone();
        }
        else if(this instanceof Circle)
        {
            return ((Circle) this).clone();
        }
        else if(this instanceof Polygon)
        {
            return ((Polygon) this).clone();
        }
        throw new CloneNotSupportedException();
    }

    public PointContainer.Polygon getAsPolygon()
    {
        return (PointContainer.Polygon) this;
    }

    public PointContainer.Circle getAsCircle()
    {
        return (PointContainer.Circle) this;
    }

    public static boolean intersect(PointContainer containerA, PointContainer containerB)
    {

        if (containerA instanceof Polygon || containerB instanceof Polygon)
        {

            Polygon polygon = (containerA instanceof Polygon) ? (Polygon) containerA : (Polygon) containerB;

            if(containerA instanceof Polygon && containerB instanceof Polygon)
            {
                Polygon other = (polygon == containerB) ? (Polygon) containerA : (Polygon) containerB;
                for(Line a : polygon.getLines()) {
                    for(Line b : other.getLines())
                    {
                        if(intersect(a, b))
                        {
                            return true;
                        }
                    }
                }

                return Arrays.stream(polygon.getPoints()).anyMatch(other::isPointInside)
                        || Arrays.stream(other.getPoints()).anyMatch(polygon::isPointInside);
            }
            else if(containerA instanceof Circle || containerB instanceof Circle)
            {
                Circle circle = (containerA instanceof Circle) ? (Circle) containerA : (Circle) containerB;

                if(polygon.isPointInside(circle.getCenter()))
                {
                    return true;
                }

                for(Line a : polygon.getLines())
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
                for(Line a : polygon.getLines())
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
        } else if (pointContainer instanceof Polygon) {
            Polygon q = (Polygon) pointContainer;

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
