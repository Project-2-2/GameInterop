package Percept.Vision;

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

}
