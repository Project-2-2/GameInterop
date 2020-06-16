package Group3;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import java.util.ArrayList;
import java.util.List;
import Group9.agent.factories.IAgentFactory;

/**
 * This class provides common way to build agents for the competition.
 *
 * Sharing knowledge between agents is NOT ALLOWED.
 *
 * For example:
 * Agents must not hold ANY references to common objects or references to each other.
 */
public class IAgentsFactoryGroup3 implements IAgentFactory{
    public List<Intruder> createIntruders(int number) {
        
    	List<Intruder> intruders = new ArrayList<Intruder>();
    	
    	for(int i = 0; i < number; i++) {
    		Group3.Intruder.Intruder intruder =  new Group3.Intruder.Intruder();
    		intruders.add(intruder);
    	}
    	
    	return intruders;
    }
    public List<Guard> createGuards(int number) {
    	
    	List<Guard> guards = new ArrayList<Guard>();
    	for(int i = 0; i < number; i++) {
    		Group3.Guard2.Guard2 guard =  new Group3.Guard2.Guard2();
    		//Group3.ExplorationAgent agent = new Group3.ExplorationAgent();
    		guards.add(guard);
    		//guards.add(agent);
    	}
    	return guards;
    }
}