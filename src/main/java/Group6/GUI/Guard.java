package Group6.GUI;

import Group6.WorldState.Object.GuardState;
import javafx.scene.paint.Color;

public class Guard extends Agent {
    private final Color color = Color.WHITE;

    public Guard(GuardState guardState) {
        this(guardState.getRADIUS(),
                guardState.getLocation().getX(),
                guardState.getLocation().getY());
    }
    public Guard(double radius, double x, double y){
        super(radius,x,y);
        setFill(color);
    }

}
