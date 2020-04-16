package Interop.Percept.Scenario;

import Interop.Utils.Require;

/**
 * Represents an agent perception of different slow down modifiers in different areas.
 *
 * This percepts allow agents to avoid issuing invalid move and sprint actions.
 *
 * AgentGui can perceive the current area based on area percepts:
 * @see Interop.Percept.AreaPercepts
 */
public final class SlowDownModifiers {

    private double inWindow;
    private double inDoor;
    private double inSentryTower;

    public SlowDownModifiers(double inWindow, double inDoor, double inSentryTower) {
        checkSlowDownModifier(inWindow);
        checkSlowDownModifier(inDoor);
        checkSlowDownModifier(inSentryTower);
        this.inWindow = inWindow;
        this.inDoor = inDoor;
        this.inSentryTower = inSentryTower;
    }

    private void checkSlowDownModifier(double modifier) {
        Require.realNumber(modifier, "A slow down modifier must be real number!");
        Require.positive(modifier, "A slow down modifier must be positive number!");
        if(modifier > 1) {
            throw new RuntimeException(
                "A slow down modifier must be 1 or smaller in order to slow down!\n" +
                "Modifier given: " + modifier
            );
        }
    }

    public double getInWindow() {
        return inWindow;
    }

    public double getInDoor() {
        return inDoor;
    }

    public double getInSentryTower() {
        return inSentryTower;
    }

}
