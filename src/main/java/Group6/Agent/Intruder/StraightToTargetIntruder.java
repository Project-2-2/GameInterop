package Group6.Agent.Intruder;

import Group6.Agent.ActionsFactory;
import Group6.Agent.Behaviour.DisperseBehaviour;
import Group6.Agent.Behaviour.ToTargetBehaviour;
import Group6.Agent.Behaviour.ToTeleportBehaviour;
import Group6.Geometry.Direction;
import Interop.Action.IntruderAction;
import Interop.Action.NoAction;
import Interop.Agent.Intruder;
import Interop.Percept.IntruderPercepts;

/**
 * @author Tomasz Darmetko
 */
public class StraightToTargetIntruder implements Intruder {

    private final RandomIntruder randomAgent = new RandomIntruder();

    private final ToTeleportBehaviour teleportBehaviour = new ToTeleportBehaviour();
    private final DisperseBehaviour disperseBehaviour = new DisperseBehaviour();
    private final ToTargetBehaviour toTargetBehaviour = new ToTargetBehaviour();

    public IntruderAction getAction(IntruderPercepts percepts) {

        teleportBehaviour.updateState(percepts);
        disperseBehaviour.updateState(percepts);
        toTargetBehaviour.updateState(percepts);

        if(teleportBehaviour.shouldExecute(percepts)) {
            return (IntruderAction)teleportBehaviour.getAction(percepts);
        }

        if(disperseBehaviour.shouldExecute(percepts)) {
            return (IntruderAction)disperseBehaviour.getAction(percepts);
        }

        if(toTargetBehaviour.shouldExecute(percepts)) {
            return (IntruderAction)toTargetBehaviour.getAction(percepts);
        }

        return randomAgent.getAction(percepts);

    }

}
