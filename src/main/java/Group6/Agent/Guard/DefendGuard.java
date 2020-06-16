package Group6.Agent.Guard;

import Group6.Agent.Behaviour.*;
import Interop.Action.GuardAction;
import Interop.Agent.Guard;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DefendGuard implements Guard {



    public GuardAction getAction(GuardPercepts percepts) {

        if(!getIntruder(percepts).getAll().isEmpty()){
            return(GuardAction)new FollowIntruderBehaviour().getAction(percepts);
        }
        if(!getTarget(percepts).getAll().isEmpty()){
            return (GuardAction)new YellBehaviour().getAction(percepts);
        }
        if(!getDoor(percepts).getAll().isEmpty() || !getWindow(percepts).getAll().isEmpty()){
            return (GuardAction)new ToPassageBehaviour().getAction(percepts);
        }
        else{
            return (GuardAction)new ToTargetBehaviour().getAction(percepts);
        }
    }


    public static ObjectPercepts getTarget(Percepts percepts) {
        return percepts.getVision().getObjects().filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.TargetArea);
    }
    public static ObjectPercepts getIntruder(Percepts percepts) {
        return percepts.getVision().getObjects().filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Intruder);
    }
    public static ObjectPercepts getDoor(Percepts percepts){
        return percepts.getVision().getObjects().filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Door);
    }
    public static ObjectPercepts getWindow(Percepts percepts){
        return percepts.getVision().getObjects().filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Window);
    }
}
