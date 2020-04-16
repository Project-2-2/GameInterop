package Group6.GUI;


import Group6.Geometry.Point;
import javafx.scene.paint.Color;

public class Pheromone extends Dot {
    public Pheromone(Point center, double radius) {
        super(center, radius);
        setFill(Color.PINK);
    }
}
