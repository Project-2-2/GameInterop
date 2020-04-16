package Group9.agent.deepspace;

import Group9.math.Vector2;
import Interop.Action.GuardAction;
import Interop.Action.NoAction;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Vision.ObjectPerceptType;

import java.util.*;

public class StateHandlerFindNewTarget implements StateHandler {
    private DeepSpace ds;

    private StateType nextState;

    private boolean active;

    Queue<GuardAction> actionsQueue = new LinkedList<>();

    @Override
    public GuardAction execute(GuardPercepts percepts, DeepSpace deepSpace) {
        GuardAction retAction = new NoAction();

        this.ds = deepSpace;

        if (!active) {
            // add actions to queue to get to new best target
            findNewTarget(percepts);
            active = true;
        }

        // if this is an ongoing state, execute queue
        if (!actionsQueue.isEmpty()) {
            retAction = actionsQueue.poll();
        }

        postExecute();
        return retAction;
    }

    @Override
    public StateType getNextState() {
        return nextState;
    }

    void postExecute() {
        if (actionsQueue.isEmpty()) {
            nextState = StateType.EXPLORE_360;
            active = false;
        } else {
            nextState = StateType.FIND_NEW_TARGET;
        }
    }

    private void findNewTarget(GuardPercepts guardPercepts)
    {

        Map<ObjectPerceptType, HashSet<Vector2>> map = ds.getCurrentVertex().getContent().getObjects();
        ObjectPerceptType[] priority = new ObjectPerceptType[] {ObjectPerceptType.SentryTower, ObjectPerceptType.Door,
                                            ObjectPerceptType.Window, ObjectPerceptType.Teleport, ObjectPerceptType.EmptySpace};

        for(ObjectPerceptType type : priority)
        {
            Set<Vector2> positions = map.get(type);
            if(positions != null && !positions.isEmpty())
            {
                Optional<Vector2> target = positions.stream().filter(e -> !ds.isInsideOtherVertex(ds.getCurrentVertex(), e)).findAny();
                if(target.isPresent())
                {
                    actionsQueue.addAll(ds.moveTowardsPoint(guardPercepts, ds.getPosition(), ds.getDirection(), target.get()));
                    // todo: Maybe Hoare might have a point this time
                    return;
                }
            }
        }

        ds.getCurrentVertex().getContent().setDeadend(true);

        // backtrack: get a list of actions to move us to a prev node/position that didn't lead to (or wasn't) a deadend
        Queue<GuardAction> actions = ds.backtrack(guardPercepts);
        if(actions.isEmpty())
        {
            System.out.println("Count: " + Arrays.toString(ds.graph.getVertices().stream().filter(e -> !e.getContent().isDeadend()).toArray()));
        }
        else
        {
            actionsQueue.addAll(actions);
        }
    }
}
