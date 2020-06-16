package Group11;

import Group11.Agents.Guards.FollowerGuard;
import Group11.Agents.Intruders.SomeIntruder;
import Group9.agent.factories.IAgentFactory;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.List;

public class CoolAgentFactory implements IAgentFactory {
    public List<Intruder> createIntruders(int number) {
        List<Intruder> intruders = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            intruders.add(new SomeIntruder());
        }
        return intruders;
    }
    public List<Guard> createGuards(int number) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            guards.add(new FollowerGuard());
        }
        return guards;
    }
}
