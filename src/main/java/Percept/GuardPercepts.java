package Percept;

import Geometry.Direction;
import Percept.Scenario.ScenarioGuardPercepts;
import Percept.Smell.SmellPercepts;
import Percept.Sound.SoundPercepts;
import Percept.Vision.VisionPrecepts;
import Utils.Require;

/**
 * Represents percepts of an agent, including percepts specific to a guard agent.
 *
 * Please, make sure that you review the documentation of the parent class!
 *
 * @see Percepts
 */
public class GuardPercepts extends Percepts {

    private ScenarioGuardPercepts scenarioGuardPercepts;

    public GuardPercepts(
        VisionPrecepts vision,
        SoundPercepts sounds,
        SmellPercepts smells,
        AreaPercepts areaPercepts,
        ScenarioGuardPercepts scenarioGuardPercepts,
        boolean wasLastActionExecuted
    ) {
        super(vision, sounds, smells, areaPercepts, wasLastActionExecuted);
        Require.notNull(scenarioGuardPercepts);
        this.scenarioGuardPercepts = scenarioGuardPercepts;
    }

    public ScenarioGuardPercepts getScenarioGuardPercepts() {
        return scenarioGuardPercepts;
    }

}
