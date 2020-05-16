package Group8.Launchers.GUIRes;

import javafx.scene.paint.Color;

public class StaticDrawable {

    private Color color;
    private boolean fill;

    public StaticDrawable(Color color, boolean fill) {
        this.color = color;
        this.fill = fill;
    }

    public Color getColor() {
        return color;
    }

    public boolean isFill() {
        return fill;
    }
}
