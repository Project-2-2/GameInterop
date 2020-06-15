package Group8.Agents.Guard;

import Group8.PathFinding.FSM;
import Interop.Action.GuardAction;
import Interop.Action.IntruderAction;
import Interop.Percept.GuardPercepts;
import Interop.Percept.IntruderPercepts;
import Interop.Percept.Vision.ObjectPercept;

import java.util.LinkedList;
import java.util.List;

public class FSMGuard {

    private double COLLISION_ROT = Math.PI;

    private LinkedList<GuardAction> guardActionQueue;
    // Represents the queue containing actions with high priority
    private LinkedList<GuardAction> guardPrioQueue;

    private GuardPercepts guardCurrentPercepts;

    private List<List<ObjectPercept>> generalObstructions;
    private List<ObjectPercept> objectPercepts;
    private List<ObjectPercept> agentObstructions;

    private boolean check = false;
}
