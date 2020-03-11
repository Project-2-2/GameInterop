package Group5.UI;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DrawableExplorerAgent extends Circle {

    // color, circle2D, coordinates, visible vision (maybe in another class "vision field")
    // think about what the agent should look like -->images, pacman, etc.
    public DrawableExplorerAgent(double centerX, double centerY, double radius, Paint fill){
        super(centerX, centerY, radius, fill);
    }
}
