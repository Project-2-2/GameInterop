package Group5.GameController;

import Interop.Geometry.Distance;
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
     * @return SmellPercepts (object containing all perceived objects )
     */

    public SmellPercepts getSmell(AgentController agent) {
        Set<SmellPercept> smellPerceptsSet= new HashSet<>();

        Point agentPosition = agent.getPosition();
        String agentType = agent.getAgentType();

        //This return the pheromone storage where all the pheromones are added
        PheromoneStorage pheromoneStorage = GameRunner.pheromoneStorage;
        ArrayList<Pheromone> guardPheromones = pheromoneStorage.getGuardPheromones();
        ArrayList<Pheromone> intruderPheromones= pheromoneStorage.getIntruderPheromones();

        if(agentType.equalsIgnoreCase("guard")) //Guards can only smell pheromones released by other guards
        {
            for (Pheromone guardPheromone:guardPheromones) {
                Point pheromoneLocation = guardPheromone.getLocation();
                Distance distance = new Distance(agentPosition,pheromoneLocation);
                if(distance.getValue()<=guardPheromone.getRadius())
                {
                    SmellPercept pheromoneDetected = new SmellPercept(guardPheromone.getType(),distance);
                    smellPerceptsSet.add(pheromoneDetected);
                }
            }

            return new SmellPercepts(smellPerceptsSet);

        }
        else if(agentType.equalsIgnoreCase("intruder")) //Intruders can also only smell pheromones released by other intruders
        {
            for (Pheromone intruderPheromone:intruderPheromones) {
                Point pheromoneLocation = intruderPheromone.getLocation();
                Distance distance = new Distance(agentPosition,pheromoneLocation);
                if(distance.getValue()<=intruderPheromone.getRadius())
                {
                    SmellPercept pheromoneDetected = new SmellPercept(intruderPheromone.getType(),distance);
                    smellPerceptsSet.add(pheromoneDetected);
                }
            }

            return new SmellPercepts(smellPerceptsSet);
        }



        return null;
    }
}
