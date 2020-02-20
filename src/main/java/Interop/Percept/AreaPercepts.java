package Interop.Percept;

/**
 * This class represents perception of the current area where agent is located.
 * This perception is relevant for the scenario percepts, especially slow down modifiers.
 * @see Interop.Percept.Scenario.SlowDownModifiers
 */
public final class AreaPercepts {

    private boolean inWindow;
    private boolean inDoor;
    private boolean inSentryTower;
    private boolean justTeleported;

    public AreaPercepts(boolean inWindow, boolean inDoor, boolean inSentryTower, boolean justTeleported) {
        this.inWindow = inWindow;
        this.inDoor = inDoor;
        this.inSentryTower = inSentryTower;
        this.justTeleported = justTeleported;
    }

    public boolean isInWindow() {
        return inWindow;
    }

    public boolean isInDoor() {
        return inDoor;
    }

    public boolean isInSentryTower() {
        return inSentryTower;
    }

    /**
     * An agent that was just teleported can not be teleported again unless it leaves the teleport area.
     * @return Whether the agent just teleported.
     */
    public boolean isJustTeleported() {
        return justTeleported;
    }

}
