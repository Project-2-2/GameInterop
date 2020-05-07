package Group9.agent.factories;

import Interop.Action.Move;
import Interop.Action.NoAction;
import Interop.Agent.Guard;
import Interop.Agent.Intruder;
import Interop.Geometry.Distance;

import java.util.LinkedList;
import java.util.List;

public class DummyAgentFactory implements IAgentFactory {

    private final boolean tinyMovement;

    public DummyAgentFactory(boolean tinyMovement)
    {
        this.tinyMovement = tinyMovement;
    }

    @Override
    public List<Intruder> createIntruders(int amount) {
        List<Intruder> intruders = new LinkedList<>();
        for(int i = 0; i < amount; i++)
        {
            intruders.add(percepts -> {
                if(tinyMovement)
                {
                    return new Move(new Distance(0));
                }
                return new NoAction();
            });
        }
        return intruders;
    }

    @Override
    public List<Guard> createGuards(int amount) {
        List<Guard> guards = new LinkedList<>();
        for(int i = 0; i < amount; i++)
        {
            guards.add(percepts -> {
                if(tinyMovement)
                {
                    return new Move(new Distance(0));
                }
                return new NoAction();
            });
        }
        return guards;
    }

}
