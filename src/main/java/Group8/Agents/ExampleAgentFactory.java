package Group8.Agents;

import Group9.agent.factories.IAgentFactory;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.List;

public class ExampleAgentFactory implements IAgentFactory {
    @Override
    public List<Intruder> createIntruders(int num) {
        return null; // Return the proper agent here
    }

    @Override
    public List<Guard> createGuards(int num) {
        return null; // Return the proper agent here
    }
}
