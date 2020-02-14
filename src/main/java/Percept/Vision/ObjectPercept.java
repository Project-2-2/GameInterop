package Percept.Vision;

import Geometry.LineCurve;

/**
 * Represents the visible part (perception) of an object.
 */
public class ObjectPercept {

    private ObjectPerceptType type;
    private LineCurve curve;

    public ObjectPercept(ObjectPerceptType type, LineCurve curve) {
        this.type = type;
        this.curve = curve;
    }

    public ObjectPerceptType getType() {
        return type;
    }

    public LineCurve getCurve() {
        return curve;
    }

}
