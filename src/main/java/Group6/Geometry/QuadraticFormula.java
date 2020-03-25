package Group6.Geometry;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tomasz Darmetko
 */
public class QuadraticFormula {

    private double a;
    private double b;
    private double c;

    public QuadraticFormula(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getDelta() {
        return Math.pow(b, 2.) - 4 * (a * c);
    }

    public boolean hasNoRoots() {
        return !hasOneRoot() && !hasTwoRoots();
    }

    public boolean hasOneRoot() {
        return Math.abs(getDelta()) < Tolerance.epsilon;
    }

    public boolean hasTwoRoots() {
        return getDelta() >= Tolerance.epsilon;
    }

    public Set<Double> getRoots() {
        Set<Double> roots = new HashSet<>();
        if(hasNoRoots()) return roots;
        if(hasOneRoot()) roots.add(-b / (2 * a));
        if(hasTwoRoots()) {
            double deltaSqrt = Math.sqrt(getDelta());
            roots.add((-b + deltaSqrt) / (2 * a));
            roots.add((-b - deltaSqrt) / (2 * a));
        }
        return roots;
    }

    public String toString() {
        return "QuadraticFormula{" +
            "a=" + a +
            ", b=" + b +
            ", c=" + c +
            ", delta=" + getDelta() +
            ", roots=" + getRoots() +
            '}';
    }
}
