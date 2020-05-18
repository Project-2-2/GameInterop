package Group6.Agent.Factory;

import Group6.Agent.Guard.RandomGuard;
import Group6.Agent.Guard.ScoutGuard;
import Group6.Agent.Intruder.RandomIntruder;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.List;

public class GuardAgentFactory implements AgentFactory{

    public List<Intruder> createIntruders(int number) {
        List<Intruder> agents = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            agents.add(new RandomIntruder());
        }
        return  agents;
    }

    public List<Guard> createGuards(int number) {
        List<Guard> agents = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            agents.add(new ScoutGuard());
        }
        return  agents;
    }
}
