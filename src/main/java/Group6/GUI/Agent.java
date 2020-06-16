package Group6.GUI;

import Group6.WorldState.Object.AgentState;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Agent extends Circle implements GameShapes{

    final double radius;
    final double x;
    final double y;

    public Agent(AgentState agentState){
        this(agentState.getRADIUS(),
             agentState.getLocation().getX(),
             agentState.getLocation().getY());
    }

    public Agent(double radius, double x, double y) {
        super(radius);
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    @Override
    public void UpdateScale() {
        double scale = Scale.scale;
        setRadius(radius * scale);
        setCenterX(x * scale);
        setCenterY(y * scale);
    }
}
