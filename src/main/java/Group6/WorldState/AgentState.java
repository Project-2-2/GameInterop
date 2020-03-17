package Group6.WorldState;

import Group6.Geometry.*;

public abstract class AgentState {

    private Point location;
    private Direction direction;
    private int cooldown;
    private boolean justTeleported;
    private boolean wasLastActionExecuted;

    protected AgentState(Point location, Direction direction, int cooldown, boolean justTeleported, boolean wasLastActionExecuted) {
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

    public boolean hasCooldown() {
        return cooldown > 0;
    }

    public void nextTurn() {
        if(hasCooldown()) cooldown--;
    }

    public boolean isJustTeleported() {
        return justTeleported;
    }

    public boolean wasLastActionExecuted() {
        return wasLastActionExecuted;
    }

    public void rejectAction() {
        wasLastActionExecuted = false;
    }

    public void move(Distance distance) {
        if(hasCooldown()) throw new IllegalActionDuringCooldown("Move");
        Vector displacement = new Vector(0, distance.getValue()).rotate(direction.getRadians());
        location = location.add(displacement).toPoint();
        wasLastActionExecuted = true;
    }

    public void rotate(Angle angle) {
        if(hasCooldown()) throw new IllegalActionDuringCooldown("Rotate");
        direction = direction.getChangedBy(angle);
        wasLastActionExecuted = true;
    }

    class IllegalActionDuringCooldown extends RuntimeException {
        public IllegalActionDuringCooldown(String action) {
            super(
                "Following action: " + action + " can not be executed during cooldown!\n" +
                "Cooldown left: " + cooldown
            );
        }
    }

}
