package Group9.agent.factories;

import Group9.agent.RandomIntruderAgent;
import Group9.agent.deepspace.DeepSpace;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.List;

public class DeepSpaceAgentFactory implements IAgentFactory {

    public List<Intruder> createIntruders(int number) {
        List<Intruder> intruders = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            intruders.add(new RandomIntruderAgent());
        }
        return intruders;
    }

    public List<Guard> createGuards(int number) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            guards.add(new DeepSpace());
        }
        return guards;
    }

}
