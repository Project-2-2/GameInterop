package Group5.Agent;

import Group5.GameController.AgentController;
import Interop.Percept.Vision.ObjectPercept;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class AgentVision {
    Hashtable<AgentController, Graph> graphs = new Hashtable<>();
    Integer idCounter = 0;

    public void updateVision(Set<ObjectPercept> newObjectPerceived, AgentController agent) {
        if (!graphs.containsKey(agent))
            graphs.put(agent, new Graph(agent));

        for (ObjectPercept object : newObjectPerceived) {
            graphs.get(agent).addVertex(new Vertex(idCounter.toString(), false , object));
            idCounter++;
        }

    }

}
