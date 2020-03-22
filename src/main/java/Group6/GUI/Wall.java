package Group6.GUI;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Wall extends Polygon implements GameShapes {

    final double[] x;
    final double[] y;

    public Wall(double[] x, double[] y) {
        super(x[0],y[0],x[1],y[1],x[2],y[2],x[3],y[3]);
        this.x = x;
        this.y = y;
        setFill(Color.GRAY);
        UpdateScale();
    }

    @Override
    public void UpdateScale() {

        double scale = Scale.scale;
        this.getPoints().setAll(x[0]*scale,y[0]*scale,x[1]*scale,y[1]*scale,x[2]*scale,y[2]*scale,x[3]*scale,y[3]*scale);
    }
}
