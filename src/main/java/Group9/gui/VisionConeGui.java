package Group9.gui;

import Group9.math.Vector2;
import javafx.scene.shape.*;

public class VisionConeGui extends Path implements GameObject {
    double range;
    double angle = 45;
    double maxX;
    double maxY;
    double x;
    double y;
    public VisionConeGui(Vector2 direction, double x, double y, double range)
    {
        super(new MoveTo(x,y));
        double directionAngle = direction.getAngle();
        this.range = range;
        double[] leftCoordinate = getCoordinate(directionAngle - angle*0.5, range);
        double[] rightCoordinate = getCoordinate(directionAngle + angle*0.5, range);

        this.getElements().add(new LineTo(leftCoordinate[0], leftCoordinate[1]));
        this.getElements().add(new QuadCurveTo(direction.getX(), direction.getY(), rightCoordinate[0], rightCoordinate[1]));
        this.getElements().add(new ClosePath());
        this.maxX = this.getLayoutBounds().getMaxX();
        this.maxY = this.getLayoutBounds().getMaxY();
        this.x = x;
        this.y = y;
    }
    public double[] getCoordinate(double angle, double range)
    {
        angle = Math.toRadians(angle);
        double[] coordinate = {Math.cos(angle) * range, Math.sin(angle) * range};
        return coordinate;
    }
    public void updateScale()
    {
        this.resize(this.maxX * Scale.scale, this.maxY * Scale.scale);
        this.setLayoutX(this.x * Scale.scale);
        this.setLayoutY(this.y * Scale.scale);
    }
    public void updatePosition(double x, double y)
    {
        this.x = x;
        this.y = y;
        updateScale();
    }
}
