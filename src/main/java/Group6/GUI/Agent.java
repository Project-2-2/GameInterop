package Group6.GUI;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Agent extends Circle implements GameShapes{

    final double radius;
    final double x;
    final double y;

    public Agent(double radius, double x, double y) {
        super(radius);
        this.radius = radius;
        this.x = x;
        this.y = y;
        setFill(Color.BLACK);
    }

    @Override
    public void UpdateScale() {
        double scale = Scale.scale;
        setRadius(radius * scale);
        setCenterX(x * scale);
        setCenterY(y * scale);
    }
}
