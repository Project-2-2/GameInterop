package Group9.tree;

import Group9.math.Vector2;
import Interop.Geometry.Point;

public interface PointContainer {

    boolean intersect();

    public static class Quadrilateral implements PointContainer {

        private Point a, b, c, d;

        public Quadrilateral(Point a, Point b, Point c, Point d)
        {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override
        public boolean intersect() {
            return false;
        }
    }

    public static interface Circle extends PointContainer
    {
        Vector2 getCenter();

        double getRadius();

    }

}
