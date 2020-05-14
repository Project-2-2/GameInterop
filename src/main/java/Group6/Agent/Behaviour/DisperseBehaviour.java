package Group6.Agent.Behaviour;

import Group6.Agent.ActionsFactory;
import Group6.Agent.PerceptsService;
import Group6.GUI.Agent;
import Interop.Action.Action;
import Interop.Action.Rotate;
import Interop.Geometry.Angle;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Utils.Utils;


import java.util.Random;

/**
 * @author Tomasz Darmetko
 */
public class DisperseBehaviour implements Behaviour {

    private int tillNextDisperse = 0;

    public Action getAction(Percepts percepts) {

        if(new Random().nextBoolean()) tillNextDisperse = 5;
        return ActionsFactory.getRandomRotate(percepts);

    }

    public boolean shouldExecute(Percepts percepts) {
        if(tillNextDisperse > 0) return false;
        return !PerceptsService
            .getAgentPercepts(percepts)
            .getAll()
            .isEmpty();
    }

    public void updateState(Percepts percepts) {
        if(tillNextDisperse > 0) tillNextDisperse--;
    }

}
