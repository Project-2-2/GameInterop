package Group6.Agent.Factory;

import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tomasz Darmetko
 */
public class AgentsFactories {

    final static private Map<String, AgentFactory> factories = new HashMap<>();

    static {
        factories.put("random", new RandomAgentFactory());
        factories.put("guard", new GuardAgentFactory());
    }

    public List<Intruder> createIntruders(String type, int number) {
        return get(type).createIntruders(number);
    }

    public List<Guard> createGuards(String type, int number) {
        return get(type).createGuards(number);
    }

    public AgentFactory get(String type) {
        return factories.get(type);
    }

}
