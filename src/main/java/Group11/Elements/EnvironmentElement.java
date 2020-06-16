package Group11.Elements;

public class EnvironmentElement {
    private double[] points;
    private EnvironmentType type;

    public EnvironmentElement(double[] points, EnvironmentType type) {
        this.points = points;
        this.type = type;
    }

    public double[] getPoints() {
        return points;
    }

    public void setPoints(double[] points) {
        this.points = points;
    }

    public EnvironmentType getType() {
        return type;
    }

    public void setType(EnvironmentType type) {
        this.type = type;
    }
}
