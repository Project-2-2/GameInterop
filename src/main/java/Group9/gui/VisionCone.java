package Group9.gui;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class VisionCone extends Path implements GameObject {
    double range;
    double angle = 45;
    public VisionCone(double x, double y, double range)
    {
        super(new MoveTo(x,y), new LineTo());
    }
    public void updateScale()
    {

    }
    public void updatePosition(double x, double y)
    {

    }
}
