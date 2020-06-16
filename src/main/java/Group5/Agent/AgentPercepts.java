package Group5.Agent;

import Group5.GameController.AgentController;
import Interop.Percept.Smell.SmellPercept;
import Interop.Percept.Sound.SoundPercept;
import Interop.Percept.Vision.ObjectPercept;

import java.util.Hashtable;
import java.util.Set;

public class AgentPercepts {
    Hashtable<AgentController , Graph> visionGraphs = new Hashtable<>(); //Hash table containing the agent and is vision graph
    Integer idCounter = 0; //id for the vision

    /*
    Set<SoundPercept> sounds;
    Set<SmellPercept> smells;
    */

    /*
    public void updateVision(Set<ObjectPercept> newObjectPerceived, AgentController agent) {
        if (!visionGraphs.containsKey(agent))
            visionGraphs.put(agent, new Graph(agent));

        for (ObjectPercept object : newObjectPerceived) {
            visionGraphs.get(agent).addVertex(new ObjectPerceptVertex(false , object));
            idCounter++;
        }

    }


     */
}
