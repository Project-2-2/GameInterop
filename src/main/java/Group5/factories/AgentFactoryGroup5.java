package Group5.factories;

import Group5.Agent.Explorer;
import Group5.Agent.Guard.GraphExplorer;
import Group5.Agent.IntruderAgent;
import Group5.Agent.Guard.GuardExplorer;
import Group9.agent.RandomAgent;
import Group9.agent.RandomIntruderAgent;
import Group9.agent.factories.IAgentFactory;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.List;

public class AgentFactoryGroup5 implements IAgentFactory {


    @Override
    public List<Intruder> createIntruders(int number) {
        List<Intruder> intruders = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            intruders.add(new IntruderAgent());
            //intruders.add(new RandomIntruderAgent());
        }
        return intruders;
    }

    @Override
    public List<Guard> createGuards(int number) {
        List<Guard> guards = new ArrayList<>();
        for(int i = 0; i < number; i++)
        {
            //Change here the type of intruder you want

            //guards.add(new GuardExplorer());
            guards.add(new GraphExplorer());
            //guards.add(new DeepSpace());
        }
        return guards;
    }
}
