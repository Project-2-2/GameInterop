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
public class AgentsFactory implements IAgentFactory{

    //minor change
    public List<Intruder> createIntruders(int number) {
        List<Intruder> intruders = new LinkedList<>();
        for(int i = 0; i < number; i++){
            intruders.add(new OurIntruder());
        }
        return intruders;
    }
    public List<Guard> createGuards(int number) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            guards.add(new FollowGuard());
        }
        return guards;
    }
}
