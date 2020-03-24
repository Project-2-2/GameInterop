package Group6.Percept.Vision;

import Group6.Geometry.Point;
import Group6.WorldState.Object.AgentState;
import Interop.Percept.Vision.ObjectPerceptType;

public class ObjectPercept {

    private ObjectPerceptType objectPerceptType;
    private Point point;

    public ObjectPercept(ObjectPerceptType objectPerceptType, Point point) {
        this.objectPerceptType = objectPerceptType;
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public ObjectPerceptType getType() {
        return objectPerceptType;
    }

    public ObjectPercept shiftPerspective(Point newOrigin) {
        return new ObjectPercept(
            getType(),
            point.subtract(newOrigin).toPoint()
        );
    }

    public Interop.Percept.Vision.ObjectPercept toInterop() {
        return new Interop.Percept.Vision.ObjectPercept(
            objectPerceptType,
            point.toInteropPoint()
        );
    }

    public String toString() {
        return "ObjectPercept{" +
            "type=" + objectPerceptType +
            ", point=" + point +
            '}';
    }

}
