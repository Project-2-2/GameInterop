package Group6.Agent.Behaviour;

import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Action.Yell;
import Interop.Percept.Percepts;
import Interop.Percept.Sound.SoundPerceptType;

/**
 * @author Tomasz Darmetko
 */
public class YellBehaviour implements Behaviour {

    private int avoidYelling = 0;

    public Action getAction(Percepts percepts) {
        avoidYelling = 20;
        return new Yell();
    }

    public boolean shouldExecute(Percepts percepts) {
        return avoidYelling == 0 && PerceptsService
            .getIntruderPercepts(percepts)
            .getAll()
            .size() > 0;
    }

    public void updateState(Percepts percepts) {
        boolean hearsYell = percepts
            .getSounds()
            .filter(soundPercept -> soundPercept.getType() == SoundPerceptType.Yell)
            .getAll()
            .size() > 0;
        if(avoidYelling > 0) avoidYelling--;
        if(hearsYell) avoidYelling += 5;
    }
}
