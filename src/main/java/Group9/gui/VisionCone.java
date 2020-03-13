package Group9.gui;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class VisionCone extends Path implements GameObject {
    public VisionCone(double x, double y)
    {
        super(new MoveTo(x,y), new LineTo());
    }
    public void updateScale()
    {

    }
}
