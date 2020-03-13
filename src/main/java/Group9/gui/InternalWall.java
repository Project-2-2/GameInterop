package Group9.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InternalWall extends Rectangle implements GameObject {
    final double x;
    final double y;
    final double width;
    final double height;
    public InternalWall(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4)
    {
        super();
        width = x3 - x1;
        height = y3 - y1;
        setLayoutX(x1);
        setLayoutY(y1);
        setWidth(width);
        setHeight(height);
        this.x = x1;
        this.y = y1;
        setFill(Color.LAVENDER);
        setStroke(Color.BLACK);
    }
    public void updateScale()
    {
        double scale = Scale.scale;
        setLayoutX(x*scale);
        setLayoutY(y*scale);
        setWidth(width*scale);
        setHeight(height*scale);
    }
}
