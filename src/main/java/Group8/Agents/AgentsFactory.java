package Group8.Agents;

import Group9.agent.factories.IAgentFactory;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;


import java.util.List;

/**
 * This class provides common way to build agents for the competition.
 *
 * Sharing knowledge between agents is NOT ALLOWED.
 *
 * For example:
 * Agents must not hold ANY references to common objects or references to each other.
 */
public class AgentsFactory {
    private final static IAgentFactory agentFactory = new AgentFactoryImpl();

    static public List<Intruder> createIntruders(int number) {
        return agentFactory.createIntruders(number);
    }
    static public List<Guard> createGuards(int number) {
        return agentFactory.createGuards(number);
    }

}