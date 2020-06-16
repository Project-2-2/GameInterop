package Group11;

import Group11.Agents.Guards.FollowerGuard;
import Group11.Agents.Guards.RandomGuard;
import Group11.Agents.Intruders.RandomIntruder;
import Group11.Agents.Intruders.SomeIntruder;
import Group9.agent.RandomAgent;
import Group9.agent.RandomIntruderAgent;
import Group9.agent.factories.IAgentFactory;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
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
     static public List<Intruder> createIntruders(int number) {
        List<Intruder> intruders = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            intruders.add(new SomeIntruder());
        }
        return intruders;
    }
    static public List<Guard> createGuards(int number) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            guards.add(new FollowerGuard());
        }
        return guards;
    }
}