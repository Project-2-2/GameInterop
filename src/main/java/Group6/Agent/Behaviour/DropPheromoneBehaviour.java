package Group6.Agent.Behaviour;

import Group6.Agent.PerceptsService;
import Interop.Action.Action;
import Interop.Action.DropPheromone;
import Interop.Percept.Percepts;
import Interop.Percept.Scenario.ScenarioPercepts;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Vision.ObjectPercept;
import Interop.Percept.Vision.ObjectPerceptType;
import Interop.Percept.Vision.ObjectPercepts;

/**
 * @author Tomasz Darmetko
 */
public class DropPheromoneBehaviour implements Behaviour {

    private int backoff = 5;
    private int cooldown = 0;
    private int passageCooldown;

    public Action getAction(Percepts percepts) {

        ScenarioPercepts scenarioPercepts = PerceptsService.getScenarioPercepts(percepts);

        ObjectPercepts specialPercepts = percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> {
                return
                    objectPercept.getType() == ObjectPerceptType.Teleport ||
                    objectPercept.getType() == ObjectPerceptType.TargetArea;
            });

        if(specialPercepts.getAll().size() == 0) {
            passageCooldown = backoff * scenarioPercepts.getPheromoneCooldown();
            backoff = backoff * 5;
        }

        cooldown = 100 * scenarioPercepts.getPheromoneCooldown();

        return new DropPheromone(SmellPerceptType.Pheromone1);

    }

    public boolean shouldExecute(Percepts percepts) {

        if(cooldown > 0) return false;
        if(hasSmells(percepts)) return false;

        ObjectPercepts specialPercepts = percepts
            .getVision()
            .getObjects()
            .filter(objectPercept -> {

                boolean shouldReactToPassage = passageCooldown == 0;
                boolean passage = shouldReactToPassage && (
                  objectPercept.getType() == ObjectPerceptType.Door ||
                  objectPercept.getType() == ObjectPerceptType.Window
                );

                return passage ||
                    objectPercept.getType() == ObjectPerceptType.Teleport ||
                    objectPercept.getType() == ObjectPerceptType.TargetArea;

            });

        return specialPercepts.getAll().size() > 0;

    }

    public void updateState(Percepts percepts) {
        if(cooldown > 0) cooldown--;
        if(passageCooldown > 0) passageCooldown--;
        if(hasSmells(percepts)) cooldown = cooldown + 10;
    }

    private boolean hasSmells(Percepts percepts) {
        return percepts.getSmells().getAll().size() > 0;
    }
}
