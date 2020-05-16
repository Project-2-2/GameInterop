package Group8.Agents;

import Group9.agent.factories.IAgentFactory;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;

import java.util.ArrayList;
import java.util.List;

public class AgentFactoryImpl implements IAgentFactory{
    public static final AlgoG GUARD_ALGORITHM= AlgoG.AI1;
    public static final AlgoI INTRUDER_ALGORITHM = AlgoI.AI1;

    public enum AlgoI {
        AI1,
        AI2;
    }
    public enum AlgoG {
        AI1,
        AI2;

    }

    @Override
     public List<Intruder> createIntruders(int number) {
        List<Intruder> intruders = new ArrayList<>();

        for(int i=0; i<number; i++){
            switch(INTRUDER_ALGORITHM) {
                case AI1:
                    intruders.add(null);
                    break;
                case AI2:
                    //intruders.add(new IntruAlgo2());
                    break;
            }
        }

        return intruders;
    }

    @Override
     public List<Guard> createGuards(int number) {
        List<Guard> guards = new ArrayList<>();

        for(int i=0; i<number; i++){
            switch(GUARD_ALGORITHM) {
                case AI1:
                    guards.add(null);
                    break;
                case AI2:
                    //guards.add(new GuardAlgo2());
                    break;
            }
        }

        return guards;
    }
}
