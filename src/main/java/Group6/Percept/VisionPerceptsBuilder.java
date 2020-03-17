package Group6.Percept;

import Group6.WorldState.AgentState;
import Group6.WorldState.WorldState;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.Vision.FieldOfView;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.Collections;

public class VisionPerceptsBuilder {
    public VisionPrecepts buildPercepts(WorldState worldState, AgentState agentState) {
        return new VisionPrecepts(
            new FieldOfView(
                new Distance(0),
                Angle.fromDegrees(45)
            ),
            new ObjectPercepts(Collections.emptySet())
        );
    }
}
