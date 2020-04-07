package Group5.GameController;

import Interop.Geometry.Point;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Smell.SmellPerceptType;
import Interop.Percept.Smell.SmellPercepts;
import Interop.Percept.Sound.SoundPercept;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Smell {
    /**
     * @param agent the agent you want to update smell
     * @return A smellPerceptType (object containing the perceived pheromone)
     */
    public SmellPercepts getSmell(AgentController agent) {
        Point position = agent.getPosition();
        String agentType = agent.getAgentType();

        //TODO add the pheromones to the game runner/map info
     //  ArrayList<Pheromone> guardPheromones =  GameRunner.StoredPheromones.getGuardPheromones();





        return null;
    }
}
