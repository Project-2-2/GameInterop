package Group8.Agents;

import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.Collections;
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
    private static IAgentFactory iAgentFactory = new ExampleAgentFactory();
    static public List<Intruder> createIntruders(int number) {
        return iAgentFactory.createIntruders(number);
    }
    static public List<Guard> createGuards(int number) {
        return iAgentFactory.createGuards(number);
    }
}