package Group4;

import Group4.Guards.PatrolGuard;
import Group4.Guards.FollowGuard;
import Group4.Intruder.OurIntruder;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Group9.agent.factories.*;

import java.util.ArrayList;
import java.util.LinkedList;
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

    static public IAgentFactory getIAgentFactoryInstance() {
        return new IAgentFactory() {
            public List<Intruder> createIntruders(int amount) {
                return AgentsFactory.createIntruders(amount);
            }

            public List<Guard> createGuards(int amount) {
                return AgentsFactory.createGuards(amount);
            }
        };
    }
    //minor change
    static public List<Intruder> createIntruders(int amount) {
        List<Intruder> intruders = new LinkedList<>();
        for(int i = 0; i < amount; i++){
            intruders.add(new OurIntruder());
        }
        return intruders;
    }
    static public List<Guard> createGuards(int amount) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < amount; i++)
        {
            guards.add(new FollowGuard());
        }
        return guards;
    }
}
