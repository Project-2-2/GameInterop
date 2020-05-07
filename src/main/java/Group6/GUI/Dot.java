package Group6.GUI;

import Group6.Geometry.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot extends Circle implements GameShapes{
    final Point center;
    final double radius;


    public Dot(Point center, double radius) {
        this.center=center;
        this.radius= radius;
        setFill(Color.PINK);
        UpdateScale();
    }
    @Override
    public void UpdateScale() {
        double scale = Scale.scale;
        this.setRadius(scale*radius);
        this.setCenterX(scale*getCenterX());
        this.setCenterY(scale*getCenterY());
    }
}

