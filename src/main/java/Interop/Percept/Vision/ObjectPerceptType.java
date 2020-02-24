package Interop.Percept.Vision;

/**
 * Lists all possible object types, detectable by vision.
 */
public enum ObjectPerceptType {

    Guard, Intruder,
    Wall, Window, Door,
    Teleport,
    SentryTower,
    ShadedArea,
    TargetArea,
    EmptySpace;

    /**
     * @return Whether the perceived object is an agent.
     */
    public boolean isAgent() {
        switch (this) {
            case Intruder:
            case Guard:
                return true;
            default:
                return false;
        }
    }

    public boolean isSolid() {
        if(isAgent()) return true;
        return this == Wall; // only wall is solid
    }

    public boolean isOpaque() {
        return isSolid() || isAgent(); // solid objects and agents are opaque
    }

    public boolean isOpaqueFromOutside() {
        if(isOpaque()) return true; // opaque objects are opaque from outside
        switch (this) {
            case Door:
            case ShadedArea:
            case SentryTower:
                return true;
            default:
                return false;
        }
    }

    public boolean isVisibleByGuard() {
        return this != TargetArea; // only target area not visible by guard
    }

    public boolean isVisibleByIntruder() {
        return true; //everything is visible by intruder
    }

}
