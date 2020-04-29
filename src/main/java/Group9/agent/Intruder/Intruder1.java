package Group9.agent.Intruder;

import Group9.agent.container.IntruderContainer;
import Group9.math.Vector2;
import Interop.Action.IntruderAction;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Geometry.Direction;
import Interop.Geometry.Distance;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.VisionPrecepts;

public class Intruder1 implements Intruder {
    Cell position = new Cell();
    History history = new History();
    public Intruder1()
    {

    }

    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        //building the "mind-map" of the building
        Direction direction = percepts.getTargetDirection();
        Angle alpha = percepts.getVision().getFieldOfView().getViewAngle();
        Distance range = percepts.getVision().getFieldOfView().getRange();
        return null;
    }
}
