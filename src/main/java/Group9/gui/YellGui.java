package Group9.gui;

import javafx.scene.shape.Circle;

public class YellGui extends Circle implements GameObject {
    double x;
    double y;
    double r;
    public YellGui(double x, double y, double r)
    {
        super(x, y, r);
        updateScale();
    }
    @Override
    public void updateScale() {
        double scale = Scale.scale;
        this.setCenterX(x*scale);
        this.setCenterY(y*scale);
        this.setRadius(r*scale);
    }
}
