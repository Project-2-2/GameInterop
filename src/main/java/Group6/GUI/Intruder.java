package Group6.GUI;

import Group6.WorldState.Object.GuardState;
import Group6.WorldState.Object.IntruderState;

import javafx.scene.paint.Color;

public class Intruder extends Agent {
    private final Color color = Color.BLACK;

    public Intruder(IntruderState intruderState) {
        this(intruderState.getRADIUS(),
                intruderState.getLocation().getX(),
                intruderState.getLocation().getY());
    }
    public Intruder(double radius, double x, double y){
        super(radius,x,y);
        setFill(color);
    }

}
