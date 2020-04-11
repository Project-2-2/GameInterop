package Group9.agent.factories;

import Group9.agent.DeepSpace;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.List;

public class DeepSpaceFactory implements IAgentFactory {
    @Override
    public List<Intruder> createIntruders(int amount) {
        return new ArrayList<>();
    }

    @Override
    public List<Guard> createGuards(int amount) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < amount; i++)
        {
            guards.add(new DeepSpace());
        }
        return guards;
    }
}
