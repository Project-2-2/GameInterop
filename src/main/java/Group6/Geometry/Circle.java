package Group6.Geometry;

import Group6.Geometry.Collection.Points;
import Group6.Geometry.Contract.Area;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Tomasz Darmetko
 */
public class Circle implements Area {

    private Point center;
    private double radius;

    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public boolean hasInside(Point point) {
        return center.getDistance(point).getValue() < radius;
    }

    public boolean isInRange(Point point, Distance distance) {
        return center.getDistance(point).getValue() < distance.getValue() + radius;
    }

    public Points getIntersections(LineSegment lineSegment) {
        Line line = lineSegment.toLine();
        double p = center.getX();
        double q = center.getY();
        double r = radius;
        Function<Double, Double> computeC = (x) -> {
            return Math.pow(p, 2.) + Math.pow(q, 2.) - Math.pow(r, 2.) - 2 * x * p + Math.pow(x, 2.);
        };
        Set<Point> pointSet = new HashSet<>();
        if(line.isVertical()) {
            double k = line.getX();
            QuadraticFormula quadraticFormula = new QuadraticFormula(
                1, -2*q, computeC.apply(k)
            );
            for(Double root: quadraticFormula.getRoots()) {
                pointSet.add(new Point(k, root));
            }
        } else {
            double m = line.getSlope();
            double c = line.getYIntercept();
            QuadraticFormula quadraticFormula = new QuadraticFormula(
                Math.pow(m, 2.) + 1,
                2 * (m*c - m*q - p),
                computeC.apply(c)
            );
            for(Double root: quadraticFormula.getRoots()) {
                pointSet.add(new Point(root, line.getY(root)));
            }
        }
        return new Points(pointSet).filter(lineSegment::includes);
    }

}
