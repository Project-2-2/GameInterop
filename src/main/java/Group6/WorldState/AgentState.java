package Group6.WorldState;

import Group6.Geometry.Direction;
import Group6.Geometry.Point;

public abstract class AgentState {

    private Point location;
    private Direction direction;
    private int cooldown;
    private boolean justTeleported;
    private boolean wasLastActionExecuted;

    public AgentState(Point location, Direction direction, int cooldown, boolean justTeleported, boolean wasLastActionExecuted) {
        this.location = location;
        this.direction = direction;
        this.cooldown = cooldown;
        this.justTeleported = justTeleported;
        this.wasLastActionExecuted = wasLastActionExecuted;
    }

    public Point getLocation() {
        return location;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean isJustTeleported() {
        return justTeleported;
    }

    public boolean wasLastActionExecuted() {
        return wasLastActionExecuted;
    }

}
