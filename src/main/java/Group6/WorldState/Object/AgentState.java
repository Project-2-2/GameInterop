package Group6.WorldState.Object;

import Group6.Geometry.*;
import Group6.Geometry.Collection.Points;
import Group6.Geometry.Collection.Quadrilaterals;
import Group6.Geometry.Contract.Area;
import Group6.WorldState.Contract.Object;
import Group6.WorldState.Pheromone;
import Group6.WorldState.Teleports;
import Group6.WorldState.WorldState;
import Interop.Action.Action;
import Interop.Action.DropPheromone;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Utils.Require;

import java.util.Set;

public abstract class AgentState implements Object {

    private final double RADIUS = 0.5;

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

    public boolean isInRange(Point point, Distance distance) {
        return location.getDistance(point).getValue() <= distance.getValue();
    }

    public boolean isInside(Area area) {
        return location.isInside(area);
    }

    public Direction getDirection() {
        return direction;
    }

    public Direction getPerceivedDirectionTo(Point point) {
        return direction.getRelativeTo(
            point.subtract(location).toPoint().getClockDirection()
        );
    }

    public int getCooldown() {
        return cooldown;
    }

    public boolean hasCooldown() {
        return cooldown > 0;
    }

    public void addCooldown(int length) {
        Require.positive(length, "Cooldown length must be at least 1 turn.");
        this.cooldown += length;
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

    public void move(WorldState worldState, Move action) {
        requireNoCooldown(action);
        move(worldState, new Distance(((Move)action).getDistance()));
        markActionAsExecuted();
    }

    protected void move(WorldState worldState, Distance distance) {

        Vector displacement = new Vector(0, distance.getValue()).rotate(direction.getRadians());
        location = location.add(displacement).toPoint();

        Teleports teleports = worldState.getScenario().getTeleports();
        if(!isJustTeleported() && isInside(teleports)) {
            teleport(teleports);
        }

    }

    protected void teleport(Teleports teleports) {
        location = teleports.getTargetArea(location).getRandomPointInside();
        direction = Direction.random();
        justTeleported = true;
    }

    public void rotate(WorldState worldState, Rotate action) {
        requireNoCooldown(action);
        direction = direction.getChangedBy(
            Angle.fromInteropAngle(action.getAngle())
        );
        markActionAsExecuted();
    }

    public void dropPheromone(WorldState worldState, DropPheromone action) {
        requireNoCooldown(action);
        worldState.addPheromone(
            Pheromone.createByAgent(worldState, this, (DropPheromone)action)
        );
        addCooldown(worldState.getScenario().getPheromoneCooldown());
        markActionAsExecuted();
    }

    public void noAction() {
        markActionAsExecuted();
    }

    public Points getIntersections(LineSegment lineSegment) {
        return new Points(); // TODO
    }

    public boolean hasInside(Point point) {
        return location.getDistance(point).getValue() < RADIUS + Tolerance.epsilon;
    }

    protected void markActionAsExecuted() {
        wasLastActionExecuted = true;
    }

    protected void requireNoCooldown(Action action) {
        if (hasCooldown()) throw new IllegalActionDuringCooldown(action.getClass().getName());
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
