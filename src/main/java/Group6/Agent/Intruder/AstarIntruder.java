package Group6.Agent.Intruder;




import Group6.Agent.Behaviour.Behaviour;
import Group6.Agent.Behaviour.ExploreBehaviour;
import Group6.Agent.RandomAgent;
import Group6.Geometry.Collection.Points;
import Group6.Geometry.Direction;
import Group6.Geometry.Line;
import Group6.Geometry.LineSegment;
import Group6.Geometry.Point;
import Group6.WorldState.Contract.Object;
import Interop.Geometry.Distance;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPercepts;
import Group6.Utils;
import Group6.Percept.*;
import Group9.agent.RandomIntruderAgent;
import Interop.Action.IntruderAction;
import Interop.Action.Move;
import Interop.Action.Rotate;
import Interop.Agent.Intruder;
import Interop.Geometry.Angle;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.VisionPrecepts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AstarIntruder implements  Intruder{
    @Override
    public IntruderAction getAction(IntruderPercepts percepts) {
        VisionPrecepts visionPrecepts = percepts.getVision();
        Iterator iter = visionPrecepts.getObjects().getAll().iterator();
        if(iter.hasNext()){
            ObjectPercept obj = (ObjectPercept) iter.next();
        }
        return null;
    }
}
