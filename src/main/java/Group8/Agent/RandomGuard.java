package Group8.Agent;

import Group8.Controller.Utils.GuardInfo;
import Interop.Action.*;
import Interop.Agent.Guard;
import Interop.Geometry.Angle;
import Interop.Geometry.Distance;
import Interop.Percept.GuardPercepts;

/**
 * @author Luc
 * This class is almost completely random
 */
public class RandomGuard extends Agent implements Guard {
    private enum ActionType{
        MOVE,ROTATE,YELL,NO_ACTION
    }
    public RandomGuard(GuardInfo guardInfo) {
        super(guardInfo);
    }

    @Override
    public GuardAction getAction(GuardPercepts percepts) {
        ActionType actionType = ActionType.values()[(int)(Math.random()*ActionType.values().length)];
     //   System.out.println(actionType);
        switch(actionType){
            case MOVE:
                return new Move(new Distance(1));
            case YELL:
                return new Yell();
            case ROTATE:
                return new Rotate(Angle.fromDegrees(Math.random()*45));
            case NO_ACTION:
                return new NoAction();
        }
        //If there is no returned value yet return no_action
        return new NoAction();
    }
}
