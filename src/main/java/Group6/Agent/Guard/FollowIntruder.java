package Group6.Agent.Guard;

import Group6.Agent.Behaviour.*;
import Group6.Agent.BehaviourBasedAgent;
import Group6.Geometry.Distance;
import Interop.Action.GuardAction;
import Interop.Action.Rotate;
import Interop.Agent.Guard;
import Interop.Percept.GuardPercepts;
import Interop.Percept.Percepts;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Sound.SoundPerceptType;
import Interop.Percept.Sound.SoundPercepts;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;
import Interop.Action.Move;

public class FollowIntruder implements Guard {

    @Override
    public GuardAction getAction(GuardPercepts percepts) {

        //The guard smells an intruder
        if(!intruderSmell(percepts).getAll().isEmpty()){
            int i=0;
            for (SmellPercept smellPercept: intruderSmell(percepts).getAll()){
                //if more than one smell is detected, then the agent choose the first one BY DEFAULT
                if(i==0){
                    new Move(smellPercept.getDistance());
                    }
            }
            return(GuardAction)new ExploreBehaviour(G.getAction(percepts);
        }

        //The guard hears an intruder
        else if(!intruderSound(percepts).getAll().isEmpty()){
            int i=0;
            for (SoundPercept soundPercept: intruderSound(percepts).getAll()){
                //if more than one sound is detected, then the agent choose the first one BY DEFAULT
                if(i==0){
                    new Rotate(soundPercept.getDirection());
                    new Move(percepts.getScenarioGuardPercepts().getMaxMoveDistanceGuard());
                }
            }
            return(GuardAction)new FollowYellBehaviour().getAction(percepts);

        }

        //The guard sees an intruder
        else if(!intruderVision(percepts).getAll().isEmpty()){
            int i=0;
            for(ObjectPercept objectPercept:intruderVision(percepts).getAll()){
                //if the agent sees more than one intruder, then the agent choose the first one BY DEFAULT
                if(i==0){
                    new Move(objectPercept.getPoint().getDistanceFromOrigin());
                }
            }
            return(GuardAction)new FollowIntruderBehaviour().getAction(percepts);
        }

        // The guards doesn't perceive anything
        else {
            return (GuardAction)new ExploreBehaviour().getAction(percepts);
        }
    }

    public static SmellPercepts intruderSmell(Percepts percepts) {

        return percepts.getSmells().filter(objectPercept -> objectPercept.getType() == SmellPerceptType.Pheromone1
                || objectPercept.getType() == SmellPerceptType.Pheromone2
                || objectPercept.getType() == SmellPerceptType.Pheromone3
                || objectPercept.getType() == SmellPerceptType.Pheromone4
                || objectPercept.getType() == SmellPerceptType.Pheromone5);

    }

    public static SoundPercepts intruderSound(Percepts percepts) {

        return percepts.getSounds().filter(objectPercept -> objectPercept.getType() == SoundPerceptType.Noise
                || objectPercept.getType() == SoundPerceptType.Yell);

    }

    public static ObjectPercepts intruderVision(Percepts percepts) {

        return percepts
                .getVision()
                .getObjects()
                .filter(objectPercept -> objectPercept.getType() == ObjectPerceptType.Intruder);

    }
}