package Group6.Agent.Behaviour;

import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Action.DropPheromone;
import Interop.Percept.Percepts;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

public class DropPheromoneBehaviour implements Behaviour {

    private int cooldown = 0;

    public Action getAction(Percepts percepts) {
        cooldown = 500;
        return new DropPheromone(SmellPerceptType.Pheromone1);
    }

    public boolean shouldExecute(Percepts percepts) {

        if(cooldown > 0) return false;
        if(hasSmells(percepts)) return false;

        ObjectPercepts specialPercepts = percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> {
                return
                    objectPercept.getType() == ObjectPerceptType.Teleport ||
                    objectPercept.getType() == ObjectPerceptType.TargetArea;
            });

        return specialPercepts.getAll().size() > 0;

    }

    public void updateState(Percepts percepts) {
        if(cooldown > 0) cooldown--;
        if(hasSmells(percepts)) cooldown = cooldown + 10;
    }

    private boolean hasSmells(Percepts percepts) {
        return percepts.getSmells().getAll().size() > 0;
    }
}
