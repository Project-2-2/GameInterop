package Group6.Agent.Guard;

import Group6.Agent.Behaviour.*;
import Interop.Action.GuardAction;
import Interop.Agent.Guard;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

import java.util.Set;



public class ScoutGuard implements Guard {

    public GuardAction getAction(GuardPercepts percepts) {

        if(!getIntruder(percepts).getAll().isEmpty()){
            return(GuardAction)new FollowIntruderBehaviour().getAction(percepts);
        }
        if(getSoundYell(percepts).size() > 0){
            return (GuardAction)new FollowYellBehaviour().getAction(percepts);
        }
        else{
            return (GuardAction)new ExploreBehaviour().getAction(percepts);
        }
    }


    public static ObjectPercepts getIntruder(Percepts percepts) {
        return percepts.getVision().getObjects().filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Intruder);
    }
    public static Set<SoundPercept> getSoundYell(Percepts percept) {
        return percept.getSounds().filter(soundPercept -> soundPercept.getType() == SoundPerceptType.Yell).getAll();
    }

}
